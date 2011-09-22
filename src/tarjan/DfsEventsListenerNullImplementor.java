package tarjan;

import java.util.Map;

import model.Vertex;

public class DfsEventsListenerNullImplementor implements DfsEventsListener {

	@Override
	public void postVisit(Vertex v) {
	}

	@Override
	public void preVisit(Vertex v) {
	}

	@Override
	public void searchCompleted(Map<Vertex, VertexDfsMetadata> map) {

	}

	@Override
	public void searchStarted(Map<Vertex, VertexDfsMetadata> map) {

	}

	@Override
	public void newVertexExplored(Vertex explorationCauseVertex, Vertex vertex) {

	}

}
