package jSBMLearning;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamException;

import org.junit.Assert;
import org.junit.Test;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Creator;
import org.sbml.jsbml.History;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.SBase;
import org.sbml.jsbml.SBaseChangedEvent;
import org.sbml.jsbml.SBaseChangedListener;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;

public class JSBMLearningTest {

	// @Test
	public void SBMLReading() {
		try {
			SBMLDocument document = (new SBMLReader())
					.readSBML("sbml-test-files/allCpdsMetabSmmReactionsCompounds.xml");
			Model model = document.getModel();
			ListOf<Reaction> listOfReactions = model.getListOfReactions();
			ListOf<Species> listOfSpecies = model.getListOfSpecies();
			int a = 3;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (XMLStreamException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	// @Test
	public void testSBMLWriting() {
		// Create a new SBMLDocument, using SBML level 2 version 4.
		SBMLDocument doc = new SBMLDocument(2, 4);
		doc.addChangeListener(new SBaseChangedListener_InnerClass());
		// Create a new SBML-Model and compartment in the document
		Model model = doc.createModel("test_model");
		model.setMetaId("meta_" + model.getId());
		Compartment compartment = model.createCompartment("default");
		compartment.setSize(1d);
		// Create model history
		History hist = new History();
		Creator creator = new Creator("Given Name", "Family Name",
				"My Organisation", "My@EMail.com");
		hist.addCreator(creator);
		model.setHistory(hist);
		// Create some example content in the document
		Species specOne = model.createSpecies("test_spec1", compartment);
		Species specTwo = model.createSpecies("test_spec2", compartment);
		Reaction sbReaction = model.createReaction("reaction_id");
		// Add a substrate (SBO: 15) and product (SBO: 11).
		SpeciesReference subs = sbReaction.createReactant(specOne);
		subs.setSBOTerm(15);
		SpeciesReference prod = sbReaction.createProduct(specTwo);
		prod.setSBOTerm(11);
		// Write the SBML document to disk
		try {
			new SBMLWriter().write(doc, "test.sbml.xml", "ProgName", "Version");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (XMLStreamException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (SBMLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	public class SBaseChangedListener_InnerClass implements
			SBaseChangedListener {

		@Override
		public void sbaseAdded(SBase sb) {
			System.out.println("[ADD] " + sb);
		}

		@Override
		public void sbaseRemoved(SBase sb) {
			System.out.println("[RMV] " + sb);
		}

		@Override
		public void stateChanged(SBaseChangedEvent ev) {
			System.out.println("[CHG] " + ev);
		}
	}

	@Test
	public void createSpecies() {
		Model sbmlModel = new Model();

		Species aSpecie = sbmlModel.createSpecies();
		Species anotherSpecie = sbmlModel.createSpecies();

		assertNotNull(aSpecie);
		assertNotNull(anotherSpecie);
	}

	@Test
	/**
	 * This method shows that if the species are build with
	 * different id, that they are not equivalent
	 */
	public void speciesCreatedByModelWithDifferentIdAreDistinct() {
		Model sbmlModel = new Model();

		Species aSpecie = sbmlModel.createSpecies("id1");
		Species anotherSpecie = sbmlModel.createSpecies("id2");

		assertNotSame(aSpecie, anotherSpecie);
		assertFalse(aSpecie.equals(anotherSpecie));
	}

	@Test
	/**
	 * This method shows that if the species are build with
	 * different id, that they are not equivalent
	 */
	public void speciesCreatedByModelWithoutIdAreEquivalent() {
		Model sbmlModel = new Model();

		Species aSpecie = sbmlModel.createSpecies();
		Species anotherSpecie = sbmlModel.createSpecies();

		assertNotSame(aSpecie, anotherSpecie);
		assertTrue(aSpecie.equals(anotherSpecie));
	}

	@Test
	public void createAndCheckModelBelongingSpecies() {
		Model sbmlModel = new Model();

		Species aSpecie = sbmlModel.createSpecies("id1");
		Species anotherSpecie = sbmlModel.createSpecies("id2");

		Species thirdSpecies = new Species("id3");

		assertTrue(sbmlModel.getListOfSpecies().contains(aSpecie));
		assertTrue(sbmlModel.getListOfSpecies().contains(anotherSpecie));
		assertFalse("The model contains a species standalone created.",
				sbmlModel.getListOfSpecies().contains(thirdSpecies));
	}

	@Test
	public void createReaction() {
		Model sbmlModel = new Model();

		Reaction reaction = sbmlModel.createReaction("someid");

		assertNotNull(reaction);
	}

	@Test
	public void characterizeReaction() {
		Model sbmlModel = new Model();

		Species reactant = sbmlModel.createSpecies("id1");
		Species product = sbmlModel.createSpecies("id2");
		Species outsideSpecies = sbmlModel.createSpecies("out");

		Reaction reaction = sbmlModel.createReaction("reaction_id");
		// add a test in the learning test to keep the species reference
		SpeciesReference reactantReference = reaction.createReactant(reactant);
		SpeciesReference productReference = reaction.createProduct(product);

		Assert.assertTrue(reaction.getListOfReactants().contains(
				reactantReference));
		Assert.assertTrue(reaction.getListOfProducts().contains(
				productReference));
		Assert.assertFalse(reaction.getListOfReactants().contains(
				outsideSpecies));
		Assert.assertFalse(reaction.getListOfReactants().contains(
				outsideSpecies));
	}
}