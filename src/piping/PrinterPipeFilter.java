package piping;

import model.OurModel;
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

	@Override
	protected OurModel doYourComputation() {
		DotExporter exporter = new SimpleExporter();
		getOurModel().acceptExporter(exporter);

		DotFileUtilHandler.MakeHandler(this.getPipelineName())
				.writeDotRepresentationInTestFolder(exporter)
				.produceSvgOutput();

		return getOurModel();
	}
}
