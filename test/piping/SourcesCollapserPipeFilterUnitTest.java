package piping;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import junit.framework.Assert;
import model.OurModel;

import org.junit.Test;

import dotInterface.DotFileUtilHandler;
import dotInterface.DotFileUtilHandler.DotUtilAction;

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

		String test_method_name = "collapse_sources_of_bartonella_and_apply_plain_statistics";

		plainTextStatsPipeFilter.applyWithListener(test_method_name,
				bartonellaModel, plainTextInfoComputationListener);

		Writer writer;
		try {
			writer = new FileWriter(DotFileUtilHandler
					.dotOutputFolderPathName()
					.concat(test_method_name)
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

	// @Test
	public void collapse_sources_of_all_sbml_example_models_and_print_them() {

		DotUtilAction<File> action = new DotUtilAction<File>() {

			@Override
			public void apply(File element) {

				PipeFilter sourceCollapserPipeFilter = PipeFilterFactory
						.MakeSourcesCollapserPipeFilter();

				PipeFilter printerPipeFilter = PipeFilterFactory
						.MakePrinterPipeFilter();

				printerPipeFilter.pipeAfter(sourceCollapserPipeFilter);

				String test_method_name = "massive_print_with_collapsed_sources-"
						.concat(element.getName().substring(0,
								element.getName().lastIndexOf(".")));

				printerPipeFilter.apply(test_method_name,
						OurModel.makeOurModelFrom(element.getAbsolutePath()));

			}
		};

		DotFileUtilHandler.mapOnAllFilesInFolder(
				DotFileUtilHandler.getSbmlExampleModelsFolder(), action);

		// this test pass any time is executed
	}
}
