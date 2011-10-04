package model;

import tarjan.DfsExplorer;

public class ExploreStatedWrapperVertex {
	private boolean explored;
	private final Vertex vertex;

	public ExploreStatedWrapperVertex(Vertex v) {
		this.vertex = v;
		this.explored = false;
	}

	public ExploreStatedWrapperVertex ifNotExplored(DfsExplorer vertexExplorer) {
		return ifNotExplored(vertexExplorer, null);
	}

	public ExploreStatedWrapperVertex ifNotExplored(DfsExplorer vertexExplorer,
			Vertex explorationCauseVertex) {

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
