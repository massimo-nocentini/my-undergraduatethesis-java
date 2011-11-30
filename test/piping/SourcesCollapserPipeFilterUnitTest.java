package piping;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import junit.framework.Assert;
import model.OurModel;

import org.junit.Test;

import dotInterface.DotFileUtilHandler;

public class SourcesCollapserPipeFilterUnitTest {

	@Test
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

	@Test
	public void collapse_sources_of_bartonella_and_apply_plain_statistics() {

		PipeFilter plainTextStatsPipeFilter = PipeFilterFactory
				.MakePlainTextStatsPipeFilter();

		PipeFilter sourcesCollapserPipeFilter = PipeFilterFactory
				.MakeSourcesCollapserPipeFilter();

		plainTextStatsPipeFilter.pipeAfter(sourcesCollapserPipeFilter);

		PlainTextInfoComputationListener plainTextInfoComputationListener = new PlainTextInfoComputationListener();

		OurModel bartonellaModel = OurModel.makeOurModelFrom(DotFileUtilHandler
				.getSbmlExampleModelsFolder().concat(
						"BartonellaQuintanaToulouse.xml"));

		plainTextStatsPipeFilter.applyWithListener(
				"collapse_sources_of_bartonella_and_apply_plain_statistics",
				bartonellaModel, plainTextInfoComputationListener);

		Writer writer;
		try {
			writer = new FileWriter(DotFileUtilHandler
					.dotOutputFolderPathName()
					.concat(plainTextStatsPipeFilter.collectPhaseInformation())
					.concat(DotFileUtilHandler
							.getPlainTextFilenameExtensionToken()));

			plainTextInfoComputationListener.writeOn(writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// this test pass any time is executed
	}
}
