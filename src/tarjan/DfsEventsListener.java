package tarjan;

import java.util.Map;

import model.Vertex;

public interface DfsEventsListener {

	void searchStarted();

	void searchCompleted(Map<Vertex, VertexDfsMetadata> map);

	void postVisit(Vertex v);

	void preVisit(Vertex v);

}
