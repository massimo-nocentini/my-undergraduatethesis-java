package tarjan;

import java.util.Map;
import java.util.Set;

import model.Vertex;

public interface DfsEventsListener {

	void searchCompleted(Map<Vertex, VertexDfsMetadata> map);

	void postVisit(Vertex v);

	void preVisit(Vertex v);

	void searchStarted(Map<Vertex, VertexDfsMetadata> map);

	void newVertexExplored(Vertex explorationCauseVertex, Vertex vertex);

	void fillCollectedVertices(Set<Vertex> vertices);

	void alreadyKnownVertex(Vertex vertex);

}
