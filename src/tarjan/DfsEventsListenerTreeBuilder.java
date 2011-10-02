package tarjan;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import model.DfsWrapperVertex;
import model.Vertex;
import model.VertexFactory;

public class DfsEventsListenerTreeBuilder implements DfsEventsListener {

	private final Map<Vertex, DfsWrapperVertex> verticesMap;

	private int clock;

	public DfsEventsListenerTreeBuilder() {
		verticesMap = new HashMap<Vertex, DfsWrapperVertex>();
		clock = 1;
	}

	public boolean isVertexClockInterval(Vertex vertex, int previsitClock,
			int postVisitClock) {
		DfsWrapperVertex dataContainer = verticesMap.get(vertex);
		return dataContainer.isYourPreVisitClock(previsitClock)
				&& dataContainer.isYourPostVisitClock(postVisitClock);
	}

	@Override
	public void postVisit(Vertex v) {
		verticesMap.get(v).postvisitedAt(clock);
		clock = clock + 1;
	}

	@Override
	public void preVisit(Vertex v) {
		verticesMap.get(v).previsitedAt(clock);
		clock = clock + 1;
	}

	@Override
	public void searchCompleted(Map<Vertex, VertexDfsMetadata> map) {
	}

	@Override
	public void searchStarted(
			Map<Vertex, VertexDfsMetadata> exploredVertexMetadatasMap) {

		for (Vertex vertex : exploredVertexMetadatasMap.keySet()) {

			verticesMap.put(vertex, VertexFactory.makeDfsWrapperVertex(vertex));
		}
	}

	@Override
	public void newVertexExplored(Vertex explorationCauseVertex, Vertex vertex) {

		verticesMap.get(explorationCauseVertex).addNeighbour(
				verticesMap.get(vertex));
	}

	@Override
	public void fillCollectedVertices(Set<Vertex> vertices) {

		for (Entry<Vertex, DfsWrapperVertex> entry : verticesMap.entrySet()) {

			vertices.add(entry.getValue());
		}
	}

	@Override
	public void alreadyKnownVertex(Vertex vertex) {
	}
}
