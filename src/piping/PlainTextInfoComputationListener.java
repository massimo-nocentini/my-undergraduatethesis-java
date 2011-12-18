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

			map.put(pipeFilter, vertexStatsRecorder);
		}
	}

	public void writeOn(Writer writer) {

		for (Entry<PipeFilter, VertexStatsRecorder> entry : map.entrySet()) {
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
		// TODO Auto-generated method stub
		return false;
	}
}
