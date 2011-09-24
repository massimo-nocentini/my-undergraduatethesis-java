package piping;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import junit.framework.Assert;
import model.OurModel;
import model.Vertex;

import org.junit.Test;

import util.CallbackSignalRecorder;
import dotInterface.DotFileUtilHandler;

public class OnePipingLevelUnitTest {

	@Test
	public void checkingWrappedPipeFiltersOnPipeOfDepthThree() {

		PipeFilter printerPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		PipeFilter dfsPipeFilter = PipeFilterFactory.MakeDfsPipeFilter();

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
	public void OnePipingLevelUnitTest_Printer_DFS_PrinterPipe_Papadimitriou() {
		String pipelineName = "OnePipingLevelUnitTest_Printer_DFS_PrinterPipe_Papadimitriou";

		PipeFilter printerPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		PipeFilter dfsPipeFilter = PipeFilterFactory.MakeDfsPipeFilter();

		PipeFilter secondPrinterPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		secondPrinterPipeFilter.pipeAfter(dfsPipeFilter
				.pipeAfter(printerPipeFilter));

		secondPrinterPipeFilter.apply(pipelineName,
				OurModel.makePapadimitriouModel());

		Assert.assertTrue(secondPrinterPipeFilter.isYourLevelOfWrapping(2));

		// Assert.assertSame(firstPrinterPipeFilter, dfsPipeFilter);
		// Assert.assertTrue(dfsPipeFilter
		// .isYourWrappedPipeFilterEquals(printerPipeFilter));
		//
		// Assert.assertFalse(printerPipeFilter
		// .isYourWrappedPipeFilterEquals(dfsPipeFilter));
		// Assert.assertFalse(printerPipeFilter.isYourWrappedPipeFilterNotNull());
		//
		// Assert.assertTrue(dfsPipeFilter.isYourWrappedPipeFilterNotNull());
		// Assert.assertFalse(printerPipeFilter.isYourWrappedPipeFilterNotNull());
		//
		// Assert.assertTrue(dfsPipeFilter.isYourLevelOfWrapping(1));
		// Assert.assertTrue(printerPipeFilter.isYourLevelOfWrapping(0));

	}

	@Test
	public void OnePipingLevelUnitTest_Printer_DFS_PrinterPipe_Papadimitriou_SingleRoot() {
		String pipelineName = "OnePipingLevelUnitTest_Printer_DFS_PrinterPipe_Papadimitriou_SingleRoot";

		PipeFilter printerPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		PipeFilter dfsPipeFilter = PipeFilterFactory.MakeDfsPipeFilter();

		PipeFilter secondPrinterPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		secondPrinterPipeFilter.pipeAfter(dfsPipeFilter
				.pipeAfter(printerPipeFilter));

		secondPrinterPipeFilter.apply(pipelineName,
				OurModel.makePapadimitriouModel());

		Assert.assertTrue(secondPrinterPipeFilter.isYourLevelOfWrapping(2));

		// Assert.assertSame(firstPrinterPipeFilter, dfsPipeFilter);
		// Assert.assertTrue(dfsPipeFilter
		// .isYourWrappedPipeFilterEquals(printerPipeFilter));
		//
		// Assert.assertFalse(printerPipeFilter
		// .isYourWrappedPipeFilterEquals(dfsPipeFilter));
		// Assert.assertFalse(printerPipeFilter.isYourWrappedPipeFilterNotNull());
		//
		// Assert.assertTrue(dfsPipeFilter.isYourWrappedPipeFilterNotNull());
		// Assert.assertFalse(printerPipeFilter.isYourWrappedPipeFilterNotNull());
		//
		// Assert.assertTrue(dfsPipeFilter.isYourLevelOfWrapping(1));
		// Assert.assertTrue(printerPipeFilter.isYourLevelOfWrapping(0));

	}

	@Test
	public void OnePipingLevelUnitTest_Printer_DFS_PrinterPipe_Simple() {
		String pipelineName = "OnePipingLevelUnitTest_Printer_DFS_PrinterPipe_Simple";

		final Vertex v = Vertex.makeVertex();
		final Vertex v2 = Vertex.makeVertex();
		final Vertex v3 = Vertex.makeVertex();

		v.addNeighbour(v2);
		v.addNeighbour(v3);

		v3.addNeighbour(v2);

		OurModel simpleModel = OurModel.makeOurModelFrom(new TreeSet<Vertex>(
				Arrays.<Vertex> asList(v, v2, v3)));

		PipeFilter printerPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		PipeFilter dfsPipeFilter = PipeFilterFactory.MakeDfsPipeFilter();

		PipeFilter secondPrinterPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		secondPrinterPipeFilter.pipeAfter(dfsPipeFilter
				.pipeAfter(printerPipeFilter));

		secondPrinterPipeFilter.apply(pipelineName, simpleModel);

		// Assert.assertSame(firstPrinterPipeFilter, dfsPipeFilter);
		// Assert.assertTrue(dfsPipeFilter
		// .isYourWrappedPipeFilterEquals(printerPipeFilter));
		//
		// Assert.assertFalse(printerPipeFilter
		// .isYourWrappedPipeFilterEquals(dfsPipeFilter));
		// Assert.assertFalse(printerPipeFilter.isYourWrappedPipeFilterNotNull());
		//
		// Assert.assertTrue(dfsPipeFilter.isYourWrappedPipeFilterNotNull());
		// Assert.assertFalse(printerPipeFilter.isYourWrappedPipeFilterNotNull());
		//
		// Assert.assertTrue(dfsPipeFilter.isYourLevelOfWrapping(1));
		// Assert.assertTrue(printerPipeFilter.isYourLevelOfWrapping(0));

	}

	@Test
	public void OnePipingLevelUnitTest_DFS_RealBartonellaQuintanaToulouse() {
		String pipelineName = "OnePipingLevelUnitTest_DFS_RealBartonellaQuintanaToulouse";

		OurModel bartonellaModel = OurModel.makeOurModelFrom(DotFileUtilHandler
				.getSbmlExampleModelsFolder().concat(
						"BartonellaQuintanaToulouse.xml"));

		PipeFilter dfsPipeFilter = PipeFilterFactory.MakeDfsPipeFilter();

		PipeFilter secondPrinterPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		secondPrinterPipeFilter.pipeAfter(dfsPipeFilter);

		secondPrinterPipeFilter.apply(pipelineName, bartonellaModel);

		// Assert.assertSame(firstPrinterPipeFilter, dfsPipeFilter);
		// Assert.assertTrue(dfsPipeFilter
		// .isYourWrappedPipeFilterEquals(printerPipeFilter));
		//
		// Assert.assertFalse(printerPipeFilter
		// .isYourWrappedPipeFilterEquals(dfsPipeFilter));
		// Assert.assertFalse(printerPipeFilter.isYourWrappedPipeFilterNotNull());
		//
		// Assert.assertTrue(dfsPipeFilter.isYourWrappedPipeFilterNotNull());
		// Assert.assertFalse(printerPipeFilter.isYourWrappedPipeFilterNotNull());
		//
		// Assert.assertTrue(dfsPipeFilter.isYourLevelOfWrapping(1));
		// Assert.assertTrue(printerPipeFilter.isYourLevelOfWrapping(0));

	}

	@Test
	public void checkManufacturedModelInOneLevelPipe() {

		PipeFilter printerPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		PipeFilter dfsPipeFilter = PipeFilterFactory.MakeDfsPipeFilter();

		dfsPipeFilter = dfsPipeFilter.pipeAfter(printerPipeFilter);

		final CallbackSignalRecorder callbackSignalRecorder = new CallbackSignalRecorder();

		OurModel tarjanModel = OurModel.makeTarjanModel();

		final List<OurModel> actualList = new LinkedList<OurModel>();

		OurModel outputModel = dfsPipeFilter.applyWithListener(
				"checkManufacturedModelInOneLevelPipe", tarjanModel,
				new PipeFilterComputationListener() {

					@Override
					public void onSkippedComputation(PipeFilter pipeFilter,
							String collectedPhaseInformation,
							OurModel inputModel) {

						Assert.fail("The computation must continue, no reason to stop it.");
					}

					@Override
					public void onComputationStarted(String pipelineIdentifier,
							OurModel inputModel) {

						callbackSignalRecorder.signal();
						actualList.add(inputModel);
					}
				});

		Assert.assertEquals(2, actualList.size());
		Assert.assertTrue(callbackSignalRecorder.isCountOfSignals(2));
		Assert.assertFalse(tarjanModel.equals(outputModel));
		Assert.assertSame(tarjanModel, actualList.get(0));
		Assert.assertSame(tarjanModel, actualList.get(1));
	}

	@Test
	public void checkingDfsPipeFilterWorkWithOnePipeLevel() {
		String pipelineName = "checkingDfsPipeFilterWorkWithOnePipeLevel";

		PipeFilter printerPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		PipeFilter dfsPipeFilter = PipeFilterFactory.MakeDfsPipeFilter();

		OurModel tarjanModel = OurModel.makeTarjanModel();

		// run the computation
		OurModel outputModel = dfsPipeFilter.pipeAfter(printerPipeFilter)
				.apply(pipelineName, tarjanModel);

		Assert.assertNotSame(outputModel, tarjanModel);
		Assert.assertFalse(outputModel.equals(tarjanModel));
	}
}
