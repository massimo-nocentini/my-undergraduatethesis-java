package JSBMLInterface;

import model.OurModel;

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

	public OurModel importSBML(Model sbmlModel) {
		if (this.isEmpty()) {
			return OurModel.MakeEmptyModel();
		}

		// per adesso posso far ritornare lo stesso un modello vuoto
		return OurModel.MakeEmptyModel();
	}

	public boolean isEmpty() {
		if (sbmlModel.getListOfSpecies().size() == 0
				&& sbmlModel.getListOfReactions().size() == 0) {
			return true;
		}

		return false;
	}

}
