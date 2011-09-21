package tarjan;

import java.util.Map;

import model.Vertex;

public class DfsEventsListenerNullImplementor implements DfsEventsListener {

	@Override
	public void searchStarted() {
	}

	@Override
	public void postVisit(Vertex v) {
	}

	@Override
	public void preVisit(Vertex v) {
	}

	@Override
	public void searchCompleted(Map<Vertex, VertexDfsMetadata> map) {
		// TODO Auto-generated method stub

	}

}
