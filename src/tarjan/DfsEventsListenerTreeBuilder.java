package tarjan;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import model.Vertex;

public class DfsEventsListenerTreeBuilder implements DfsEventsListener {

	private Map<Vertex, DataContainer> verticesMap = new HashMap<Vertex, DfsEventsListenerTreeBuilder.DataContainer>();

	private class DataContainer {

		private final Vertex wrappedVertex;

		public DataContainer(Vertex explorationCauseVertex) {
			this.wrappedVertex = Vertex
					.cloneOnlyCharacteristicsFields(explorationCauseVertex);
		}

		public void addNeighbour(Vertex vertex) {
			this.wrappedVertex.addNeighbour(vertex);
		}

	}

	@Override
	public void postVisit(Vertex v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preVisit(Vertex v) {
		// TODO Auto-generated method stub

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
			if (vertices.contains(wrappedVertex) == false) {
				vertices.add(wrappedVertex);
			}
		}
	}
}
