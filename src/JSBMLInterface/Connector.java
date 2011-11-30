package JSBMLInterface;

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

	private Model sbmlModel;
	private final String path;

	private Connector(String path) {
		this.path = path;
	}

	public static Connector makeConnector() {
		return new Connector("");
	}

	public static Connector makeConnector(String path) {
		return new Connector(path);
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

	public Connector readModel() {
		SBMLDocument document = null;
		try {
			document = (new SBMLReader()).readSBML(path);
		} catch (FileNotFoundException e) {
		} catch (XMLStreamException e) {
		} catch (Exception e) {
		}

		if (document != null) {
			this.sbmlModel = document.getModel();
		}

		return this;
	}

	public Set<Vertex> parseModel() {

		HashSet<Vertex> hashSet = new HashSet<Vertex>();

		hashSet.addAll(this.readReactions(sbmlModel.getListOfReactions()));

		return hashSet;
	}

	public boolean hadYouReadSuccessfully() {
		return this.sbmlModel != null;
	}
}
