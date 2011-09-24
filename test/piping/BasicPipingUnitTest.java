package piping;

import junit.framework.Assert;
import model.OurModel;

import org.junit.Test;

import util.CallbackSignalRecorder;

/*
 * In this test methods I use the PrinterPipeFilter but
 * without loss of generality. I choose it because is the
 * simplest pipe filter that I've right now (at the moment I'm typing)
 * and it is sure that it works.
 */
public class BasicPipingUnitTest {

	@Test
	public void passingInitialOurModelToPrinterPipeFilter() {
		PipeFilter printerPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		OurModel model = OurModel.makeEmptyModel();
		printerPipeFilter = printerPipeFilter.workOn(model);

		Assert.assertTrue(printerPipeFilter.isYourWorkingOurModelNotNull());
		Assert.assertTrue(printerPipeFilter.isYourWorkingOurModelEquals(model));
	}

	@Test
	public void zeroLevelOfWrappingPipeFilter() {
		PipeFilter printerPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

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
			public void computationStartedWithPipelineIdentifier(
					String pipelineIdentifier) {

				callbackSignalRecorder.signal();

				Assert.assertEquals(
						pipelineName.concat("-").concat(
								printerPipeFilter.collectPhaseInformation()),
						pipelineIdentifier);
			}

		};

		printerPipeFilter.applyWithListener(pipelineName,
				OurModel.makeEmptyModel(), listener);

		Assert.assertTrue(callbackSignalRecorder.isSignaled());
	}

	// @Test
	// public void acceptListenerPrinterPipeFilter() {
	//
	// PipeFilter printerPipeFilter = PipeFilterFactory
	// .MakePrinterPipeFilter();
	//
	// PipeFilterOutputListener listener = new NullPipeFilterOutputListener();
	//
	// OurModel tarjanModel = OurModel.makeTarjanModel();
	// printerPipeFilter = printerPipeFilter.acceptOutputListener(listener)
	// .workOn(tarjanModel);
	//
	// Assert.assertNotNull(printerPipeFilter);
	// Assert.assertTrue(printerPipeFilter.isYourListenerNotNull());
	// Assert.assertTrue(printerPipeFilter.isYourListenerEquals(listener));
	// // the following looks like a regression test.
	// Assert.assertTrue(printerPipeFilter.isYourWorkingOurModelNotNull());
	// Assert.assertTrue(printerPipeFilter
	// .isYourWorkingOurModelEquals(tarjanModel));
	//
	// }

}
