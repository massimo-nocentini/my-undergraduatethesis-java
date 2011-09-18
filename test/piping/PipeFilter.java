package piping;

import model.OurModel;

import org.sbml.jsbml.Model;

public class PipeFilter {

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

	public OurModel parse(Model sbmlModel) {
		return null;
	}
}
