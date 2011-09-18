package piping;

import junit.framework.Assert;
import model.OurModel;

import org.junit.Test;

public class BasicPipingUnitTest {

	@Test
	public void creationOfSimplePipeFilter() {
		PipeFilter simpleFilter = PipeFilter
				.Make(AvailableFilters.SimpleFilter);

		Assert.assertNotNull(simpleFilter);
		Assert.assertTrue(simpleFilter.areYouSimplePipeFilter());
	}

	@Test
	public void setInitialOurModel() {
		PipeFilter simpleFilter = PipeFilter
				.Make(AvailableFilters.SimpleFilter);

		simpleFilter.workOn(OurModel.makeEmptyModel());

		Assert.assertTrue(simpleFilter.someoneSettedYouInitialModel());
		Assert.assertEquals(SimplePipeFilter.class, simpleFilter.getClass());
	}

}
