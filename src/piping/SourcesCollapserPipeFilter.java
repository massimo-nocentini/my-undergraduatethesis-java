package piping;

import model.OurModel;
import model.Vertex;

public class SourcesCollapserPipeFilter extends PipeFilter {

	@Override
	public boolean isYourTagEquals(AvailableFilters other) {
		return AvailableFilters.SourcesCollapser.equals(other);
	}

	@Override
	protected OurModel doYourComputationOn(String pipelineName,
			OurModel inputModel,
			PipeFilterComputationListener computationListener) {

		OurModel cloned = inputModel.cloneYourself();

		@SuppressWarnings("unused")
		Vertex collapsedSource = cloned.collapseSources();

		return cloned;
	}

}
