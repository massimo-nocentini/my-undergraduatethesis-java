package model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OurModelUnitTest {

	@Test
	public void creation() {
		OurModel model = OurModel.MakeEmptyModel();
		assertNotNull(model);
	}

	@Test
	public void emptyAfterCreation() {
		OurModel model = OurModel.MakeEmptyModel();
		assertTrue(model.isEmpty());
	}
}
