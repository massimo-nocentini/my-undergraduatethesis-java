package piping;

public class PipeFilterFactory {

	public static PipeFilter MakeDfsPipeFilter() {

		return new DfsPipeFilter();
	}

	public static PipeFilter MakePrinterPipeFilter() {
		PipeFilter filterObject = new PrinterPipeFilter();

		return filterObject;
	}

}
