package piping;

import java.io.File;

import junit.framework.Assert;
import model.ModelsRepository;
import model.OurModel;

import org.junit.Test;

import dotInterface.DotFileUtilHandler;

public class PrinterPipeFilterUnitTest {

	@Test
	public void applicationOfPrinterPipeFilterOnTarjanModel() {
		String pipeName = "applicationOfPrinterPipeFilterOnTarjanModel";

		PipeFilter printerPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		File workingFile = DotFileUtilHandler
				.makeDotOutputFile(printerPipeFilter
						.formatPhaseIdentifier(pipeName));

		if (workingFile.exists()) {
			try {
				workingFile.delete();
			} catch (Exception e) {
				Assert.fail("Impossible to prepare the context for run this test.");
			}
		}

		final OurModel tarjanModel = ModelsRepository.makeTarjanModel();

		OurModel outputModel = printerPipeFilter.apply(pipeName, tarjanModel);

		Assert.assertSame(tarjanModel, outputModel);

		Assert.assertTrue(
				"The filter application seem to be completed successfully "
						+ "but the output file wasn't created",
				workingFile.exists());

		Assert.assertTrue("The file created is empty", workingFile.length() > 0);
	}

	@Test
	public void applicationOfPrinterPipeFilterOnPapadimitriouModel() {
		String pipeName = "applicationOfPrinterPipeFilterOnPapadimitriouModel";

		PipeFilter printerPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		File workingFile = DotFileUtilHandler
				.makeDotOutputFile(printerPipeFilter
						.formatPhaseIdentifier(pipeName));

		if (workingFile.exists()) {
			try {
				workingFile.delete();
			} catch (Exception e) {
				Assert.fail("Impossible to prepare the context for run this test.");
			}
		}

		final OurModel papadimitriouModel = ModelsRepository.makePapadimitriouModel();

		OurModel outputModel = printerPipeFilter.apply(pipeName,
				papadimitriouModel);

		Assert.assertSame(papadimitriouModel, outputModel);

		Assert.assertTrue(
				"The filter application seem to be completed successfully "
						+ "but the output file wasn't created",
				workingFile.exists());

		Assert.assertTrue("The file created is empty", workingFile.length() > 0);
	}

	@Test
	public void applicationOfPrinterPipeFilterOnCrescenziModel() {
		String pipeName = "applicationOfPrinterPipeFilterOnCrescenziModel";

		PipeFilter printerPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		File workingFile = DotFileUtilHandler
				.makeDotOutputFile(printerPipeFilter
						.formatPhaseIdentifier(pipeName));

		if (workingFile.exists()) {
			try {
				workingFile.delete();
			} catch (Exception e) {
				Assert.fail("Impossible to prepare the context for run this test.");
			}
		}

		final OurModel crescenziModel = ModelsRepository.makeCrescenziModel();

		OurModel outputModel = printerPipeFilter
				.apply(pipeName, crescenziModel);

		Assert.assertSame(crescenziModel, outputModel);

		Assert.assertTrue(
				"The filter application seem to be completed successfully "
						+ "but the output file wasn't created",
				workingFile.exists());

		Assert.assertTrue("The file created is empty", workingFile.length() > 0);
	}
}
