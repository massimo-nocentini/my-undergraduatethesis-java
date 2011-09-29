package tarjan;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import model.SimpleVertex;
import model.Vertex;

public class DfsEventsListenerTreeBuilder implements DfsEventsListener {

	private Map<Vertex, DataContainer> verticesMap;

	private int clock;

	public DfsEventsListenerTreeBuilder() {
		verticesMap = new HashMap<Vertex, DfsEventsListenerTreeBuilder.DataContainer>();
		clock = 1;
	}

	boolean isVertexClockInterval(Vertex vertex, int previsitClock,
			int postVisitClock) {
		DataContainer dataContainer = verticesMap.get(vertex);
		return dataContainer.isYourPreVisitClock(previsitClock)
				&& dataContainer.isYourPostVisitClock(postVisitClock);
	}

	static class DataContainer {

		private final Vertex wrappedVertex;

		private int preVisitClock;
		private int postVisitClock;

		public DataContainer previsitedAt(int instant) {
			preVisitClock = instant;
			return this;
		}

		public boolean isYourPreVisitClock(int otherClock) {
			return preVisitClock == otherClock;
		}

		public DataContainer postvisitedAt(int other) {
			postVisitClock = other;
			return this;
		}

		public boolean isYourPostVisitClock(int otherClock) {
			return postVisitClock == otherClock;
		}

		public DataContainer(Vertex explorationCauseVertex) {
			this.wrappedVertex = SimpleVertex
					.makeVertex(explorationCauseVertex);
		}

		public void addNeighbour(Vertex vertex) {
			this.wrappedVertex.addNeighbour(vertex);
		}

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
		// TODO Auto-generated method stub

	}

	@Override
	public void searchStarted(
			Map<Vertex, VertexDfsMetadata> exploredVertexMetadatasMap) {

		for (Vertex vertex : exploredVertexMetadatasMap.keySet()) {

			verticesMap.put(vertex, new DataContainer(vertex));
		}
	}

	@Override
	public void newVertexExplored(Vertex explorationCauseVertex, Vertex vertex) {

		verticesMap.get(explorationCauseVertex).addNeighbour(
				verticesMap.get(vertex).wrappedVertex);
	}

	public void fillCollectedVertices(Set<Vertex> vertices) {

		for (Entry<Vertex, DataContainer> entry : verticesMap.entrySet()) {

			Vertex wrappedVertex = entry.getValue().wrappedVertex;
			// if (vertices.contains(wrappedVertex) == false) {
			// vertices.add(wrappedVertex);
			// }
			vertices.add(wrappedVertex);
		}
	}
}
