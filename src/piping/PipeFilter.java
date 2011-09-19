package piping;

import model.OurModel;

public abstract class PipeFilter {

	private OurModel ourModel;

	protected OurModel getOurModel() {
		return ourModel;
	}

	public PipeFilter(String pipelineName) {
		this.pipelineName = pipelineName;
	}

	private PipeFilterOutputListener outputListener;
	private String pipelineName;

	// public static PipeFilter Make(AvailableFilters filter) {
	// PipeFilter filterObject = null;
	// switch (filter) {
	// case Printer:
	// filterObject = new PrinterPipeFilter();
	// break;
	//
	// default:
	// break;
	// }
	// return filterObject;
	// }

	protected String getPipelineName() {
		return pipelineName;
	}

	public abstract boolean areYouAn(AvailableFilters other);

	public PipeFilter workOn(OurModel ourModel) {
		this.ourModel = ourModel;
		return this;
	}

	public boolean someoneSettedYouInitialModel() {
		return this.ourModel != null;
	}

	public boolean haveYouAnOurModel() {
		return ourModel != null;
	}

	public PipeFilter acceptOutputListener(PipeFilterOutputListener listener) {
		this.outputListener = listener;
		return this;
	}

	public boolean someoneIsListeningYou() {
		return outputListener != null;
	}

	public PipeFilter apply() {
		computeAtomically();
		outputListener.onOutputProduced(ourModel);
		return this;
	}

	protected abstract void computeAtomically();

	public boolean isYourLevelOfWrapping(int levelOfWrapping) {
		return 0 == levelOfWrapping;
	}

	public static PipeFilter MakePrinterPipeFilter(String pipelineName) {
		PipeFilter filterObject = new PrinterPipeFilter(pipelineName);

		return filterObject;
	}

	public boolean isYourPipelineNameEquals(String pipelineName) {
		return this.pipelineName.equals(pipelineName);

	}
}
