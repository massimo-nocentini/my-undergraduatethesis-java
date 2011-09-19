package piping;

import dotInterface.DotExporter;
import dotInterface.DotFileUtilHandler;
import dotInterface.SimpleExporter;

public class PrinterPipeFilter extends PipeFilter {

	public PrinterPipeFilter(String pipelineName) {
		super(pipelineName);
	}

	@Override
	public boolean isYourTagEquals(AvailableFilters other) {
		return AvailableFilters.Printer.equals(other);
	}

	private void computeAtomically() {
		DotExporter exporter = new SimpleExporter();
		getOurModel().acceptExporter(exporter);

		DotFileUtilHandler.MakeHandler(this.getPipelineName())
				.writeDotRepresentationInTestFolder(exporter)
				.produceSvgOutput();
	}

	@Override
	public PipeFilter apply() {
		computeAtomically();
		getOutputListener().onOutputProduced(getOurModel());
		return this;
	}

}
