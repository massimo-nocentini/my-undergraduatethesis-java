package piping;

import junit.framework.Assert;
import model.OurModel;
import dotInterface.DotFileUtilHandler;

public class SourcesCollapserPipeFilterUnitTest {

	// @Test
	public void realBartonellaQuintanaToulouse_Parsing_CollapsingSources() {
		String pipelineName = "realBartonellaQuintanaToulouse_Parsing_CollapsingSources";

		OurModel bartonellaModel = OurModel.makeOurModelFrom(DotFileUtilHandler
				.getSbmlExampleModelsFolder().concat(
						"BartonellaQuintanaToulouse.xml"));

		// PipeFilter printerPipeFilter = PipeFilterFactory
		// .MakePrinterPipeFilter();

		PipeFilter sourcesCollapserPipeFilter = PipeFilterFactory
				.MakeSourcesCollapserPipeFilter();

		PipeFilter secondPrinterPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		// sourcesCollapserPipeFilter.pipeAfter(printerPipeFilter);

		secondPrinterPipeFilter.pipeAfter(sourcesCollapserPipeFilter);

		secondPrinterPipeFilter.apply(pipelineName, bartonellaModel);

		Assert.assertTrue(secondPrinterPipeFilter.isYourLevelOfWrapping(1));
	}
}
