package tarjan;

import java.util.Map;

import model.Vertex;

public interface DfsEventsListener {

	void searchCompleted(Map<Vertex, VertexDfsMetadata> map);

	void postVisit(Vertex v);

	void preVisit(Vertex v);

	void searchStarted(Map<Vertex, VertexDfsMetadata> map);

	void newVertexExplored(Vertex explorationCauseVertex, Vertex vertex);

}
