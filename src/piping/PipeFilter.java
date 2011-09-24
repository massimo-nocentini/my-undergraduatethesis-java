package piping;

import model.OurModel;

public abstract class PipeFilter {

	private OurModel ourModel;
	private PipeFilter wrappedPipeFilter;

	/**
	 * Package access for the constructor, only the factory can call it
	 */
	PipeFilter() {
	}

	public PipeFilter workOn(OurModel ourModel) {
		if (this.isYourWrappedPipeFilterNotNull()) {
			wrappedPipeFilter.workOn(ourModel);
		} else {
			this.ourModel = ourModel;
		}

		return this;
	}

	public boolean isYourWorkingOurModelNotNull() {
		return ourModel != null;
	}

	public boolean isYourLevelOfWrapping(int levelOfWrapping) {
		return this.computeLevelsOfWrapping() == levelOfWrapping;
	}

	private int computeLevelsOfWrapping() {
		int levelsOfWrapping = 0;

		if (this.isYourWrappedPipeFilterNotNull()) {
			levelsOfWrapping = 1 + wrappedPipeFilter.computeLevelsOfWrapping();
		}

		return levelsOfWrapping;
	}

	public boolean isYourWrappedPipeFilterEquals(PipeFilter otherPipeFilter) {
		return this.isYourWrappedPipeFilterNotNull()
				&& wrappedPipeFilter.equals(otherPipeFilter);
	}

	public boolean isYourWrappedPipeFilterNotNull() {
		return wrappedPipeFilter != null;
	}

	public abstract boolean isYourTagEquals(AvailableFilters other);

	protected abstract OurModel doYourComputationOn(String pipelineName,
			OurModel inputModel,
			PipeFilterComputationListener computationListener);

	public final OurModel apply(String pipelineName, OurModel inputModel) {

		return applyWithListener(pipelineName, inputModel,
				new PipeFilterComputationListenerNullObject());
	}

	public final OurModel applyWithListener(String pipelineName,
			OurModel inputModel,
			PipeFilterComputationListener computationListener) {

		computationListener
				.computationStartedWithPipelineIdentifier(formatPhaseIdentifier(pipelineName));

		OurModel workingModel = inputModel;

		if (this.isYourWrappedPipeFilterNotNull()) {

			workingModel = wrappedPipeFilter.applyWithListener(pipelineName,
					workingModel, computationListener);

			ourModel = workingModel;
		}

		workingModel = doYourComputationOn(pipelineName, workingModel,
				computationListener);

		return workingModel;
	}

	public PipeFilter pipeAfter(PipeFilter pipeFilterToWrap) {
		wrappedPipeFilter = pipeFilterToWrap;
		return this;
	}

	public boolean isYourWorkingOurModelEquals(OurModel otherModel) {
		return this.isYourWorkingOurModelNotNull()
				&& this.ourModel.equals(otherModel);
	}

	protected final String formatPhaseIdentifier(String pipelineName) {
		return pipelineName.concat("-").concat(collectPhaseInformation());
	}

	/**
	 * This method collect the informations relative to the application of this
	 * filter. This informations returned are build on top of these components:
	 * <ul>
	 * <li>phase identifier (which for now is computed by getting the classname
	 * of this object's class)
	 * <li>the level of depth where this filter happen to do its job
	 * </ul>
	 * 
	 * @return a string with the previously described informations formatted in
	 *         some way (encapsulated by this method)
	 */
	public String collectPhaseInformation() {

		String phaseIdentifier = this.getClass().getSimpleName();

		String level = String.valueOf(computeLevelsOfWrapping());

		return "phase-".concat(phaseIdentifier).concat("-level-").concat(level);
	}
}
