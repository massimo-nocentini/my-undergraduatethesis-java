package piping;

import junit.framework.Assert;
import model.OurModel;

import org.junit.Test;

import dotInterface.DotExportableUnitTest;

public class OnePipingLevelUnitTest {

	@Test
	public void creationOfPrinterPipeFilter() {
		String pipelineName = "pipelineName";
		PipeFilter dfsPipeFilter = PipeFilter.MakeDfsPipeFilter(pipelineName);

		Assert.assertNotNull(dfsPipeFilter);
		Assert.assertTrue(dfsPipeFilter.isYourTagEquals(AvailableFilters.DFS));
	}

	@Test
	public void buildOnePipeLevel() {
		String pipelineName = "pipelineName";

		PipeFilter printerPipeFilter = PipeFilter
				.MakePrinterPipeFilter(pipelineName);

		PipeFilter dfsPipeFilter = PipeFilter.MakeDfsPipeFilter(pipelineName);

		PipeFilter dfsPipeFilterAfterPipe = dfsPipeFilter
				.pipeAfter(printerPipeFilter);

		Assert.assertSame(dfsPipeFilterAfterPipe, dfsPipeFilter);
		Assert.assertTrue(dfsPipeFilter
				.isYourWrappedPipeFilterEquals(printerPipeFilter));

		Assert.assertFalse(printerPipeFilter
				.isYourWrappedPipeFilterEquals(dfsPipeFilter));
		Assert.assertFalse(printerPipeFilter.isYourWrappedPipeFilterNotNull());

		Assert.assertTrue(dfsPipeFilter.isYourWrappedPipeFilterNotNull());
		Assert.assertFalse(printerPipeFilter.isYourWrappedPipeFilterNotNull());

		Assert.assertTrue(dfsPipeFilter.isYourLevelOfWrapping(1));
		Assert.assertTrue(printerPipeFilter.isYourLevelOfWrapping(0));

	}

	@Test
	public void checkWorkOnWithOnePipeLevel() {
		String pipelineName = "pipelineName";

		PipeFilter printerPipeFilter = PipeFilter
				.MakePrinterPipeFilter(pipelineName);

		PipeFilter dfsPipeFilter = PipeFilter.MakeDfsPipeFilter(pipelineName);

		dfsPipeFilter = dfsPipeFilter.pipeAfter(printerPipeFilter);

		OurModel tarjanModel = DotExportableUnitTest.MakeTarjanModel();
		dfsPipeFilter.workOn(tarjanModel);

		Assert.assertTrue(printerPipeFilter.isYourWorkingOurModelNotNull());
		Assert.assertTrue(printerPipeFilter
				.isYourWorkingOurModelEquals(tarjanModel));

		// until I call apply method and run the pipe backwards, all the filters
		// except the head of the pipe haven't any model to work on.
		Assert.assertFalse(dfsPipeFilter.isYourWorkingOurModelNotNull());
	}

	@Test
	public void checkPresenceOfModelInWrapperFilterAfterApply() {
		String pipelineName = "checkPresenceOfModelInWrapperFilterAfterApply";

		PipeFilter printerPipeFilter = PipeFilter
				.MakePrinterPipeFilter(pipelineName);

		PipeFilter dfsPipeFilter = PipeFilter.MakeDfsPipeFilter(pipelineName);

		dfsPipeFilter = dfsPipeFilter.pipeAfter(printerPipeFilter);

		OurModel tarjanModel = DotExportableUnitTest.MakeTarjanModel();
		dfsPipeFilter.workOn(tarjanModel).apply();

		Assert.assertTrue(printerPipeFilter.isYourWorkingOurModelNotNull());
		Assert.assertTrue(printerPipeFilter
				.isYourWorkingOurModelEquals(tarjanModel));

		Assert.assertTrue(dfsPipeFilter.isYourWorkingOurModelNotNull());
		// this assert assure very much things!
		Assert.assertTrue(dfsPipeFilter
				.isYourWorkingOurModelEquals(tarjanModel));
	}

	@Test
	public void checkAcceptListenerWithOnePipeLevel() {
		String pipelineName = "pipelineName";

		PipeFilter printerPipeFilter = PipeFilter
				.MakePrinterPipeFilter(pipelineName);

		PipeFilter dfsPipeFilter = PipeFilter.MakeDfsPipeFilter(pipelineName);

		dfsPipeFilter = dfsPipeFilter.pipeAfter(printerPipeFilter);

		Assert.assertTrue(printerPipeFilter.isYourListenerNotNull());
		Assert.assertTrue(printerPipeFilter.isYourListenerEquals(dfsPipeFilter));
		Assert.assertFalse(dfsPipeFilter.isYourListenerNotNull());
	}

	@Test
	public void checkingDfsPipeFilterWorkWithOnePipeLevel() {
		String pipelineName = "checkingDfsPipeFilterWorkWithOnePipeLevel";

		PipeFilter printerPipeFilter = PipeFilter
				.MakePrinterPipeFilter(pipelineName);

		PipeFilter dfsPipeFilter = PipeFilter.MakeDfsPipeFilter(pipelineName);

		final OurModel tarjanModel = DotExportableUnitTest.MakeTarjanModel();
		final StringBuilder listenerSignalRecorder = new StringBuilder();
		final String listenerActuallySignaledFlag = "signaled";

		dfsPipeFilter.acceptOutputListener(new PipeFilterOutputListener() {

			@Override
			public void onOutputProduced(OurModel ourModel) {
				Assert.assertNotSame(ourModel, tarjanModel);
				Assert.assertFalse(ourModel.equals(tarjanModel));

				// adding some information on the string builder in order
				// to notify that this listener is actually signaled
				listenerSignalRecorder.append(listenerActuallySignaledFlag);
			}
		});

		// run the computation
		dfsPipeFilter.pipeAfter(printerPipeFilter).workOn(tarjanModel).apply();

		// check if the signal really happened on the listener
		Assert.assertEquals(listenerActuallySignaledFlag,
				listenerSignalRecorder.toString());
	}
}
