package piping;

import junit.framework.Assert;

import org.junit.Test;

public class PipeFilterFactoryUnitTest {

	/**
	 * This test assures that the factory PipeFilterFactory builds a DFS filter
	 * correctly
	 */
	@Test
	public void creationOfDfsPipeFilter() {
		PipeFilter dfsPipeFilter = PipeFilterFactory.MakeDfsPipeFilter();

		Assert.assertNotNull(dfsPipeFilter);
		Assert.assertTrue(dfsPipeFilter.isYourTagEquals(AvailableFilters.DFS));
	}

	/**
	 * This test assures that the factory PipeFilterFactory builds a Printer
	 * filter correctly
	 */
	@Test
	public void creationOfPrinterPipeFilter() {
		PipeFilter printerPipeFilter = PipeFilterFactory
				.MakePrinterPipeFilter();

		Assert.assertNotNull(printerPipeFilter);
		Assert.assertTrue(printerPipeFilter
				.isYourTagEquals(AvailableFilters.Printer));
	}

	/**
	 * This test assures that the factory PipeFilterFactory builds a ByPass
	 * filter correctly
	 */
	@Test
	public void creationOfByPassPipeFilter() {
		PipeFilter byPassPipeFilter = PipeFilterFactory.MakeByPassPipeFilter();

		Assert.assertNotNull(byPassPipeFilter);
		Assert.assertTrue(byPassPipeFilter
				.isYourTagEquals(AvailableFilters.ByPass));
	}

	/**
	 * This test assures that the factory PipeFilterFactory builds a Tarjan
	 * filter correctly
	 */
	@Test
	public void creationOfTarjanPipeFilter() {
		PipeFilter tarjanPipeFilter = PipeFilterFactory.MakeTarjanPipeFilter();

		Assert.assertNotNull(tarjanPipeFilter);
		Assert.assertTrue(tarjanPipeFilter
				.isYourTagEquals(AvailableFilters.Tarjan));
	}

	/**
	 * This test assures that the factory PipeFilterFactory builds a
	 * PlainTextStats filter correctly
	 */
	@Test
	public void creationOfPlainTextStatsPipeFilter() {
		PipeFilter plainTextStatsPipeFilter = PipeFilterFactory
				.MakePlainTextStatsPipeFilter();

		Assert.assertNotNull(plainTextStatsPipeFilter);
		Assert.assertTrue(plainTextStatsPipeFilter
				.isYourTagEquals(AvailableFilters.PlainTextStats));
	}

	/**
	 * This test assures that the factory PipeFilterFactory builds a
	 * PlainTextStats filter correctly
	 */
	@Test
	public void creationOfSourcesCollapserPipeFilter() {
		PipeFilter sourcesCollapserPipeFilter = PipeFilterFactory
				.MakeSourcesCollapserPipeFilter();

		Assert.assertNotNull(sourcesCollapserPipeFilter);
		Assert.assertTrue(sourcesCollapserPipeFilter
				.isYourTagEquals(AvailableFilters.SourcesCollapser));
	}

}
