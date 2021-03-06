package piping;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import model.OurModel;
import model.PlainTextStatsComponents;
import model.VertexStatsRecorder;
import dotInterface.DotFileUtilHandler;

public class PlainTextInfoComputationListener implements
		PipeFilterComputationListener {

	private final Map<PipeFilter, VertexStatsRecorder> map;

	public PlainTextInfoComputationListener() {
		map = new HashMap<PipeFilter, VertexStatsRecorder>();
	}

	@Override
	public void onComputationStarted(String pipelineIdentifier,
			OurModel inputModel) {

	}

	@Override
	public void onSkippedComputation(PipeFilter pipeFilter,
			String collectedPhaseInformation, OurModel inputModel) {

	}

	public boolean isPlainTextInfoEquals(PipeFilter pipeFilter,
			Map<PlainTextStatsComponents, Integer> otherMap) {

		if (map.containsKey(pipeFilter) == false) {

			return false;
		}

		return map.get(pipeFilter).isSimpleVerticesVotesEquals(otherMap);
	}

	@Override
	public void onComputationFinished(PipeFilter pipeFilter,
			Object pipeFilterCustomOutput) {

		if (pipeFilterCustomOutput instanceof VertexStatsRecorder) {

			VertexStatsRecorder vertexStatsRecorder = (VertexStatsRecorder) pipeFilterCustomOutput;

			if (map.containsKey(pipeFilter) == false) {
				map.put(pipeFilter, vertexStatsRecorder);
			} else {
				map.get(pipeFilter).add(vertexStatsRecorder);
			}
		}
	}

	public void writeOn(Writer writer) {

		for (Entry<PipeFilter, VertexStatsRecorder> entry : this.map.entrySet()) {
			try {
				writer.append(entry.getKey().collectPhaseInformation()
						.concat(DotFileUtilHandler.getNewLineSeparator()));

				entry.getValue().writeOn(writer);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public boolean arePlainTextInfoConsistent() {

		boolean result = true;

		for (Entry<PipeFilter, VertexStatsRecorder> entry : map.entrySet()) {
			result = result & entry.getValue().areConsistent();
		}

		return result;
	}

	public PlainTextInfoComputationListener average(Integer count) {

		PlainTextInfoComputationListener computationListener = new PlainTextInfoComputationListener();

		for (Entry<PipeFilter, VertexStatsRecorder> entry : this.map.entrySet()) {
			computationListener.map.put(entry.getKey(), entry.getValue()
					.average(count));
		}

		return computationListener;

	}
}
