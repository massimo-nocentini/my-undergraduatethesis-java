package JSBMLInterface;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.Species;

public class SBMLWrapperUnitTest {

	@Test
	public void wrapNewSBMLModelLeadEmptyWrapperProperty()
			throws NullSBMLModelArgumentException {
		Model sbmlModel = new Model();

		SBMLWrapper wrapper = SBMLWrapper.wrap(sbmlModel);

		assertTrue(wrapper.isEmpty());
	}

	@Test
	public void wrapFactoryMethod() throws NullSBMLModelArgumentException {
		Model sbmlModel = new Model();

		SBMLWrapper wrapper = SBMLWrapper.wrap(sbmlModel);

		assertNotNull(wrapper);
	}

	@Test
	public void wrapNullSBMLModel() {
		Model sbmlModel = null;
		try {
			SBMLWrapper wrapper = SBMLWrapper.wrap(sbmlModel);
			Assert.fail("This point shouldn't be reached.");
		} catch (NullSBMLModelArgumentException e) {
			// I want this exception, in this case nothing
			// to assert, I'm done right now.
		} catch (Exception e) {
			Assert.assertEquals(NullSBMLModelArgumentException.class,
					e.getClass());
		}
	}

	/**
	 * This test has the scope to capture a model characterized only by a set of
	 * species and no reaction. In this case the model degenerates to a non very
	 * interesting metabolic network.
	 */
	@Test
	public void wrapSBMLModelWithSomeSpeciesButNoReactions()
			throws NullSBMLModelArgumentException {
		Model sbmlModel = new Model();

		// the method createSpecies has the side effect to add
		// the returned new species directly in the model automatically.
		sbmlModel.createSpecies("id1");
		sbmlModel.createSpecies("id2");

		SBMLWrapper wrapper = SBMLWrapper.wrap(sbmlModel);

		// in this case I re-do a check on null because this time
		// I've a non empty sbml model so I want to be sure
		// that the static factory wrap method works correctly
		assertNotNull(wrapper);
		assertFalse(wrapper.isEmpty());
	}

	@Test
	public void wrapSBMLModelWithOnetoOneReaction()
			throws NullSBMLModelArgumentException {
		Model sbmlModel = new Model();

		Species reactant = sbmlModel.createSpecies("id1");
		Species product = sbmlModel.createSpecies("id2");

		Reaction reaction = sbmlModel.createReaction("reaction_id");
		// add a test in the learning test to keep the species reference
		reaction.createReactant(reactant);
		reaction.createProduct(product);

		SBMLWrapper wrapper = SBMLWrapper.wrap(sbmlModel);

		// in this case I re-do a check on null because this time
		// I've a non empty sbml model and with one reaction so I want to be
		// sure that the static factory wrap method works correctly
		assertNotNull(wrapper);
		assertFalse(wrapper.isEmpty());
	}

}
