package piping;

import java.util.Arrays;
import java.util.TreeSet;

import junit.framework.Assert;
import model.OurModel;
import model.Vertex;

import org.junit.Test;

import dotInterface.DotFileUtilHandler;

public class OnePipingLevelUnitTest {

	@Test
	public void buildOnePipeLevel() {

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

		secondPrinterPipeFilter.workOn(OurModel.makePapadimitriouModel())
				.apply(pipelineName, OurModel.makePapadimitriouModel());

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

		secondPrinterPipeFilter.workOn(simpleModel).apply(pipelineName,
				simpleModel);

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

		secondPrinterPipeFilter.workOn(bartonellaModel).apply(pipelineName,
				bartonellaModel);

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
	public void checkWorkOnWithOnePipeLevel() {

		PipeFilter printerPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		PipeFilter dfsPipeFilter = PipeFilterFactory.MakeDfsPipeFilter();

		dfsPipeFilter = dfsPipeFilter.pipeAfter(printerPipeFilter);

		OurModel tarjanModel = OurModel.makeTarjanModel();
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

		PipeFilter printerPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		PipeFilter dfsPipeFilter = PipeFilterFactory.MakeDfsPipeFilter();

		dfsPipeFilter = dfsPipeFilter.pipeAfter(printerPipeFilter);

		OurModel tarjanModel = OurModel.makeTarjanModel();

		OurModel outputModel = dfsPipeFilter.workOn(tarjanModel).apply(
				pipelineName, tarjanModel);

		Assert.assertTrue(printerPipeFilter.isYourWorkingOurModelNotNull());
		Assert.assertTrue(printerPipeFilter
				.isYourWorkingOurModelEquals(tarjanModel));

		Assert.assertTrue(dfsPipeFilter.isYourWorkingOurModelNotNull());
		// this assert assure very much things!
		Assert.assertTrue(dfsPipeFilter
				.isYourWorkingOurModelEquals(tarjanModel));
	}

	// @Test
	// public void checkAcceptListenerWithOnePipeLevel() {
	//
	// PipeFilter printerPipeFilter = PipeFilterFactory
	// .MakePrinterPipeFilter();
	//
	// PipeFilter dfsPipeFilter = PipeFilterFactory.MakeDfsPipeFilter();
	//
	// dfsPipeFilter = dfsPipeFilter.pipeAfter(printerPipeFilter);
	//
	// Assert.assertTrue(printerPipeFilter.isYourListenerNotNull());
	// Assert.assertTrue(printerPipeFilter.isYourListenerEquals(dfsPipeFilter));
	// Assert.assertFalse(dfsPipeFilter.isYourListenerNotNull());
	// }

	@Test
	public void checkingDfsPipeFilterWorkWithOnePipeLevel() {
		String pipelineName = "checkingDfsPipeFilterWorkWithOnePipeLevel";

		PipeFilter printerPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		PipeFilter dfsPipeFilter = PipeFilterFactory.MakeDfsPipeFilter();

		final OurModel tarjanModel = OurModel.makeTarjanModel();

		// run the computation
		OurModel outputModel = dfsPipeFilter.pipeAfter(printerPipeFilter)
				.workOn(tarjanModel).apply(pipelineName, tarjanModel);

		Assert.assertNotSame(outputModel, tarjanModel);
		Assert.assertFalse(outputModel.equals(tarjanModel));
	}
}
