package piping;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import model.ModelsRepository;
import model.PlainTextStatsComponents;

import org.junit.Assert;
import org.junit.Test;

import dotInterface.DotFileUtilHandler;

public class PlainTextStatsPipeFilterUnitTest {

	@Test
	public void gettingInformationFromPapadimitriouModel() {

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

		Assert.assertTrue(plainTextInfoComputationListener
				.isPlainTextInfoEquals(plainTextStatsPipeFilter, map));
	}

	@Test
	public void gettingInformationFromPapadimitriouModelAfterTarjanPipeFilter() {

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
}
