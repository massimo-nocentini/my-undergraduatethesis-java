package model;

import model.Vertex.DoAction;

import org.sbml.jsbml.Species;

// TODO: check if exists some unit tests for the dfs and Tarjan factory methods.
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

		return makeSimpleVertex(species_id, "", compartment_id);
	}

	private static class ContainerForClosures {
		private Object contained_instance;

		@SuppressWarnings("unchecked")
		public <T> T getTypedInstance() {

			T result = null;
			try {
				result = (T) this.contained_instance;
			} catch (Exception e) {
			}

			return result;
		}

		public void setContained_instance(Object contained_instance) {
			this.contained_instance = contained_instance;
		}
	}

	public static SimpleVertex makeSimpleVertex(Vertex vertex) {

		final ContainerForClosures container = new ContainerForClosures();

		vertex.doWithVertexType(new DoAction<VertexType>() {

			@Override
			public void apply(VertexType item, String species_id,
					String species_name, String compartment_id) {

				container.setContained_instance(makeSimpleVertex(species_id,
						species_name, compartment_id));
			}
		});

		return container.<SimpleVertex> getTypedInstance();
	}

	public static DfsWrapperVertex makeDfsWrapperVertex(Vertex vertex) {
		return new DfsWrapperVertex(vertex);
	}

	public static TarjanWrapperVertex makeTarjanWrapperVertex(Vertex vertex) {
		return new TarjanWrapperVertex(vertex);
	}

	public static ConnectedComponentWrapperVertex makeConnectedComponentWrapperVertex() {
		return new ConnectedComponentWrapperVertex();
	}

	public static SimpleVertex makeSimpleVertex(String species_id,
			String species_name, String compartment_id) {

		return SimpleVertex
				.makeVertex(species_id, species_name, compartment_id);
	}

}
