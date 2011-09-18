package piping;

import junit.framework.Assert;
import model.OurModel;

import org.junit.Test;
import org.sbml.jsbml.Model;

public class BasicPipingUnitTest {

	@Test
	public void creationOfSimplePipeFilter() {
		PipeFilter simpleFilter = PipeFilter
				.Make(AvailableFilters.SimpleFilter);

		Assert.assertNotNull(simpleFilter);
		Assert.assertTrue(simpleFilter.areYouSimplePipeFilter());
	}

	@Test
	public void parsingMethodAvailableOnPipeFilter() {
		PipeFilter simpleFilter = PipeFilter
				.Make(AvailableFilters.SimpleFilter);

		Model sbmlModel = null;
		OurModel parsedModel = simpleFilter.parse(sbmlModel);

		// Assert.assertNotNull(parsedModel);
		// Assert.assertTrue(parsedModel.isEmpty());
	}
}
