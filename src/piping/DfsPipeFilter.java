package piping;

import java.util.HashSet;
import java.util.Set;

import model.OurModel;
import model.Vertex;
import tarjan.DfsEventsListenerTreeBuilder;
import tarjan.DfsExplorer;
import tarjan.DfsExplorerDefaultImplementor;

public class DfsPipeFilter extends PipeFilter {

	public DfsPipeFilter(String pipelineName) {
		super(pipelineName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isYourTagEquals(AvailableFilters other) {
		return AvailableFilters.DFS.equals(other);
	}

	@Override
	protected OurModel doYourComputationOn(OurModel ourModel) {

		DfsEventsListenerTreeBuilder dfsEventListener = new DfsEventsListenerTreeBuilder();

		DfsExplorer dfsExplorer = DfsExplorerDefaultImplementor.make();

		dfsExplorer.acceptDfsEventsListener(dfsEventListener);

		ourModel.runDepthFirstSearch(dfsExplorer);

		Set<Vertex> vertices = new HashSet<Vertex>();
		dfsEventListener.fillCollectedVertices(vertices);

		return OurModel.makeOurModelFrom(vertices);
	}
}
