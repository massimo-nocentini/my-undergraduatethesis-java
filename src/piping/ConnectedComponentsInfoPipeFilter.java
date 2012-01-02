package piping;

import java.io.OutputStream;

import model.ConnectedComponentInfoRecorder;
import model.ConnectedComponentWrapperVertex;
import model.OurModel;
import model.Vertex;
import model.VertexLogicApplier;

public class ConnectedComponentsInfoPipeFilter extends PipeFilter {

	private final ConnectedComponentInfoRecorder connected_component_info_recorder = new ConnectedComponentInfoRecorder();

	@Override
	public boolean isYourTagEquals(AvailableFilters other) {
		return AvailableFilters.ConnectedComponentsInfo.equals(other);
	}

	@Override
	protected OurModel doYourComputationOn(String pipelineName,
			OurModel input_model,
			PipeFilterComputationListener computationListener) {

		input_model.doOnVertices(new VertexLogicApplier() {

			@Override
			public void apply(Vertex vertex) {

				if (vertex instanceof ConnectedComponentWrapperVertex) {

					ConnectedComponentWrapperVertex connected_component_vertex = (ConnectedComponentWrapperVertex) vertex;
					connected_component_vertex
							.publishYourContentOn(connected_component_info_recorder);
				}

			}
		});

		// like a byPass pipe filter
		return input_model;
	}

	public void writeOn(OutputStream output_stream) {

		this.connected_component_info_recorder
				.writeDataStructure(output_stream);
	}

}
