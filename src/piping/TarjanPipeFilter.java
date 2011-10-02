package piping;

import java.util.Set;
import java.util.TreeSet;

import model.OurModel;
import model.Vertex;
import tarjan.DfsEventsListener;
import tarjan.DfsExplorer;
import tarjan.DfsExplorerDefaultImplementor;
import tarjan.TarjanEventsListenerTreeBuilder;

public class TarjanPipeFilter extends PipeFilter {

	@Override
	public boolean isYourTagEquals(AvailableFilters other) {
		return AvailableFilters.Tarjan.equals(other);
	}

	@Override
	protected OurModel doYourComputationOn(String pipelineName,
			OurModel inputModel,
			PipeFilterComputationListener computationListener) {

		DfsEventsListener tarjanEventListener = new TarjanEventsListenerTreeBuilder();

		DfsExplorer dfsExplorer = DfsExplorerDefaultImplementor.make();

		dfsExplorer.acceptDfsEventsListener(tarjanEventListener);

		inputModel.runDepthFirstSearch(dfsExplorer);

		Set<Vertex> vertices = new TreeSet<Vertex>();
		tarjanEventListener.fillCollectedVertices(vertices);

		return OurModel.makeOurModelFrom(vertices);
	}

}
