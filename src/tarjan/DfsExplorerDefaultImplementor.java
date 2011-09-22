package tarjan;

import java.util.Map;

import model.Vertex;
import model.VertexLogicApplier;

public class DfsExplorerDefaultImplementor implements DfsExplorer {

	private DfsEventsListener dfsEventsListener;
	private Map<Vertex, VertexDfsMetadata> exploredVertexMetadatasMap;

	private DfsExplorerDefaultImplementor() {
	}

	@Override
	public void exploreVertex(Vertex v) {
		dfsEventsListener.preVisit(v);

		final DfsExplorer thisExplorer = this;

		VertexLogicApplier dfsRecursivelyNeighborsApplier = new VertexLogicApplier() {

			@Override
			public void apply(Vertex vertex) {
				exploredVertexMetadatasMap.get(vertex).ifNotExplored(
						thisExplorer);
			}
		};

		v.doOnNeighbors(dfsRecursivelyNeighborsApplier);

		dfsEventsListener.postVisit(v);
	}

	@Override
	public void acceptDfsEventsListener(DfsEventsListener dfsEventsListener) {
		this.dfsEventsListener = dfsEventsListener;
	}

	@Override
	public void searchStarted(
			Map<Vertex, VertexDfsMetadata> exploredVertexMetadatasMap) {

		this.exploredVertexMetadatasMap = exploredVertexMetadatasMap;

		dfsEventsListener.searchStarted(exploredVertexMetadatasMap);
	}

	public static DfsExplorer Make() {
		return new DfsExplorerDefaultImplementor();
	}

	@Override
	public void searchCompleted(Map<Vertex, VertexDfsMetadata> map) {
		dfsEventsListener.searchCompleted(map);
	}

}
