package tarjan;

import java.util.Map;

import model.Vertex;

public interface DfsExplorer {

	void exploreVertex(Vertex v);

	void acceptDfsEventsListener(DfsEventsListener listener);

	void searchStarted(Map<Vertex, VertexDfsMetadata> map);

	void searchCompleted();
}
