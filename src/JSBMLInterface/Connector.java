package JSBMLInterface;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import model.OurModel;
import model.Vertex;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SpeciesReference;

public class Connector {

	private Connector() {
	}

	public static Connector makeConnector() {
		return new Connector();
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

			Vertex newVertex = Vertex.makeVertex(sRef.getSpeciesInstance());

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

		Map<Vertex, Vertex> knownVertices = new HashMap<Vertex, Vertex>();
		for (Reaction reaction : collectionOfReactions) {
			this.readReaction(reaction, new VertexHandlerListenerNullObject(),
					knownVertices);
		}

		return knownVertices.keySet();
	}

	public Model parseModel(String path) {
		SBMLDocument document = null;
		Model model = null;
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

	public OurModel makeOurModel(String path) {
		Connector connector = Connector.makeConnector();
		Model sbmlModel = connector.parseModel(path);

		if (sbmlModel == null) {
			// TODO: signal that some errors happened before this point
			return OurModel.makeEmptyModel();
		}

		Set<Vertex> vertices = connector.readReactions(sbmlModel
				.getListOfReactions());

		return OurModel.makeModel(vertices);
	}

}
