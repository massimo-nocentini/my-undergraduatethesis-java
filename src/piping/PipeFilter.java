package piping;

import model.OurModel;

public abstract class PipeFilter {

	private OurModel ourModel;
	private PipeFilterOutputListener outputListener;
	private String pipelineName;

	protected PipeFilterOutputListener getOutputListener() {
		return outputListener;
	}

	protected OurModel getOurModel() {
		return ourModel;
	}

	protected String getPipelineName() {
		return pipelineName;
	}

	public PipeFilter(String pipelineName) {
		this.pipelineName = pipelineName;
	}

	public PipeFilter workOn(OurModel ourModel) {
		this.ourModel = ourModel;
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
		return 0 == levelOfWrapping;
	}

	public boolean isYourPipelineNameEquals(String pipelineName) {
		return this.pipelineName.equals(pipelineName);
	}

	public abstract boolean isYourTagEquals(AvailableFilters other);

	public abstract PipeFilter apply();

	public static PipeFilter MakePrinterPipeFilter(String pipelineName) {
		PipeFilter filterObject = new PrinterPipeFilter(pipelineName);

		return filterObject;
	}

}
