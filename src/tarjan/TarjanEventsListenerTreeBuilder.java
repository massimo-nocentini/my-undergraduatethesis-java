package tarjan;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import model.ConnectedComponentWrapperVertex;
import model.TarjanWrapperVertex;
import model.Vertex;
import model.VertexFactory;
import model.VertexLogicApplier;

public class TarjanEventsListenerTreeBuilder implements DfsEventsListener {

	private final Map<Vertex, TarjanWrapperVertex> verticesMap;
	private final Map<Vertex, ConnectedComponentWrapperVertex> componentsMembershipMap;
	private int clock;

	private final Stack<Vertex> representatives;
	private final Stack<Vertex> partials;

	public TarjanEventsListenerTreeBuilder() {
		verticesMap = new HashMap<Vertex, TarjanWrapperVertex>();
		clock = 1;
		representatives = new Stack<Vertex>();
		partials = new Stack<Vertex>();
		componentsMembershipMap = new HashMap<Vertex, ConnectedComponentWrapperVertex>();
	}

	@Override
	public void searchCompleted(Map<Vertex, VertexDfsMetadata> map) {
	}

	@Override
	public void postVisit(Vertex u) {
		if (u.equals(representativeOnTopOfTheStack()) == false) {
			return;
		}

		ConnectedComponentWrapperVertex connectedComponent = VertexFactory
				.makeConnectedComponentWrapperVertex();

		while (partials.size() > 0) {

			Vertex partialVertexOnTopOfTheStack = partials.pop();

			retrieveWrapperOf(partialVertexOnTopOfTheStack)
					.joinConnectedComponent(connectedComponent);

			componentsMembershipMap.put(partialVertexOnTopOfTheStack,
					connectedComponent);

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
		fillCollectedVertices(vertices, verticesMap, componentsMembershipMap);
	}

	public void fillCollectedVertices(Set<Vertex> collectingVertices,
			final Map<Vertex, TarjanWrapperVertex> map,
			Map<Vertex, ConnectedComponentWrapperVertex> componentsMembershipMap) {

		for (final Entry<Vertex, TarjanWrapperVertex> pair : map.entrySet()) {

			pair.getKey().doOnNeighbors(new VertexLogicApplier() {

				@Override
				public void apply(Vertex vertex) {
					pair.getValue().bridgeConnectedComponentOf(map.get(vertex));
				}
			});

			collectingVertices.add(componentsMembershipMap.get(pair.getKey()));
		}

	}

	@Override
	public void alreadyKnownVertex(Vertex vertex) {
		TarjanWrapperVertex v = retrieveWrapperOf(vertex);

		if (v.areYouMemberInSomeConnectedComponent()) {
			return;
		}

		while (v.hadYouBeenDiscoveredFirstThan(retrieveWrapperOf(representativeOnTopOfTheStack()))) {

			Vertex pop = representatives.pop();
			// Assert.assertEquals(pop, actual);
		}

	}
}
