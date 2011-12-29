package piping;

public class PipeFilterFactory {

	public static DfsPipeFilter MakeDfsPipeFilter() {
		return new DfsPipeFilter();
	}

	public static PrinterPipeFilter MakePrinterPipeFilter() {
		return new PrinterPipeFilter();
	}

	public static ByPassPipeFilter MakeByPassPipeFilter() {
		return new ByPassPipeFilter();
	}

	public static TarjanPipeFilter MakeTarjanPipeFilter() {
		return new TarjanPipeFilter();
	}

	public static PlainTextStatsPipeFilter MakePlainTextStatsPipeFilter() {
		return new PlainTextStatsPipeFilter();
	}

	public static SourcesCollapserPipeFilter MakeSourcesCollapserPipeFilter() {
		return new SourcesCollapserPipeFilter();
	}

	public static ConnectedComponentsInfoPipeFilter MakeConnectedComponentsInfoPipeFilter() {
		return new ConnectedComponentsInfoPipeFilter();
	}

}
