package tarjan;

import model.Vertex;

public interface DfsEventsListener {

	void searchStarted();

	void searchCompleted();

	void postVisit(Vertex v);

	void preVisit(Vertex v);

}
