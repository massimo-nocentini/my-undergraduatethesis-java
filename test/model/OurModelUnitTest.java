package model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
}
