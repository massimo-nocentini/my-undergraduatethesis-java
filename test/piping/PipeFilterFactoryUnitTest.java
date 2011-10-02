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
		PipeFilter byPassPipeFilter = PipeFilterFactory.MakeTarjanPipeFilter();

		Assert.assertNotNull(byPassPipeFilter);
		Assert.assertTrue(byPassPipeFilter
				.isYourTagEquals(AvailableFilters.Tarjan));
	}

}
