package tarjan;

import java.util.Map;

import model.Vertex;
import model.ExploreStatedWrapperVertex;

public interface DfsExplorer {

	void exploreVertex(Vertex v);

	void acceptDfsEventsListener(DfsEventsListener listener);

	void searchStarted(Map<Vertex, ExploreStatedWrapperVertex> map);

	void searchCompleted(Map<Vertex, ExploreStatedWrapperVertex> map);

	void newVertexExplored(Vertex explorationCauseVertex, Vertex vertex);

	void alreadyKnownVertex(Vertex vertex);
}
