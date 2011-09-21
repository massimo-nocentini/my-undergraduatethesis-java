package tarjan;

import java.util.Map;

import model.INeighbourApplier;
import model.Vertex;

public class DfsExplorerDefaultImplementor implements DfsExplorer {

	private DfsEventsListener dfsEventsListener;
	private Map<Vertex, VertexDfsMetadata> exploredVertexMetadatasMap;

	private DfsExplorerDefaultImplementor() {
	}

	@Override
	public void exploreVertex(Vertex v) {
		dfsEventsListener.preVisit(v);

		INeighbourApplier dfsRecursivelyNeighborsApplier = new INeighbourApplier() {

			@Override
			public void apply(Vertex vertex) {

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

		dfsEventsListener.searchStarted();
	}

	@Override
	public void searchCompleted() {
		dfsEventsListener.searchCompleted();
	}

	public static DfsExplorer Make() {
		return new DfsExplorerDefaultImplementor();
	}

}
