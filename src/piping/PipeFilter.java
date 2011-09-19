package piping;

import model.OurModel;

public abstract class PipeFilter {

	private OurModel ourModel;

	public static PipeFilter Make(AvailableFilters filter) {
		PipeFilter filterObject = null;
		switch (filter) {
		case SimpleFilter:
			filterObject = new SimplePipeFilter();
			break;

		default:
			break;
		}
		return filterObject;
	}

	public boolean areYouSimplePipeFilter() {
		return true;
	}

	public PipeFilter workOn(OurModel ourModel) {
		this.ourModel = ourModel;
		return this;
	}

	public boolean someoneSettedYouInitialModel() {
		return this.ourModel != null;
	}
}
