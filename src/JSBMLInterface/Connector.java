package JSBMLInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import model.Vertex;
import model.VertexFactory;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SpeciesReference;

public class Connector {

	private interface ConnectorState {

		Set<Vertex> parse();

		boolean canParse();

		String fetchModelName();
	}

	private class StatelessConnectorState implements ConnectorState {

		@Override
		public Set<Vertex> parse() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean canParse() {
			return false;
		}

		@Override
		public String fetchModelName() {
			throw new UnsupportedOperationException();
		}
	}

	private class StatefullConnectorState implements ConnectorState {

		private final String data_path;

		public StatefullConnectorState(String path) {
			this.data_path = path;
		}

		private Model readModel(String path) {

			Model model = null;
			SBMLDocument document = null;

			try {
				document = (new SBMLReader()).readSBML(path);
			} catch (FileNotFoundException e) {
			} catch (XMLStreamException e) {
			} catch (Exception e) {
			}

			if (document != null) {
				model = document.getModel();
			}

			return model;
		}

		@Override
		public Set<Vertex> parse() {

			HashSet<Vertex> hashSet = new HashSet<Vertex>();

			Model model = readModel(data_path);

			// here we use the reference of our enclosing class in order to use
			// its 'readReactions' method. This seems quite a closure, although
			// is checked and supplied directly from the language.
			// To have a more functional-style code, we should pass a delegate
			// which capture the logic for reading the reaction, leaving this
			// code the responsibility to invoke it.
			hashSet.addAll(Connector.this.readReactions(model
					.getListOfReactions()));

			return hashSet;

		}

		@Override
		public boolean canParse() {
			return true;
		}

		@Override
		public String fetchModelName() {

			Model model = readModel(data_path);
			return model.getName().concat("-").concat(model.getId());
		}
	}

	private ConnectorState state;

	private Connector() {
		// private constructor to prevent non-controlled instantiation by
		// clients, use the static factories instead
	}

	public static Connector makeConnector() {
		// this method doesn't call the other overload because in this way
		// we preevent that overload to be force to check for null argument
		// in order to satify this creation request. In this way we put here the
		// logic necessary for the construction of the stateless state for the
		// connector instead.
		Connector connector = new Connector();
		connector.state = connector.new StatelessConnectorState();
		return connector;
	}

	public static Connector makeConnector(String path) {
		Connector connector = new Connector();

		connector.state = checkPathValidity(path, connector);
		return connector;
	}

	private static ConnectorState checkPathValidity(String path,
			Connector connector) {

		if (path == null || "".equals(path)
				|| (new File(path)).exists() == false) {
			return connector.new StatelessConnectorState();
		}

		return connector.new StatefullConnectorState(path);
	}

	public Set<Vertex> readReaction(Reaction reaction,
			VertexHandlingWithSourceListener listener) {

		return this.readReaction(reaction, listener,
				new HashMap<Vertex, Vertex>());
	}

	public Set<Vertex> readReaction(Reaction reaction,
			VertexHandlingWithSourceListener listener,
			Map<Vertex, Vertex> knownVertices) {

		Set<Vertex> reactants = this.convertToVertexSet(
				reaction.getListOfReactants(), knownVertices,
				new FromReactantDriver(listener));

		Set<Vertex> products = this.convertToVertexSet(
				reaction.getListOfProducts(), knownVertices,
				new FromProductDriver(listener));

		this.updateNeighbourhoodByCrossProduct(reactants, products);

		if (reaction.isReversible()) {
			this.updateNeighbourhoodByCrossProduct(products, reactants);
		}

		return knownVertices.keySet();
	}

	private void updateNeighbourhoodByCrossProduct(Set<Vertex> domainSet,
			Set<Vertex> rangeSet) {
		for (Vertex domainVertex : domainSet) {
			for (Vertex rangeVertex : rangeSet) {
				domainVertex.addNeighbour(rangeVertex);
			}
		}
	}

	public Set<Vertex> convertToVertexSet(
			ListOf<SpeciesReference> listOfSpeciesReference,
			Map<Vertex, Vertex> knownVertices, VertexHandlingListener listener) {

		Set<Vertex> result = new HashSet<Vertex>();

		for (SpeciesReference sRef : listOfSpeciesReference) {

			Vertex newVertex = VertexFactory.makeSimpleVertex(sRef
					.getSpeciesInstance());

			if (knownVertices.containsKey(newVertex) == true) {
				newVertex = knownVertices.get(newVertex);
			} else {
				knownVertices.put(newVertex, newVertex);
			}

			listener.vertexHandled(newVertex);
			result.add(newVertex);
		}

		return result;
	}

	protected class FromReactantDriver extends AbstractVertexHandlingListener {

		public FromReactantDriver(VertexHandlingWithSourceListener listener) {
			super(listener);
		}

		@Override
		public void onVertexHandled(Vertex vertex) {
			this.getListener().reactantVertexHandled(vertex);
		}
	}

	protected class FromProductDriver extends AbstractVertexHandlingListener {

		public FromProductDriver(VertexHandlingWithSourceListener listener) {
			super(listener);
		}

		@Override
		public void onVertexHandled(Vertex vertex) {
			this.getListener().productVertexHandled(vertex);
		}
	}

	public Set<Vertex> readReactions(
			Collection<Reaction> collectionOfReactions,
			VertexHandlingWithSourceListener vertexHandlingWithSourceListener) {

		Map<Vertex, Vertex> knownVertices = new HashMap<Vertex, Vertex>();
		for (Reaction reaction : collectionOfReactions) {
			this.readReaction(reaction, vertexHandlingWithSourceListener,
					knownVertices);
		}

		return knownVertices.keySet();
	}

	public Set<Vertex> readReactions(Collection<Reaction> collectionOfReactions) {

		return this.readReactions(collectionOfReactions,
				new VertexHandlerListenerNullObject());
	}

	public Set<Vertex> parseModel() {
		return state.parse();
	}

	public boolean canParse() {
		return state.canParse();
	}

	public String fetchModelName() {
		return state.fetchModelName();
	}

}
