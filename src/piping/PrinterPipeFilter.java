package piping;

import dotInterface.DotExporter;
import dotInterface.DotFileUtilHandler;
import dotInterface.SimpleExporter;

public class PrinterPipeFilter extends PipeFilter {

	public PrinterPipeFilter(String pipelineName) {
		super(pipelineName);
	}

	@Override
	public boolean areYouAn(AvailableFilters other) {
		return AvailableFilters.Printer.equals(other);
	}

	@Override
	protected void computeAtomically() {
		DotExporter exporter = new SimpleExporter();
		getOurModel().acceptExporter(exporter);

		DotFileUtilHandler.MakeHandler(this.getPipelineName())
				.writeDotRepresentationInTestFolder(exporter)
				.produceSvgOutput();
	}

}
