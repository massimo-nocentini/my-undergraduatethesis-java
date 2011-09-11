package JSBMLInterface;

import java.util.HashMap;
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

	public Set<Vertex> readReaction(Reaction reaction) {
		Map<String, Vertex> result = new HashMap<String, Vertex>();

		for (SpeciesReference sRef : reaction.getListOfReactants()) {

			String domainObjectId = sRef.getSpeciesInstance().getId();

			if (result.containsKey(domainObjectId) == false) {
				Vertex domainVertex = Vertex.makeVertex(domainObjectId);
				result.put(domainObjectId, domainVertex);
			}

			Vertex domainVertex = result.get(domainObjectId);

			this.explodeSingleSpecies(domainVertex,
					reaction.getListOfProducts(), result);
		}

		return null;
	}

	private void explodeSingleSpecies(Vertex domainVertex,
			ListOf<SpeciesReference> range, Map<String, Vertex> result) {
		for (SpeciesReference rangeObject : range) {
			String rangeObjectId = rangeObject.getSpeciesInstance().getId();

			if (result.containsKey(rangeObjectId) == false) {
				Vertex reachedVertex = Vertex.makeVertex(rangeObjectId);
				result.put(rangeObjectId, reachedVertex);
			}

			Vertex reachedVertex = result.get(rangeObjectId);
			domainVertex.addNeighbour(reachedVertex);

		}
	}
}
