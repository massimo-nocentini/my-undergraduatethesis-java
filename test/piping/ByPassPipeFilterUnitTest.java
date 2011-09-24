package piping;

import junit.framework.Assert;
import model.OurModel;

import org.junit.Test;

import util.CallbackSignalRecorder;

public class ByPassPipeFilterUnitTest {

	@Test
	public void checkByPassingPipeFilter() {
		PipeFilter byPassPipeFilter = PipeFilterFactory.MakeByPassPipeFilter();

		final CallbackSignalRecorder computationStartedRecorder = new CallbackSignalRecorder();
		final CallbackSignalRecorder computationSkippedRecorder = new CallbackSignalRecorder();

		OurModel inputModel = OurModel.makeEmptyModel();

		OurModel outputModel = byPassPipeFilter.applyWithListener(
				"pipelineName", inputModel,
				new PipeFilterComputationListener() {

					@Override
					public void onSkippedComputation(PipeFilter pipeFilter,
							String collectedPhaseInformation,
							OurModel inputModel) {

						computationSkippedRecorder.signal();
					}

					@Override
					public void onComputationStarted(String pipelineIdentifier,
							OurModel inputModel) {

						computationStartedRecorder.signal();
					}
				});

		Assert.assertTrue(computationStartedRecorder.isSignaled());
		Assert.assertTrue(computationSkippedRecorder.isSignaled());

		Assert.assertSame(inputModel, outputModel);
	}
}
