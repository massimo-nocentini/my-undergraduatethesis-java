package piping;

import model.OurModel;

public class DfsPipeFilter extends PipeFilter {

	public DfsPipeFilter(String pipelineName) {
		super(pipelineName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isYourTagEquals(AvailableFilters other) {
		return AvailableFilters.DFS.equals(other);
	}

	@Override
	protected OurModel doYourComputation(OurModel ourModel) {
		return OurModel.makeEmptyModel();
	}

}
