package piping;

import model.OurModel;
import dotInterface.DotExporter;
import dotInterface.DotFileUtilHandler;
import dotInterface.SimpleExporter;

public class PrinterPipeFilter extends PipeFilter {

	@Override
	public boolean isYourTagEquals(AvailableFilters other) {
		return AvailableFilters.Printer.equals(other);
	}

	@Override
	protected OurModel doYourComputationOn(String pipelineName,
			OurModel inputModel,
			PipeFilterComputationListener computationListener) {

		DotExporter exporter = new SimpleExporter();
		inputModel.acceptExporter(exporter);

		DotFileUtilHandler.makeHandler(formatPhaseIdentifier(pipelineName))
				.writeDotRepresentationInTestFolder(exporter)
				.produceSvgOutput();

		return inputModel;
	}
}
