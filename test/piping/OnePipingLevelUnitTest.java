package piping;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.Assert;
import model.ModelsRepository;
import model.OurModel;
import model.Vertex;
import model.VertexFactory;

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
				ModelsRepository.makePapadimitriouModel());

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
	public void OnePipingLevelUnitTest_Printer_Tarjan_PrinterPipe_Crescenzi() {
		String pipelineName = "OnePipingLevelUnitTest_Printer_DFS_PrinterPipe_Crescenzi";

		PipeFilter printerPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		PipeFilter tarjanPipeFilter = PipeFilterFactory.MakeTarjanPipeFilter();

		PipeFilter secondPrinterPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		secondPrinterPipeFilter.pipeAfter(tarjanPipeFilter
				.pipeAfter(printerPipeFilter));

		secondPrinterPipeFilter.apply(pipelineName,
				ModelsRepository.makeCrescenziModel());

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
	public void OnePipingLevelUnitTest_Printer_Tarjan_PrinterPipe_Presentation() {
		String pipelineName = "OnePipingLevelUnitTest_Printer_DFS_PrinterPipe_Presentation";

		Set<Vertex> vertices = new HashSet<Vertex>();
		Vertex a = VertexFactory.makeSimpleVertex();
		Vertex b = VertexFactory.makeSimpleVertex();
		Vertex c = VertexFactory.makeSimpleVertex();
		Vertex d = VertexFactory.makeSimpleVertex();
		Vertex e = VertexFactory.makeSimpleVertex();
		Vertex f = VertexFactory.makeSimpleVertex();
		Vertex g = VertexFactory.makeSimpleVertex();
		Vertex h = VertexFactory.makeSimpleVertex();
		Vertex i = VertexFactory.makeSimpleVertex();
		Vertex l = VertexFactory.makeSimpleVertex();

		// source strongly connected component
		a.addNeighbour(b);
		b.addNeighbour(a);

		b.addNeighbour(c);

		c.addNeighbour(e).addNeighbour(d);

		e.addNeighbour(f).addNeighbour(g);
		f.addNeighbour(e);

		g.addNeighbour(h).addNeighbour(l);
		h.addNeighbour(i);
		i.addNeighbour(g);

		d.addNeighbour(l);

		vertices.add(a);
		vertices.add(b);
		vertices.add(c);
		vertices.add(d);
		vertices.add(e);
		vertices.add(f);
		vertices.add(g);
		vertices.add(h);
		vertices.add(i);
		vertices.add(l);

		PipeFilter printerPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		PipeFilter tarjanPipeFilter = PipeFilterFactory.MakeTarjanPipeFilter();

		PipeFilter secondPrinterPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		secondPrinterPipeFilter.pipeAfter(tarjanPipeFilter
				.pipeAfter(printerPipeFilter));

		secondPrinterPipeFilter.apply(pipelineName,
				OurModel.makeOurModelFrom(vertices, "graph-for-presentation"));

		Assert.assertTrue(secondPrinterPipeFilter.isYourLevelOfWrapping(2));

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
				ModelsRepository.makePapadimitriouModel());

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

		final Vertex v = VertexFactory.makeSimpleVertex();
		final Vertex v2 = VertexFactory.makeSimpleVertex();
		final Vertex v3 = VertexFactory.makeSimpleVertex();

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
	public void OnePipingLevelUnitTest_Tarjan_RealBartonellaQuintanaToulouse() {
		String pipelineName = "OnePipingLevelUnitTest_Tarjan_RealBartonellaQuintanaToulouse";

		OurModel bartonellaModel = OurModel.makeOurModelFrom(DotFileUtilHandler
				.getSbmlExampleModelsFolder().concat(
						"BartonellaQuintanaToulouse.xml"));

		PipeFilter tarjanPipeFilter = PipeFilterFactory.MakeTarjanPipeFilter();

		PipeFilter secondPrinterPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		secondPrinterPipeFilter.pipeAfter(tarjanPipeFilter);

		secondPrinterPipeFilter.apply(pipelineName, bartonellaModel);

		Assert.assertTrue(secondPrinterPipeFilter.isYourLevelOfWrapping(1));
	}

	@Test
	public void OnePipingLevelUnitTest_Tarjan_RealBartonellaQuintanaToulouse_CollapsedSources() {
		String pipelineName = "OnePipingLevelUnitTest_Tarjan_RealBartonellaQuintanaToulouse_CollapsedSources";

		OurModel bartonellaModel = OurModel.makeOurModelFrom(DotFileUtilHandler
				.getSbmlExampleModelsFolder().concat(
						"BartonellaQuintanaToulouse.xml"));

		PipeFilter sourceCollapserPipeFilter = PipeFilterFactory
				.MakeSourcesCollapserPipeFilter();

		PipeFilter tarjanPipeFilter = PipeFilterFactory.MakeTarjanPipeFilter();

		PipeFilter secondPrinterPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		tarjanPipeFilter.pipeAfter(sourceCollapserPipeFilter);

		secondPrinterPipeFilter.pipeAfter(tarjanPipeFilter);

		secondPrinterPipeFilter.apply(pipelineName, bartonellaModel);

		Assert.assertTrue(secondPrinterPipeFilter.isYourLevelOfWrapping(2));
	}

	@Test
	public void OnePipingLevelUnitTest_Tarjan_RealEscherichiaColiK12() {
		String pipelineName = "OnePipingLevelUnitTest_Tarjan_RealEscherichiaColiK12";

		OurModel escherichiaColiK12Model = OurModel
				.makeOurModelFrom(DotFileUtilHandler
						.getSbmlExampleModelsFolder().concat(
								"EscherichiaColiK12.xml"));

		PipeFilter tarjanPipeFilter = PipeFilterFactory.MakeTarjanPipeFilter();

		PipeFilter secondPrinterPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		secondPrinterPipeFilter.pipeAfter(tarjanPipeFilter);

		secondPrinterPipeFilter.apply(pipelineName, escherichiaColiK12Model);

		Assert.assertTrue(secondPrinterPipeFilter.isYourLevelOfWrapping(1));
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
	public void OnePipingLevelUnitTest_DFS_CrescenziModel() {
		String pipelineName = "OnePipingLevelUnitTest_DFS_CrescenziModel";

		OurModel crescenziModel = ModelsRepository.makeCrescenziModel();

		PipeFilter dfsPipeFilter = PipeFilterFactory.MakeDfsPipeFilter();

		PipeFilter secondPrinterPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		secondPrinterPipeFilter.pipeAfter(dfsPipeFilter);

		secondPrinterPipeFilter.apply(pipelineName, crescenziModel);

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

		OurModel tarjanModel = ModelsRepository.makeTarjanModel();

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

					@Override
					public void onComputationFinished(PipeFilter pipeFilter,
							Object pipeFilterCustomOutput) {

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

		OurModel tarjanModel = ModelsRepository.makeTarjanModel();

		// run the computation
		OurModel outputModel = dfsPipeFilter.pipeAfter(printerPipeFilter)
				.apply(pipelineName, tarjanModel);

		Assert.assertNotSame(outputModel, tarjanModel);
		Assert.assertFalse(outputModel.equals(tarjanModel));
	}
}
