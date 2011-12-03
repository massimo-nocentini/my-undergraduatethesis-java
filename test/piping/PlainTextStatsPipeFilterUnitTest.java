package piping;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import model.ModelsRepository;
import model.OurModel;
import model.PlainTextStatsComponents;

import org.junit.Assert;
import org.junit.Test;

import dotInterface.DotFileUtilHandler;
import dotInterface.DotFileUtilHandler.DotUtilAction;

public class PlainTextStatsPipeFilterUnitTest {

	@Test
	public void getting_stats_information_for_Papadimitriou_model() {

		PipeFilter plainTextStatsPipeFilter = PipeFilterFactory
				.MakePlainTextStatsPipeFilter();

		PlainTextInfoComputationListener plainTextInfoComputationListener = new PlainTextInfoComputationListener();

		plainTextStatsPipeFilter.applyWithListener("pipelineName",
				ModelsRepository.makePapadimitriouModel(),
				plainTextInfoComputationListener);

		Map<PlainTextStatsComponents, Integer> map = new HashMap<PlainTextStatsComponents, Integer>();

		map.put(PlainTextStatsComponents.NOfVertices, 12);
		map.put(PlainTextStatsComponents.NOfEdges, 26);
		map.put(PlainTextStatsComponents.NOfSources, 0);
		map.put(PlainTextStatsComponents.NOfSinks, 1);
		map.put(PlainTextStatsComponents.NOfWhites, 11);

		String test_method_name = "getting_stats_information_for_Papadimitriou_model";

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

		Assert.assertTrue(plainTextInfoComputationListener
				.isPlainTextInfoEquals(plainTextStatsPipeFilter, map));
	}

	@Test
	public void getting_stats_information_for_Papadimitriou_model_after_Tarjan_filter() {

		PipeFilter tarjanPipeFilter = PipeFilterFactory.MakeTarjanPipeFilter();

		PipeFilter plainTextStatsPipeFilter = PipeFilterFactory
				.MakePlainTextStatsPipeFilter();

		plainTextStatsPipeFilter.pipeAfter(tarjanPipeFilter);

		PlainTextInfoComputationListener plainTextInfoComputationListener = new PlainTextInfoComputationListener();

		plainTextStatsPipeFilter
				.applyWithListener(
						"gettingInformationFromPapadimitriouModelAfterTarjanPipeFilter",
						ModelsRepository.makePapadimitriouModel(),
						plainTextInfoComputationListener);

		String test_method_name = "getting_stats_information_for_Papadimitriou_model_after_Tarjan_filter";

		// Map<PlainTextStatsComponents, Integer> map = new
		// HashMap<PlainTextStatsComponents, Integer>();
		//
		// map.put(PlainTextStatsComponents.NOfVertices, 12);
		// map.put(PlainTextStatsComponents.NOfEdges, 26);
		// map.put(PlainTextStatsComponents.NOfSources, 0);
		// map.put(PlainTextStatsComponents.NOfSinks, 1);
		// map.put(PlainTextStatsComponents.NOfWhites, 11);

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

		// Assert.assertTrue(plainTextInfoComputationListener
		// .isPlainTextInfoEquals(plainTextStatsPipeFilter, map));
	}

	@Test
	public void generating_massive_stats_reports_forall_sbml_models() {

		DotUtilAction<File> action = new DotUtilAction<File>() {

			@Override
			public void apply(File element) {

				PipeFilter firstPlainTextStatsPipeFilter = PipeFilterFactory
						.MakePlainTextStatsPipeFilter();

				PipeFilter tarjanPipeFilter = PipeFilterFactory
						.MakeTarjanPipeFilter();

				PipeFilter secondPlainTextStatsPipeFilter = PipeFilterFactory
						.MakePlainTextStatsPipeFilter();

				tarjanPipeFilter.pipeAfter(firstPlainTextStatsPipeFilter);
				secondPlainTextStatsPipeFilter.pipeAfter(tarjanPipeFilter);

				PlainTextInfoComputationListener plainTextInfoComputationListener = new PlainTextInfoComputationListener();

				String test_method_name = "massive_stats_report-"
						.concat(element.getName().substring(0,
								element.getName().lastIndexOf(".")));

				secondPlainTextStatsPipeFilter.applyWithListener(
						test_method_name,
						OurModel.makeOurModelFrom(element.getAbsolutePath()),
						plainTextInfoComputationListener);

				Writer writer;
				try {
					writer = new FileWriter(DotFileUtilHandler
							.dotOutputFolderPathName()
							.concat(test_method_name)
							.concat("-")
							.concat(secondPlainTextStatsPipeFilter
									.collectPhaseInformation())
							.concat(DotFileUtilHandler
									.getPlainTextFilenameExtensionToken()));

					plainTextInfoComputationListener.writeOn(writer);
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		};

		DotFileUtilHandler.mapOnAllFilesInFolder(
				DotFileUtilHandler.getSbmlExampleModelsFolder(), action);

	}

	@Test
	public void generating_massive_stats_reports_forall_sbml_models_collapsing_sources() {

		DotUtilAction<File> action = new DotUtilAction<File>() {

			@Override
			public void apply(File element) {

				// PipeFilter originalPlainTextStatsPipeFilter =
				// PipeFilterFactory
				// .MakePlainTextStatsPipeFilter();

				PipeFilter sourceCollapserPipeFilter = PipeFilterFactory
						.MakeSourcesCollapserPipeFilter();

				PipeFilter firstPlainTextStatsPipeFilter = PipeFilterFactory
						.MakePlainTextStatsPipeFilter();

				PipeFilter tarjanPipeFilter = PipeFilterFactory
						.MakeTarjanPipeFilter();

				PipeFilter secondPlainTextStatsPipeFilter = PipeFilterFactory
						.MakePlainTextStatsPipeFilter();

				// sourceCollapserPipeFilter
				// .pipeAfter(originalPlainTextStatsPipeFilter);

				firstPlainTextStatsPipeFilter
						.pipeAfter(sourceCollapserPipeFilter);
				tarjanPipeFilter.pipeAfter(firstPlainTextStatsPipeFilter);
				secondPlainTextStatsPipeFilter.pipeAfter(tarjanPipeFilter);

				PlainTextInfoComputationListener plainTextInfoComputationListener = new PlainTextInfoComputationListener();

				String test_method_name = "massive_stats_report_sources_collapsed-"
						.concat(element.getName().substring(0,
								element.getName().lastIndexOf(".")));

				secondPlainTextStatsPipeFilter.applyWithListener(
						test_method_name,
						OurModel.makeOurModelFrom(element.getAbsolutePath()),
						plainTextInfoComputationListener);

				Writer writer;
				try {
					writer = new FileWriter(DotFileUtilHandler
							.dotOutputFolderPathName()
							.concat(test_method_name)
							.concat("-")
							.concat(secondPlainTextStatsPipeFilter
									.collectPhaseInformation())
							.concat(DotFileUtilHandler
									.getPlainTextFilenameExtensionToken()));

					plainTextInfoComputationListener.writeOn(writer);
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		};

		DotFileUtilHandler.mapOnAllFilesInFolder(
				DotFileUtilHandler.getSbmlExampleModelsFolder(), action);

	}
}
