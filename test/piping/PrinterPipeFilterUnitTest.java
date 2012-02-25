package piping;

import java.io.File;

import junit.framework.Assert;
import model.IgnoreVertexTypeDecorationVertex;
import model.ModelsRepository;
import model.OurModel;
import model.OurModel.VertexTransformer;
import model.Vertex;

import org.junit.Test;

import dotInterface.DotFileUtilHandler;
import dotInterface.NullObjectLineDecorator;

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
	public void applicationOfPrinterPipeFilterOnTarjanModel_printing_without_vertex_type_color() {
		String pipeName = "applicationOfPrinterPipeFilterOnTarjanModel_printing_without_vertex_type_color";

		PipeFilter printerPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		final OurModel tarjanModel = ModelsRepository.makeTarjanModel();

		OurModel without_vertex_type = OurModel
				.makeOurModelFromExistingModelTransformingVertices(tarjanModel,
						new VertexTransformer() {

							@Override
							public Vertex transform(Vertex vertex) {

								return new IgnoreVertexTypeDecorationVertex(
										vertex, new NullObjectLineDecorator());
							}
						});

		printerPipeFilter.apply(pipeName, without_vertex_type);

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

		final OurModel papadimitriouModel = ModelsRepository
				.makePapadimitriouModel();

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
