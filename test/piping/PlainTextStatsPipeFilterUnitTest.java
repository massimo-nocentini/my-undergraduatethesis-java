package piping;

import java.util.HashMap;
import java.util.Map;

import model.ModelsRepository;
import model.PlainTextStatsComponents;

import org.junit.Assert;
import org.junit.Test;

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

		map.put(PlainTextStatsComponents.NumberOfVertices, 12);
		map.put(PlainTextStatsComponents.NumberOfEdges, 26);
		map.put(PlainTextStatsComponents.NumberOfSources, 0);
		map.put(PlainTextStatsComponents.NumberOfSinks, 1);
		map.put(PlainTextStatsComponents.NumberOfWhites, 11);

		Assert.assertTrue(plainTextInfoComputationListener
				.isPlainTextInfoEquals(map));
	}
}
