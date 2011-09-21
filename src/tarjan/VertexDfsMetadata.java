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
		if (explored == false) {
			explored = true;
			vertexExplorer.exploreVertex(vertex);
		}
		return this;
	}
}
