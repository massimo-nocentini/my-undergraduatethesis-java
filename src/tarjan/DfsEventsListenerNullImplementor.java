package tarjan;

import java.util.Map;
import java.util.Set;

import model.Vertex;
import model.ExploreStatedWrapperVertex;

public class DfsEventsListenerNullImplementor implements DfsEventsListener {

	@Override
	public void postVisit(Vertex v) {
	}

	@Override
	public void preVisit(Vertex v) {
	}

	@Override
	public void searchCompleted(Map<Vertex, ExploreStatedWrapperVertex> map) {
	}

	@Override
	public void searchStarted(Map<Vertex, ExploreStatedWrapperVertex> map) {
	}

	@Override
	public void newVertexExplored(Vertex explorationCauseVertex, Vertex vertex) {
	}

	@Override
	public void fillCollectedVertices(Set<Vertex> vertices) {
	}

	@Override
	public void alreadyKnownVertex(Vertex vertex) {
	}

}
