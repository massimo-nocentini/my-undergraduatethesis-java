package jSBMLearning;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Test
	public void SBMLCheckingNONUniquenessIDOfSpeciesInAllReactantSets() {
		try {
			Map<String, Species> result = new HashMap<String, Species>();

			boolean uniqueness = true;

			SBMLDocument document = (new SBMLReader())
					.readSBML("sbml-test-files/allCpdsMetabSmmReactionsCompounds.xml");
			Model model = document.getModel();
			label: for (Reaction reaction : model.getListOfReactions()) {
				for (SpeciesReference species : reaction.getListOfReactants()) {

					String domainObjectId = species.getSpeciesInstance()
							.getId();
					String domainObjectName = species.getSpeciesInstance()
							.getName();

					if (result.containsKey(domainObjectId) == false) {
						result.put(domainObjectId, species.getSpeciesInstance());
					} else if (result.get(domainObjectId).equals(
							species.getSpeciesInstance())
							&& result.get(domainObjectId) == species
									.getSpeciesInstance()) {
						uniqueness = false;
						break label;
					} else {
						Assert.fail("The species with id "
								+ domainObjectId
								+ " and with name "
								+ domainObjectName
								+ " belong to at least two reactants sets of "
								+ "different reactions but are not an equivalent species"
								+ "respect the one already known. UNEXPECTED CONTEXT");

					}

				}
			}

			Assert.assertFalse(
					"Uniqueness by meta id of species among reactants sets "
							+ "is assured, while is known for the metabolic net"
							+ "under test that uniqueness of species is violated.",
					uniqueness);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (XMLStreamException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	// @Test
	public void SBMLReading() {
		try {
			SBMLDocument document = (new SBMLReader())
					.readSBML("sbml-test-files/allCpdsMetabSmmReactionsCompounds.xml");
			Model model = document.getModel();

			@SuppressWarnings("unused")
			ListOf<Reaction> listOfReactions = model.getListOfReactions();

			@SuppressWarnings("unused")
			ListOf<Species> listOfSpecies = model.getListOfSpecies();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (XMLStreamException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	// @Test
	public void SBMLWriting() {
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
		assertTrue(sbmlModel.getListOfReactions().contains(reaction));
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
				productReference));
		Assert.assertFalse(reaction.getListOfProducts().contains(
				reactantReference));
		Assert.assertFalse(reaction.getListOfReactants().contains(
				outsideSpecies));
		Assert.assertFalse(reaction.getListOfReactants().contains(
				outsideSpecies));
	}

	@Test
	public void uniquenessOfSpeciesIdInsideModelWithOneCompartment() {
		this.uniquenessTestCommonMethod("sbml-test-files/allCpdsMetabSmmReactionsCompounds.xml");
	}

	@Test
	public void uniquenessOfSpeciesIdInsideModelWithMultipleCompartments() {
		/**
		 * In this file there exists the species with id=CARDIOLIPIN that is
		 * defined twice, one that belong to two different compartments at the
		 * same time
		 */
		this.uniquenessTestCommonMethod("sbml-test-files/"
				+ "allCpdsMetabSmmReactionsCompounds_multipleCompartments.xml");
	}

	private void uniquenessTestCommonMethod(String filePath) {
		try {
			Map<String, List<Species>> mapOfListOfSpecies = new HashMap<String, List<Species>>();

			SBMLDocument document = (new SBMLReader()).readSBML(filePath);

			Model model = document.getModel();
			for (Species species : model.getListOfSpecies()) {

				String compartmentId = species.getCompartmentInstance().getId();

				if (mapOfListOfSpecies.containsKey(compartmentId) == false) {
					mapOfListOfSpecies.put(compartmentId,
							new ArrayList<Species>());
				}

				mapOfListOfSpecies.get(compartmentId).add(species);

			}

			String violatingSpeciesId = null;
			String violatedCompartment = null;
			boolean uniquenessInsideCompartmentViolated = false;
			List<String> listOfSpeciesIds = new ArrayList<String>();
			label: for (List<Species> listOfSpecies : mapOfListOfSpecies
					.values()) {

				for (Species species : listOfSpecies) {
					String speciesId = species.getId();
					if (listOfSpeciesIds.contains(speciesId)) {

						violatingSpeciesId = speciesId;
						violatedCompartment = species.getCompartmentInstance()
								.getId();
						uniquenessInsideCompartmentViolated = true;
						break label;
					}
				}

				listOfSpeciesIds.clear();
			}

			if (uniquenessInsideCompartmentViolated == true) {
				Assert.fail("The species with id "
						+ violatingSpeciesId
						+ " violates the uniqueness of id inside the compartment "
						+ violatedCompartment);
			}

			// else the uniqueness is guaranteed

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (XMLStreamException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void everySpeciesShouldBeContainedInSomeCompartment() {
		try {
			SBMLDocument document = (new SBMLReader())
					.readSBML("sbml-test-files/allCpdsMetabSmmReactionsCompounds.xml");

			Model model = document.getModel();
			String violatingSpeciesId = null;
			boolean violated = false;
			for (Species species : model.getListOfSpecies()) {

				if (species.getCompartmentInstance() == null) {
					violatingSpeciesId = species.getId();
					violated = true;
				}
			}

			if (violated == true) {
				Assert.fail("The species with id " + violatingSpeciesId
						+ " isn't cointained in any compartments.");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (XMLStreamException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void createSpeciesWithEqualsIdInTheSameCompartment() {
		Model sbmlModel = new Model();

		Compartment compartment = sbmlModel.createCompartment("compartment_id");

		Species firstAdded = sbmlModel.createSpecies("id1", compartment);
		Species secondAdded = sbmlModel.createSpecies("id1", compartment);

		Assert.assertNotSame(firstAdded, secondAdded);
		Assert.assertEquals(firstAdded, secondAdded);
		Assert.assertEquals(1, sbmlModel.getListOfSpecies().size());

		Assert.assertNotNull(firstAdded.getCompartmentInstance());

		/**
		 * Here instead of gaining an exception during the creation of the
		 * species (because I'm trying to add a second species with an id
		 * already used by another species in the same compartment) we have a
		 * valid species object (instantiated in memory) but with no compartment
		 * associated with.
		 */
		Assert.assertNull(secondAdded.getCompartmentInstance());
	}

	@Test
	public void createSpeciesWithEqualsIdInDifferentCompartments() {
		Model sbmlModel = new Model();

		Compartment compartment = sbmlModel.createCompartment("compartment_id");
		Compartment anotherCompartment = sbmlModel
				.createCompartment("another_compartment_id");

		Species firstAdded = sbmlModel.createSpecies("id1", compartment);
		Species secondAdded = sbmlModel
				.createSpecies("id1", anotherCompartment);

		Assert.assertNotSame(firstAdded, secondAdded);
		Assert.assertFalse(firstAdded.equals(secondAdded));

		// guessing 1 is very hard! the two added species are not equals
		// by the above test, although the count species in the model
		// is 1, like if the second is ignored.
		Assert.assertEquals(1, sbmlModel.getListOfSpecies().size());
	}

	@Test
	public void createSpeciesWithDifferentIdInDifferentCompartments() {
		Model sbmlModel = new Model();

		Compartment compartment = sbmlModel.createCompartment("compartment_id");
		Compartment anotherCompartment = sbmlModel
				.createCompartment("another_compartment_id");

		Species firstAdded = sbmlModel.createSpecies("id1", compartment);
		Species secondAdded = sbmlModel
				.createSpecies("id2", anotherCompartment);

		Assert.assertNotSame(firstAdded, secondAdded);
		Assert.assertFalse(firstAdded.equals(secondAdded));
		Assert.assertEquals(2, sbmlModel.getListOfSpecies().size());
	}
}