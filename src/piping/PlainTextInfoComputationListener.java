package piping;

import java.util.HashMap;
import java.util.Map;

import model.OurModel;
import model.PlainTextStatsComponents;
import model.VertexStatsRecorder;

public class PlainTextInfoComputationListener implements
		PipeFilterComputationListener {

	Map<PlainTextStatsComponents, Integer> map;

	PlainTextInfoComputationListener() {
		map = new HashMap<PlainTextStatsComponents, Integer>();
	}

	@Override
	public void onComputationStarted(String pipelineIdentifier,
			OurModel inputModel) {

	}

	@Override
	public void onSkippedComputation(PipeFilter pipeFilter,
			String collectedPhaseInformation, OurModel inputModel) {

	}

	public boolean isPlainTextInfoEquals(
			Map<PlainTextStatsComponents, Integer> otherMap) {
		return map.equals(otherMap);
	}

	@Override
	public void onComputationFinished(PipeFilter pipeFilter,
			Object pipeFilterCustomOutput) {

		if (pipeFilterCustomOutput instanceof VertexStatsRecorder) {
			VertexStatsRecorder vertexStatsRecorder = (VertexStatsRecorder) pipeFilterCustomOutput;

			map.putAll(vertexStatsRecorder.collectVotes());
		}
	}
}
