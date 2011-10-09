package piping;

import model.OurModel;

public interface PipeFilterComputationListener {

	void onComputationStarted(String pipelineIdentifier, OurModel inputModel);

	void onSkippedComputation(PipeFilter pipeFilter,
			String collectedPhaseInformation, OurModel inputModel);

	void onComputationFinished(PipeFilter pipeFilter,
			Object pipeFilterCustomOutput);

}
