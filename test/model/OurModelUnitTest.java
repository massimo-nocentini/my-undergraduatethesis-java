package model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import junit.framework.Assert;

import org.junit.Test;

public class OurModelUnitTest {

	@Test
	public void creation() {
		OurModel model = OurModel.makeEmptyModel();
		assertNotNull(model);
	}

	@Test
	public void emptyAfterCreation() {
		OurModel model = OurModel.makeEmptyModel();
		assertTrue(model.isEmpty());
	}

	@Test
	public void checkMinusMethod() {
		// TODO: write down this test and implement the necessary logic to pass
		// it. Bookmark fail.
		Assert.fail();
	}
}
