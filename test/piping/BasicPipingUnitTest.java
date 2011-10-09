package piping;

import junit.framework.Assert;
import model.OurModel;

import org.junit.Test;

import util.CallbackSignalRecorder;

public class BasicPipingUnitTest {

	@Test
	public void passingInitialOurModelToByPassPipeFilter() {

		PipeFilter printerPipeFilter = PipeFilterFactory.MakeByPassPipeFilter();

		final OurModel inputModel = OurModel.makeEmptyModel();
		final CallbackSignalRecorder callbackSignalRecorder = new CallbackSignalRecorder();

		@SuppressWarnings("unused")
		OurModel outputModel = printerPipeFilter.applyWithListener(
				"pipelineName", inputModel,
				new PipeFilterComputationListener() {

					@Override
					public void onSkippedComputation(PipeFilter pipeFilter,
							String collectedPhaseInformation,
							OurModel inputModel) {

					}

					@Override
					public void onComputationStarted(String pipelineIdentifier,
							OurModel innerInputModel) {

						callbackSignalRecorder.signal();
						Assert.assertEquals(inputModel, innerInputModel);
					}

					@Override
					public void onComputationFinished(PipeFilter pipeFilter,
							Object pipeFilterCustomOutput) {

					}
				});

		Assert.assertTrue(callbackSignalRecorder.isSignaled());
	}

	@Test
	public void zeroLevelOfWrappingPipeFilter() {
		PipeFilter printerPipeFilter = PipeFilterFactory.MakeByPassPipeFilter();

		Assert.assertTrue(printerPipeFilter.isYourLevelOfWrapping(0));
	}

	@Test
	public void checkPipelinePhaseIdentifier() {
		final String pipelineName = "pipelineName";

		final PipeFilter printerPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		final CallbackSignalRecorder callbackSignalRecorder = new CallbackSignalRecorder();

		PipeFilterComputationListener listener = new PipeFilterComputationListener() {

			@Override
			public void onComputationStarted(String pipelineIdentifier,
					OurModel inputModel) {

				callbackSignalRecorder.signal();

				Assert.assertEquals(
						pipelineName.concat("-").concat(
								printerPipeFilter.collectPhaseInformation()),
						pipelineIdentifier);

			}

			@Override
			public void onSkippedComputation(PipeFilter pipeFilter,
					String collectedPhaseInformation, OurModel inputModel) {

				Assert.fail("The computation must continue, no reason to stop it.");
			}

			@Override
			public void onComputationFinished(PipeFilter pipeFilter,
					Object pipeFilterCustomOutput) {

			}

		};

		printerPipeFilter.applyWithListener(pipelineName,
				OurModel.makeEmptyModel(), listener);

		Assert.assertTrue(callbackSignalRecorder.isSignaled());
	}
}
