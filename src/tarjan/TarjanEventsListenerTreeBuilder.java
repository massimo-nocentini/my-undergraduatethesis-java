package tarjan;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import model.TarjanWrapperVertex;
import model.Vertex;
import model.VertexFactory;

public class TarjanEventsListenerTreeBuilder implements DfsEventsListener {

	private final Map<Vertex, TarjanWrapperVertex> verticesMap;
	private int clock;

	private final Stack<Vertex> representatives;
	private final Stack<Vertex> partials;

	public TarjanEventsListenerTreeBuilder() {
		verticesMap = new HashMap<Vertex, TarjanWrapperVertex>();
		clock = 1;
		representatives = new Stack<Vertex>();
		partials = new Stack<Vertex>();
	}

	@Override
	public void searchCompleted(Map<Vertex, VertexDfsMetadata> map) {
	}

	@Override
	public void postVisit(Vertex u) {
		if (u.equals(representativeOnTopOfTheStack()) == false) {
			return;
		}

		Vertex connectedComponent = null;

		while (partials.size() > 0) {

			Vertex partialVertexOnTopOfTheStack = partials.pop();

			retrieveWrapperOf(partialVertexOnTopOfTheStack)
					.joinConnectedComponent(connectedComponent);

			if (u.equals(partialVertexOnTopOfTheStack) == true) {
				break;
			}
		}

		representatives.pop();
	}

	private Vertex representativeOnTopOfTheStack() {
		return representatives.lastElement();
	}

	@Override
	public void preVisit(Vertex u) {
		retrieveWrapperOf(u).exploredAt(clock);
		clock = clock + 1;

		partials.push(u);
		representatives.push(u);
	}

	protected TarjanWrapperVertex retrieveWrapperOf(Vertex key) {
		return verticesMap.get(key);
	}

	@Override
	// TODO: duplicated code with the dfs tree listener. Did copy and paste of
	// this code, how we can refactor it?
	public void searchStarted(Map<Vertex, VertexDfsMetadata> map) {

		for (Vertex vertex : map.keySet()) {

			verticesMap.put(vertex,
					VertexFactory.makeTarjanWrapperVertex(vertex));
		}

	}

	@Override
	public void newVertexExplored(Vertex explorationCauseVertex, Vertex vertex) {
	}

	@Override
	public void fillCollectedVertices(Set<Vertex> vertices) {
		// TODO Auto-generated method stub

	}

	@Override
	public void alreadyKnownVertex(Vertex vertex) {

		if (retrieveWrapperOf(vertex).haveYouParentComponent()) {
			return;
		}

		TarjanWrapperVertex tarjanWrapperVertex = retrieveWrapperOf(vertex);
		while (tarjanWrapperVertex
				.hadYouBeenDiscoveredFirstThan(retrieveWrapperOf(representativeOnTopOfTheStack()))) {

			Vertex pop = representatives.pop();
			// Assert.assertEquals(pop, actual);
		}

	}
}
