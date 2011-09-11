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
	public void wrapAnEmptySBMLModel() throws NullSBMLModelArgumentException {
		Model sbmlModel = new Model();

		SBMLWrapper wrapper = SBMLWrapper.wrap(sbmlModel);

		assertNotNull(wrapper);
		assertTrue(wrapper.isEmpty());
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
		sbmlModel.createSpecies();
		sbmlModel.createSpecies();

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

		Species reactant = new Species();
		Species product = new Species();

		sbmlModel.addSpecies(reactant);
		sbmlModel.addSpecies(product);

		Reaction reaction = sbmlModel.createReaction("reaction_id");
		// add a test in the learning test to keep the species reference
		reaction.createReactant(reactant);
		reaction.createProduct(product);

		SBMLWrapper wrapper = SBMLWrapper.wrap(sbmlModel);

		// in this case I re-do a check on null because this time
		// I've a non empty sbml model so I want to be sure
		// that the static factory wrap method works correctly
		assertNotNull(wrapper);
		assertFalse(wrapper.isEmpty());
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
}
