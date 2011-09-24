package piping;

import model.OurModel;

public class PipeFilterComputationListenerNullObject implements
		PipeFilterComputationListener {

	@Override
	public void onSkippedComputation(PipeFilter pipeFilter,
			String collectedPhaseInformation, OurModel inputModel) {
	}

	@Override
	public void onComputationStarted(String pipelineIdentifier,
			OurModel inputModel) {

	}

}
