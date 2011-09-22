package tarjan;

import model.Vertex;

public class VertexDfsMetadata {
	private boolean explored;
	private final Vertex vertex;

	public VertexDfsMetadata(Vertex v) {
		this.vertex = v;
		this.explored = false;
	}

	public VertexDfsMetadata ifNotExplored(DfsExplorer vertexExplorer) {
		return ifNotExplored(vertexExplorer, null);
	}

	public VertexDfsMetadata ifNotExplored(DfsExplorer vertexExplorer,
			Vertex explorationCauseVertex) {

		if (explored == false) {
			explored = true;

			if (explorationCauseVertex != null) {
				vertexExplorer
						.newVertexExplored(explorationCauseVertex, vertex);
			}

			vertexExplorer.exploreVertex(vertex);
		}

		return this;
	}

	public boolean isExplored() {
		return explored;
	}
}
