package JSBMLInterface;

import java.util.HashSet;
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

	public Set<Vertex> readReaction(Reaction reaction) {

		Set<Vertex> result = new HashSet<Vertex>();
		for (SpeciesReference sRef : reaction.getListOfReactants()) {

			Vertex domainVertex = Vertex.makeVertex();

			result.add(domainVertex);

			this.explodeSingleSpecies(domainVertex,
					reaction.getListOfProducts());
		}

		return result;
	}

	private void explodeSingleSpecies(Vertex domainVertex,
			ListOf<SpeciesReference> range) {

		// for (SpeciesReference rangeObject : range) {
		// String rangeObjectId = rangeObject.getSpeciesInstance().getId();
		//
		// if (result.containsKey(rangeObjectId) == false) {
		// Vertex reachedVertex = Vertex.makeVertex(rangeObjectId);
		// result.put(rangeObjectId, reachedVertex);
		// }
		//
		// Vertex reachedVertex = result.get(rangeObjectId);
		// domainVertex.addNeighbour(reachedVertex);
		//
		// }
	}

	public Set<Vertex> convertToVertexSet(
			ListOf<SpeciesReference> listOfSpeciesReference) {

		Set<Vertex> result = new HashSet<Vertex>();

		for (SpeciesReference sRef : listOfSpeciesReference) {

			Vertex domainVertex = Vertex.makeVertex();

			result.add(domainVertex);
		}

		return result;
	}
}
