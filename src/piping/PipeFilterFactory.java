package piping;

public class PipeFilterFactory {

	public static PipeFilter MakeDfsPipeFilter() {
		return new DfsPipeFilter();
	}

	public static PipeFilter MakePrinterPipeFilter() {
		return new PrinterPipeFilter();
	}

	public static PipeFilter MakeByPassPipeFilter() {
		return new ByPassPipeFilter();
	}

}
