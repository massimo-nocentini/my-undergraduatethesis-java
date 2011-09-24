package piping;

import model.OurModel;

public class ByPassPipeFilter extends PipeFilter {

	@Override
	public boolean isYourTagEquals(AvailableFilters other) {
		return AvailableFilters.ByPass.equals(other);
	}

	@Override
	protected OurModel doYourComputationOn(String pipelineName,
			OurModel inputModel,
			PipeFilterComputationListener computationListener) {

		computationListener.onSkippedComputation(this,
				collectPhaseInformation(), inputModel);

		return inputModel;
	}

}
