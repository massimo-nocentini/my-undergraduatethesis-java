package piping;

import junit.framework.Assert;
import model.OurModel;

import org.junit.Test;

import dotInterface.DotExportableUnitTest;

/*
 * In this test methods I use the PrinterPipeFilter but
 * without loss of generality. I choose it because is the
 * simplest pipe filter that I've right now (at the moment I'm typing)
 * and it is sure that it works.
 */
public class BasicPipingUnitTest {

	@Test
	public void passingInitialOurModelToPrinterPipeFilter() {
		String string = "unimportantName";
		PipeFilter printerPipeFilter = PipeFilter.MakePrinterPipeFilter(string);

		OurModel model = OurModel.makeEmptyModel();
		printerPipeFilter = printerPipeFilter.workOn(model);

		Assert.assertNotNull(printerPipeFilter);
		Assert.assertTrue(printerPipeFilter.isYourWorkingOurModelNotNull());
		Assert.assertTrue(printerPipeFilter.isYourWorkingOurModelEquals(model));
	}

	@Test
	public void zeroLevelOfWrappingPipeFilter() {
		String string = "unimportantName";
		PipeFilter printerPipeFilter = PipeFilter.MakePrinterPipeFilter(string);

		Assert.assertTrue(printerPipeFilter.isYourLevelOfWrapping(0));
	}

	@Test
	public void checkOverallPipelineNameOfPipeFilter() {
		String pipelineName = "pipelineName";
		PipeFilter printerPipeFilter = PipeFilter
				.MakePrinterPipeFilter(pipelineName);

		Assert.assertTrue(printerPipeFilter
				.isYourPipelineNameEquals(pipelineName));
	}

	@Test
	public void checkPipelinePhaseIdentifier() {
		String pipelineName = "pipelineName";

		PipeFilter printerPipeFilter = PipeFilter
				.MakePrinterPipeFilter(pipelineName);

		Assert.assertTrue(printerPipeFilter.isYourPhaseIdentifier(pipelineName
				.concat(printerPipeFilter.fillWithPhaseInformation())));
	}

	@Test
	public void acceptListenerPrinterPipeFilter() {
		String string = "unimportantName";
		PipeFilter printerPipeFilter = PipeFilter.MakePrinterPipeFilter(string);

		PipeFilterOutputListener listener = new NullPipeFilterOutputListener();
		OurModel tarjanModel = DotExportableUnitTest.MakeTarjanModel();
		printerPipeFilter = printerPipeFilter.acceptOutputListener(listener)
				.workOn(tarjanModel);

		Assert.assertNotNull(printerPipeFilter);
		Assert.assertTrue(printerPipeFilter.isYourListenerNotNull());
		Assert.assertTrue(printerPipeFilter.isYourListenerEquals(listener));
		// the following looks like a regression test.
		Assert.assertTrue(printerPipeFilter.isYourWorkingOurModelNotNull());
		Assert.assertTrue(printerPipeFilter
				.isYourWorkingOurModelEquals(tarjanModel));

	}

}
