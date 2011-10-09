package piping;

import model.OurModel;
import model.Vertex;
import model.VertexLogicApplier;
import model.VertexStatsRecorder;

public class PlainTextStatsPipeFilter extends PipeFilter {

	@Override
	public boolean isYourTagEquals(AvailableFilters other) {
		return AvailableFilters.PlainTextStats.equals(other);
	}

	@Override
	protected OurModel doYourComputationOn(String pipelineName,
			OurModel inputModel,
			PipeFilterComputationListener computationListener) {

		final VertexStatsRecorder vertexStatsRecorder = new VertexStatsRecorder();

		inputModel.doOnVertices(new VertexLogicApplier() {

			@Override
			public void apply(Vertex vertex) {

				vertex.publishYourStatsOn(vertexStatsRecorder);

			}
		});

		computationListener.onComputationFinished(this, vertexStatsRecorder);

		// like a byPass pipe filter
		return inputModel;
	}

}
