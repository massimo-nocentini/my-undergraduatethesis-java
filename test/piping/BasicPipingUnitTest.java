package piping;

import java.io.File;

import junit.framework.Assert;
import model.OurModel;

import org.junit.Test;

import dotInterface.DotExportableUnitTest;
import dotInterface.DotFileUtilHandler;

public class BasicPipingUnitTest {

	@Test
	public void creationOfPrinterPipeFilter() {
		String string = "unimportantName";
		PipeFilter printerPipeFilter = PipeFilter.MakePrinterPipeFilter(string);

		Assert.assertNotNull(printerPipeFilter);
		Assert.assertTrue(printerPipeFilter.areYouAn(AvailableFilters.Printer));
	}

	@Test
	public void passingInitialOurModelToPrinterPipeFilter() {
		String string = "unimportantName";
		PipeFilter printerPipeFilter = PipeFilter.MakePrinterPipeFilter(string);

		printerPipeFilter = printerPipeFilter.workOn(OurModel.makeEmptyModel());

		Assert.assertNotNull(printerPipeFilter);
		Assert.assertTrue(printerPipeFilter.haveYouAnOurModel());
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

		Assert.assertTrue(printerPipeFilter.isYourPipelineNameEquals(pipelineName));
	}

	@Test
	public void acceptListenerPrinterPipeFilter() {
		String string = "unimportantName";
		PipeFilter printerPipeFilter = PipeFilter.MakePrinterPipeFilter(string);

		PipeFilterOutputListener listener = new NullPipeFilterOutputListener();
		printerPipeFilter = printerPipeFilter.acceptOutputListener(listener)
				.workOn(DotExportableUnitTest.MakeTarjanModel());

		Assert.assertNotNull(printerPipeFilter);
		Assert.assertTrue(printerPipeFilter.someoneIsListeningYou());
		// the following looks like a regression test.
		Assert.assertTrue(printerPipeFilter.haveYouAnOurModel());
	}

	@Test
	public void applyPrinterPipeFilter() {
		String string = "tarjanSingleLevelTestPrinterPipeFilterOutput";
		File workingFile = new File(
				DotFileUtilHandler
						.getAbsoluteFileNameInTestOutputFolder(string));

		if (workingFile.exists()) {
			try {
				workingFile.delete();
			} catch (Exception e) {
				Assert.fail("Impossible to prepare the context for test run.");
			}
		}

		PipeFilter printerPipeFilter = PipeFilter.MakePrinterPipeFilter(string);

		final OurModel tarjanModel = DotExportableUnitTest.MakeTarjanModel();

		PipeFilterOutputListener listener = new PipeFilterOutputListener() {

			@Override
			public void onOutputProduced(OurModel ourModel) {
				// first is checked this assert
				Assert.assertSame(tarjanModel, ourModel);
			}
		};

		printerPipeFilter = printerPipeFilter.acceptOutputListener(listener)
				.workOn(tarjanModel).apply();

		// second is checked this assert
		Assert.assertNotNull(printerPipeFilter);

		Assert.assertTrue(
				"The filter application seem to be completed successfully "
						+ "but the output file wasn't created",
				workingFile.exists());

		Assert.assertTrue("The file created is empty", workingFile.length() > 0);
	}
}
