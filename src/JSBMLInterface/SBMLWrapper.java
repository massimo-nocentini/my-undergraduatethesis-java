package JSBMLInterface;

import org.sbml.jsbml.Model;

public class SBMLWrapper {

	private Model sbmlModel;

	private SBMLWrapper(Model sbmlModel) {
		this.sbmlModel = sbmlModel;
	}

	public static SBMLWrapper wrap(Model sbmlModel)
			throws NullSBMLModelArgumentException {

		if (sbmlModel == null) {
			throw new NullSBMLModelArgumentException();
		}

		return new SBMLWrapper(sbmlModel);
	}

	public boolean isEmpty() {

		boolean ret = false;
		if (sbmlModel.getListOfSpecies().size() == 0
				&& sbmlModel.getListOfReactions().size() == 0) {
			ret = true;
		}

		return ret;
	}

}
