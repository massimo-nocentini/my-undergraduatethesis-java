package tarjan;

import java.util.Map;

import model.Vertex;
import model.ExploreStatedWrapperVertex;
import model.VertexLogicApplierWithNeighborhoodRelation;

public class DfsExplorerDefaultImplementor implements DfsExplorer {

	private DfsEventsListener dfsEventsListener;
	private Map<Vertex, ExploreStatedWrapperVertex> exploredVertexMetadatasMap;

	private DfsExplorerDefaultImplementor() {
	}

	@Override
	public void exploreVertex(Vertex v) {

		dfsEventsListener.preVisit(v);

		final DfsExplorer thisExplorer = this;

		VertexLogicApplierWithNeighborhoodRelation dfsRecursivelyNeighborsApplier = new VertexLogicApplierWithNeighborhoodRelation() {

			@Override
			public void apply(Vertex parent, Vertex neighbour) {
				exploredVertexMetadatasMap.get(neighbour).ifNotExplored(
						thisExplorer, parent);
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
			Map<Vertex, ExploreStatedWrapperVertex> exploredVertexMetadatasMap) {

		this.exploredVertexMetadatasMap = exploredVertexMetadatasMap;

		dfsEventsListener.searchStarted(exploredVertexMetadatasMap);
	}

	public static DfsExplorer make() {
		return new DfsExplorerDefaultImplementor();
	}

	@Override
	public void searchCompleted(Map<Vertex, ExploreStatedWrapperVertex> map) {
		dfsEventsListener.searchCompleted(map);
	}

	@Override
	public void newVertexExplored(Vertex explorationCauseVertex, Vertex vertex) {
		dfsEventsListener.newVertexExplored(explorationCauseVertex, vertex);

	}

	@Override
	public void alreadyKnownVertex(Vertex vertex) {
		dfsEventsListener.alreadyKnownVertex(vertex);

	}

}
