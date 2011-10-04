package model;

import tarjan.DfsEventsListener;
import tarjan.DfsExplorer;

public class ExploreStatedWrapperVertex extends WrapperVertex {

	public interface ExploreStateWrapperVertexMapper {

		ExploreStatedWrapperVertex map(Vertex vertex);
	}

	// TODO: use the factory to retrieve object of this class.
	public ExploreStatedWrapperVertex(Vertex wrappingVertex) {
		super(wrappingVertex);
		this.explored = false;
	}

	private boolean explored;

	public ExploreStatedWrapperVertex ifNotExplored(
			DfsEventsListener dfsEventsListener,
			ExploreStateWrapperVertexMapper mapper) {

		return ifNotExplored(dfsEventsListener, null, mapper);
	}

	public ExploreStatedWrapperVertex ifNotExplored(
			final DfsEventsListener dfsEventsListener,
			Vertex explorationCauseVertex,
			final ExploreStateWrapperVertexMapper mapper) {

		final Vertex vertex = getWrappedVertex();

		if (isExplored() == false) {

			toggle();

			if (explorationCauseVertex != null) {
				dfsEventsListener.newVertexExplored(explorationCauseVertex,
						vertex);
			}

			dfsEventsListener.preVisit(vertex);

			vertex.doOnNeighbors(new VertexLogicApplierWithNeighborhoodRelation() {

				@Override
				public void apply(Vertex parent, Vertex neighbour) {

					if ((parent == vertex) == false) {
						throw new RuntimeException("Semantic error");
					}

					mapper.map(neighbour).ifNotExplored(dfsEventsListener,
							parent, mapper);
				}
			});

			dfsEventsListener.postVisit(vertex);
		} else {
			dfsEventsListener.alreadyKnownVertex(vertex);
		}

		return this;
	}

	public ExploreStatedWrapperVertex ifNotExplored(DfsExplorer vertexExplorer) {
		return ifNotExplored(vertexExplorer, null);
	}

	public ExploreStatedWrapperVertex ifNotExplored(DfsExplorer vertexExplorer,
			Vertex explorationCauseVertex) {

		Vertex vertex = getWrappedVertex();

		if (isExplored() == false) {

			toggle();

			if (explorationCauseVertex != null) {
				vertexExplorer
						.newVertexExplored(explorationCauseVertex, vertex);
			}

			vertexExplorer.exploreVertex(vertex);
		} else {
			vertexExplorer.alreadyKnownVertex(vertex);
		}

		return this;
	}

	public boolean isExplored() {
		return explored;
	}

	public ExploreStatedWrapperVertex toggle() {
		if (isExplored() == false) {
			explored = !explored;
		}
		return this;
	}
}
