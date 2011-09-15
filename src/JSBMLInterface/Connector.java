package JSBMLInterface;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import model.Vertex;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SpeciesReference;

public class Connector {

	private Connector() {
	}

	public static Connector makeConnector() {
		return new Connector();
	}

	public Set<Vertex> readReaction(Reaction reaction,
			VertexHandlingWithSourceListener listener) {

		Map<Vertex, Vertex> knownVertices = new HashMap<Vertex, Vertex>();

		Set<Vertex> reactants = this.convertToVertexSet(
				reaction.getListOfReactants(), knownVertices,
				new FromReactorDriver(listener));

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

	public class FromReactorDriver extends AbstractVertexHandlingListener {

		public FromReactorDriver(VertexHandlingWithSourceListener listener) {
			super(listener);
		}

		@Override
		public void onVertexHandled(Vertex vertex) {
			this.getListener().reactantVertexHandled(vertex);
		}
	}

	public class FromProductDriver extends AbstractVertexHandlingListener {

		public FromProductDriver(VertexHandlingWithSourceListener listener) {
			super(listener);
		}

		@Override
		public void onVertexHandled(Vertex vertex) {
			this.getListener().productVertexHandled(vertex);
		}
	}

}
