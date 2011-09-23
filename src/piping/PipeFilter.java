package piping;

import model.OurModel;

public abstract class PipeFilter implements PipeFilterOutputListener {

	private OurModel ourModel;
	private PipeFilterOutputListener outputListener;
	private String pipelineName;
	private PipeFilter wrappedPipeFilter;

	protected PipeFilterOutputListener getOutputListener() {

		PipeFilterOutputListener result = new NullPipeFilterOutputListener();

		if (this.isYourListenerNotNull()) {
			result = outputListener;
		}

		return result;
	}

	protected String getPipelineName() {
		return pipelineName;
	}

	public PipeFilter(String pipelineName) {
		this.pipelineName = pipelineName;
	}

	public PipeFilter workOn(OurModel ourModel) {
		if (this.isYourWrappedPipeFilterNotNull()) {
			wrappedPipeFilter.workOn(ourModel);
		} else {
			this.ourModel = ourModel;
		}

		return this;
	}

	public PipeFilter acceptOutputListener(PipeFilterOutputListener listener) {
		this.outputListener = listener;
		return this;
	}

	public boolean isYourWorkingOurModelNotNull() {
		return ourModel != null;
	}

	public boolean isYourListenerNotNull() {
		return outputListener != null;
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

	public boolean isYourPipelineNameEquals(String pipelineName) {
		return this.pipelineName.equals(pipelineName);
	}

	public boolean isYourWrappedPipeFilterEquals(PipeFilter otherPipeFilter) {
		return this.isYourWrappedPipeFilterNotNull()
				&& wrappedPipeFilter.equals(otherPipeFilter);
	}

	public boolean isYourWrappedPipeFilterNotNull() {
		return wrappedPipeFilter != null;
	}

	public abstract boolean isYourTagEquals(AvailableFilters other);

	protected abstract OurModel doYourComputationOn(OurModel inputModel);

	public final PipeFilter apply() {

		if (this.isYourWrappedPipeFilterNotNull()) {
			wrappedPipeFilter.apply();
		} else {
			// TODO: in the refactored version the 'ourModel' will be given as
			// parameter of this method.
			this.runDedicatedComputationAndNotifyOnListenerIfPresent(ourModel);
		}

		return this;
	}

	private void runDedicatedComputationAndNotifyOnListenerIfPresent(
			OurModel inputModel) {

		OurModel computedOurModel = this.doYourComputationOn(inputModel);

		getOutputListener().onOutputProduced(computedOurModel);

	}

	public static PipeFilter MakePrinterPipeFilter(String pipelineName) {
		PipeFilter filterObject = new PrinterPipeFilter(pipelineName);

		return filterObject;
	}

	public static PipeFilter MakeDfsPipeFilter(String pipelineName) {

		return new DfsPipeFilter(pipelineName);
	}

	public PipeFilter pipeAfter(PipeFilter pipeFilterToWrap) {
		wrappedPipeFilter = pipeFilterToWrap;
		wrappedPipeFilter.acceptOutputListener(this);
		return this;
	}

	@Override
	public void onOutputProduced(OurModel manufacturedModel) {
		ourModel = manufacturedModel;
		runDedicatedComputationAndNotifyOnListenerIfPresent(manufacturedModel);
	}

	public boolean isYourListenerEquals(PipeFilterOutputListener outputListener) {
		return this.isYourListenerNotNull()
				&& this.outputListener.equals(outputListener);
	}

	public boolean isYourWorkingOurModelEquals(OurModel otherModel) {
		return this.isYourWorkingOurModelNotNull()
				&& this.ourModel.equals(otherModel);
	}

	protected final String formatPhaseIdentifier() {

		return pipelineName.concat(collectPhaseInformation());
	}

	public boolean isYourPhaseIdentifier(String otherPhaseIdentifier) {
		return formatPhaseIdentifier().equals(otherPhaseIdentifier);
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

		return "-phase-".concat(phaseIdentifier).concat("-level-")
				.concat(level);
	}
}
