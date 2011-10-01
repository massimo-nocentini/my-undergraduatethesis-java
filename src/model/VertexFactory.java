package model;

import org.sbml.jsbml.Species;

public class VertexFactory {

	public static SimpleVertex makeSimpleVertex() {

		int id = SimpleVertex.VertexIntegerEnumerator.enumerateNewVertex();

		SimpleVertex createdVertex = VertexFactory.makeSimpleVertex(
				SimpleVertex.DummySpeciesId.concat(String.valueOf(id)),
				SimpleVertex.DummyCompartmentId);

		return createdVertex;
	}

	public static SimpleVertex makeSimpleVertex(Species species) {
		return VertexFactory.makeSimpleVertex(species.getId(),
				species.getCompartment());
	}

	public static SimpleVertex makeSimpleVertex(String species_id,
			String compartment_id) {
		return SimpleVertex.makeVertex(species_id, compartment_id);
	}

	public static SimpleVertex makeSimpleVertex(Vertex vertex) {

		return makeSimpleVertex(vertex.asSimpleVertex().species_id,
				vertex.asSimpleVertex().compartment_id);
	}

	public static DfsWrapperVertex makeDfsWrapperVertex(Vertex vertex) {
		return new DfsWrapperVertex(vertex);
	}

}
