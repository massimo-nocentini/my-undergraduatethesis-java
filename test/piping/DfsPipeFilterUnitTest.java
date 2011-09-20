package piping;

import junit.framework.Assert;

import org.junit.Test;

public class DfsPipeFilterUnitTest {

	@Test
	public void buildPipeWithThreeLevelsDfsInMiddle() {
		String pipelineName = "pipelineName";

		PipeFilter printerPipeFilter = PipeFilter
				.MakePrinterPipeFilter(pipelineName);

		PipeFilter dfsPipeFilter = PipeFilter.MakeDfsPipeFilter(pipelineName);

		dfsPipeFilter = dfsPipeFilter.pipeAfter(printerPipeFilter);

		Assert.assertTrue(printerPipeFilter.isYourListenerNotNull());
		Assert.assertTrue(printerPipeFilter.isYourListenerEquals(dfsPipeFilter));
		Assert.assertFalse(dfsPipeFilter.isYourListenerNotNull());
	}

}
