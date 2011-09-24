package piping;

import java.util.Set;
import java.util.TreeSet;

import model.OurModel;
import model.Vertex;
import tarjan.DfsEventsListenerTreeBuilder;
import tarjan.DfsExplorer;
import tarjan.DfsExplorerDefaultImplementor;

public class DfsPipeFilter extends PipeFilter {

	@Override
	public boolean isYourTagEquals(AvailableFilters other) {
		return AvailableFilters.DFS.equals(other);
	}

	@Override
	protected OurModel doYourComputationOn(String pipelineName,
			OurModel inputModel,
			PipeFilterComputationListener computationListener) {

		DfsEventsListenerTreeBuilder dfsEventListener = new DfsEventsListenerTreeBuilder();

		DfsExplorer dfsExplorer = DfsExplorerDefaultImplementor.make();

		dfsExplorer.acceptDfsEventsListener(dfsEventListener);

		inputModel.runDepthFirstSearch(dfsExplorer);

		Set<Vertex> vertices = new TreeSet<Vertex>();
		dfsEventListener.fillCollectedVertices(vertices);

		return OurModel.makeOurModelFrom(vertices);
	}
}
