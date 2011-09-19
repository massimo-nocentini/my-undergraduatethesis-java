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

		printerPipeFilter = printerPipeFilter.workOn(OurModel.makeEmptyModel());

		Assert.assertNotNull(printerPipeFilter);
		Assert.assertTrue(printerPipeFilter.isYourWorkingOurModelNotNull());
	}

	@Test
	public void levelOfWrappingPipeFilter() {
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
	public void acceptListenerPrinterPipeFilter() {
		String string = "unimportantName";
		PipeFilter printerPipeFilter = PipeFilter.MakePrinterPipeFilter(string);

		PipeFilterOutputListener listener = new NullPipeFilterOutputListener();
		printerPipeFilter = printerPipeFilter.acceptOutputListener(listener)
				.workOn(DotExportableUnitTest.MakeTarjanModel());

		Assert.assertNotNull(printerPipeFilter);
		Assert.assertTrue(printerPipeFilter.isYourListenerNotNull());
		// the following looks like a regression test.
		Assert.assertTrue(printerPipeFilter.isYourWorkingOurModelNotNull());
	}

}
