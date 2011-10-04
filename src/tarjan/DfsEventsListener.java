package tarjan;

import java.util.Map;
import java.util.Set;

import model.Vertex;
import model.ExploreStatedWrapperVertex;

public interface DfsEventsListener {

	void searchCompleted(Map<Vertex, ExploreStatedWrapperVertex> map);

	void postVisit(Vertex v);

	void preVisit(Vertex v);

	void searchStarted(Map<Vertex, ExploreStatedWrapperVertex> map);

	void newVertexExplored(Vertex explorationCauseVertex, Vertex vertex);

	void fillCollectedVertices(Set<Vertex> vertices);

	void alreadyKnownVertex(Vertex vertex);

}
