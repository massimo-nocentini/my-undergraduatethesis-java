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

	Map<PipeFilter, VertexStatsRecorder> map;

	PlainTextInfoComputationListener() {
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

		return map.get(pipeFilter).collectVotes().equals(otherMap);
	}

	@Override
	public void onComputationFinished(PipeFilter pipeFilter,
			Object pipeFilterCustomOutput) {

		if (pipeFilterCustomOutput instanceof VertexStatsRecorder) {
			VertexStatsRecorder vertexStatsRecorder = (VertexStatsRecorder) pipeFilterCustomOutput;

			map.put(pipeFilter, vertexStatsRecorder);
		}
	}

	public void writeOn(Writer writer) {

		for (Entry<PipeFilter, VertexStatsRecorder> entry : map.entrySet()) {
			try {
				writer.append(entry.getKey().collectPhaseInformation()
						.concat(DotFileUtilHandler.getNewLineSeparator()));

				Map<PlainTextStatsComponents, Integer> collectedStats = entry
						.getValue().collectVotes();

				StringBuilder line = new StringBuilder();
				StringBuilder headerLine = new StringBuilder();
				for (PlainTextStatsComponents component : PlainTextStatsComponents
						.values()) {

					headerLine.append(DotFileUtilHandler.getTabString().concat(
							component.name()));

					if (collectedStats.containsKey(component) == true) {
						line.append(DotFileUtilHandler.getTabString().concat(
								String.valueOf(collectedStats.get(component))));
					}
				}

				writer.append(headerLine.toString().concat(
						DotFileUtilHandler.getNewLineSeparator()));

				writer.append(line.toString().concat(
						DotFileUtilHandler.getNewLineSeparator()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
