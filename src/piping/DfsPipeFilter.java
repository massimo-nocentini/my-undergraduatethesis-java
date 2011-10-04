package piping;

import java.util.Set;
import java.util.TreeSet;

import model.OurModel;
import model.Vertex;
import tarjan.DfsEventsListener;
import tarjan.DfsEventsListenerTreeBuilder;

public class DfsPipeFilter extends PipeFilter {

	@Override
	public boolean isYourTagEquals(AvailableFilters other) {
		return AvailableFilters.DFS.equals(other);
	}

	@Override
	protected OurModel doYourComputationOn(String pipelineName,
			OurModel inputModel,
			PipeFilterComputationListener computationListener) {

		DfsEventsListener dfsEventListener = new DfsEventsListenerTreeBuilder();

		inputModel.runDepthFirstSearch(dfsEventListener);

		Set<Vertex> vertices = new TreeSet<Vertex>();
		dfsEventListener.fillCollectedVertices(vertices);

		return OurModel.makeOurModelFrom(vertices);
	}
}
