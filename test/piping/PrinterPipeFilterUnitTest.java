package piping;

import java.io.File;

import junit.framework.Assert;
import model.OurModel;

import org.junit.Test;

import dotInterface.DotExportableUnitTest;
import dotInterface.DotFileUtilHandler;

public class PrinterPipeFilterUnitTest {

	@Test
	public void creationOfPrinterPipeFilter() {
		String string = "unimportantName";
		PipeFilter printerPipeFilter = PipeFilter.MakePrinterPipeFilter(string);

		Assert.assertNotNull(printerPipeFilter);
		Assert.assertTrue(printerPipeFilter
				.isYourTagEquals(AvailableFilters.Printer));
	}

	@Test
	public void applyPrinterPipeFilter() {
		String pipeName = "tarjanSingleLevelTestPrinterPipeFilterOutput";

		PipeFilter printerPipeFilter = PipeFilter
				.MakePrinterPipeFilter(pipeName);

		File workingFile = new File(
				DotFileUtilHandler
						.getAbsoluteFileNameInTestOutputFolder(printerPipeFilter
								.formatPhaseIdentifier()));

		if (workingFile.exists()) {
			try {
				workingFile.delete();
			} catch (Exception e) {
				Assert.fail("Impossible to prepare the context for run this test.");
			}
		}

		final OurModel tarjanModel = DotExportableUnitTest.makeTarjanModel();

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
