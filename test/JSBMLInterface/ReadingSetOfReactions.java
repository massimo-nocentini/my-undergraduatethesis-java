package JSBMLInterface;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import model.SimpleVertex;
import model.Vertex;

import org.junit.Test;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.Species;

public class ReadingSetOfReactions {

	@Test
	public void twoIrreversibleReactionsWithNothingInCommon() {
		Connector connector = Connector.makeConnector();

		Model sbmlModel = new Model();

		Map<Vertex, Set<Vertex>> checkingMap = new HashMap<Vertex, Set<Vertex>>();

		Set<Vertex> setOfProductsOfFirstReaction = new HashSet<Vertex>();
		Set<Vertex> setOfProductsOfSecondReaction = new HashSet<Vertex>();

		Compartment compartment = sbmlModel.createCompartment("compartment_id");

		Species reactant1 = sbmlModel.createSpecies("idR1", compartment);
		Species reactant2 = sbmlModel.createSpecies("idR2", compartment);
		Species reactant3 = sbmlModel.createSpecies("idR3", compartment);
		Species product1 = sbmlModel.createSpecies("idP1", compartment);
		Species product2 = sbmlModel.createSpecies("idP2", compartment);
		Species product3 = sbmlModel.createSpecies("idP3", compartment);

		Vertex vR1 = SimpleVertex.makeVertex(reactant1);
		Vertex vR2 = SimpleVertex.makeVertex(reactant2);
		Vertex vR3 = SimpleVertex.makeVertex(reactant3);
		Vertex vP1 = SimpleVertex.makeVertex(product1);
		Vertex vP2 = SimpleVertex.makeVertex(product2);
		Vertex vP3 = SimpleVertex.makeVertex(product3);

		setOfProductsOfFirstReaction.add(vP1);
		setOfProductsOfSecondReaction.add(vP2);
		setOfProductsOfSecondReaction.add(vP3);

		checkingMap.put(vR1, setOfProductsOfFirstReaction);
		checkingMap.put(vR2, setOfProductsOfFirstReaction);
		checkingMap.put(vR3, setOfProductsOfSecondReaction);

		Reaction reaction = sbmlModel.createReaction("reaction1_id");
		reaction.setReversible(false);

		// add a test in the learning test to keep the species reference
		reaction.createReactant(reactant1);
		reaction.createReactant(reactant2);
		reaction.createProduct(product1);

		Reaction anotherReaction = sbmlModel.createReaction("reaction2_id");
		anotherReaction.setReversible(false);

		// add a test in the learning test to keep the species reference
		anotherReaction.createReactant(reactant3);
		anotherReaction.createProduct(product2);
		anotherReaction.createProduct(product3);

		final Set<Vertex> reactants = new HashSet<Vertex>();
		final Set<Vertex> products = new HashSet<Vertex>();

		Set<Vertex> vertices = connector.readReactions(
				sbmlModel.getListOfReactions(),
				new VertexHandlingWithSourceListener() {

					@Override
					public void vertexHandled(Vertex vertex) {
					}

					@Override
					public void reactantVertexHandled(Vertex vertex) {
						reactants.add(vertex);
					}

					@Override
					public void productVertexHandled(Vertex vertex) {
						products.add(vertex);
					}
				});

		assertEquals(6, vertices.size());

		assertEquals(3, reactants.size());
		assertEquals(3, products.size());

		for (Vertex vertex : reactants) {
			assertTrue(vertex.isYourNeighborhoodEquals(checkingMap.get(vertex)));
		}

		for (Vertex vertex : products) {
			assertTrue(vertex.isYourNeighborhoodEmpty());
		}
	}

	@Test
	public void twoReversibleReactionsWithNothingInCommon() {
		Connector connector = Connector.makeConnector();

		Model sbmlModel = new Model();

		Map<Vertex, Set<Vertex>> reactantsCheckingMap = new HashMap<Vertex, Set<Vertex>>();
		Map<Vertex, Set<Vertex>> productsCheckingMap = new HashMap<Vertex, Set<Vertex>>();

		Set<Vertex> setOfProductsOfFirstReaction = new HashSet<Vertex>();
		Set<Vertex> setOfProductsOfSecondReaction = new HashSet<Vertex>();
		Set<Vertex> setOfReactantsOfFirstReaction = new HashSet<Vertex>();
		Set<Vertex> setOfReactantsOfSecondReaction = new HashSet<Vertex>();

		Compartment compartment = sbmlModel.createCompartment("compartment_id");

		Species reactant1 = sbmlModel.createSpecies("idR1", compartment);
		Species reactant2 = sbmlModel.createSpecies("idR2", compartment);
		Species reactant3 = sbmlModel.createSpecies("idR3", compartment);
		Species product1 = sbmlModel.createSpecies("idP1", compartment);
		Species product2 = sbmlModel.createSpecies("idP2", compartment);
		Species product3 = sbmlModel.createSpecies("idP3", compartment);

		Vertex vR1 = SimpleVertex.makeVertex(reactant1);
		Vertex vR2 = SimpleVertex.makeVertex(reactant2);
		Vertex vR3 = SimpleVertex.makeVertex(reactant3);
		Vertex vP1 = SimpleVertex.makeVertex(product1);
		Vertex vP2 = SimpleVertex.makeVertex(product2);
		Vertex vP3 = SimpleVertex.makeVertex(product3);

		setOfProductsOfFirstReaction.add(vP1);
		setOfProductsOfSecondReaction.add(vP2);
		setOfProductsOfSecondReaction.add(vP3);

		setOfReactantsOfFirstReaction.add(vR1);
		setOfReactantsOfFirstReaction.add(vR2);
		setOfReactantsOfSecondReaction.add(vR3);

		reactantsCheckingMap.put(vR1, setOfProductsOfFirstReaction);
		reactantsCheckingMap.put(vR2, setOfProductsOfFirstReaction);
		reactantsCheckingMap.put(vR3, setOfProductsOfSecondReaction);

		productsCheckingMap.put(vP1, setOfReactantsOfFirstReaction);
		productsCheckingMap.put(vP2, setOfReactantsOfSecondReaction);
		productsCheckingMap.put(vP3, setOfReactantsOfSecondReaction);

		Reaction reaction = sbmlModel.createReaction("reaction1_id");
		reaction.setReversible(true);

		// add a test in the learning test to keep the species reference
		reaction.createReactant(reactant1);
		reaction.createReactant(reactant2);
		reaction.createProduct(product1);

		Reaction anotherReaction = sbmlModel.createReaction("reaction2_id");
		anotherReaction.setReversible(true);

		// add a test in the learning test to keep the species reference
		anotherReaction.createReactant(reactant3);
		anotherReaction.createProduct(product2);
		anotherReaction.createProduct(product3);

		final Set<Vertex> reactants = new HashSet<Vertex>();
		final Set<Vertex> products = new HashSet<Vertex>();

		Set<Vertex> vertices = connector.readReactions(
				sbmlModel.getListOfReactions(),
				new VertexHandlingWithSourceListener() {

					@Override
					public void vertexHandled(Vertex vertex) {
					}

					@Override
					public void reactantVertexHandled(Vertex vertex) {
						reactants.add(vertex);
					}

					@Override
					public void productVertexHandled(Vertex vertex) {
						products.add(vertex);
					}
				});

		assertEquals(6, vertices.size());

		assertEquals(3, reactants.size());
		assertEquals(3, products.size());

		for (Vertex vertex : reactants) {
			assertTrue(vertex.isYourNeighborhoodEquals(reactantsCheckingMap
					.get(vertex)));
		}

		for (Vertex vertex : products) {
			assertTrue(vertex.isYourNeighborhoodEquals(productsCheckingMap
					.get(vertex)));
		}
	}

	@Test
	public void twoIrreversibleReactionsWithSpeciesInCommonOnReactants() {
		Connector connector = Connector.makeConnector();

		Model sbmlModel = new Model();

		Map<Vertex, Set<Vertex>> checkingMap = new HashMap<Vertex, Set<Vertex>>();

		Set<Vertex> setOfProductsOfFirstReaction = new HashSet<Vertex>();
		Set<Vertex> setOfProductsOfSecondReaction = new HashSet<Vertex>();

		Compartment compartment = sbmlModel.createCompartment("compartment_id");

		Species reactant1 = sbmlModel.createSpecies("idR1", compartment);
		Species reactant2 = sbmlModel.createSpecies("idR2", compartment);

		// common species
		Species reactant3 = sbmlModel.createSpecies("idR3", compartment);

		Species reactant4 = sbmlModel.createSpecies("idR4", compartment);
		Species product1 = sbmlModel.createSpecies("idP1", compartment);
		Species product2 = sbmlModel.createSpecies("idP2", compartment);
		Species product3 = sbmlModel.createSpecies("idP3", compartment);

		Vertex vR1 = SimpleVertex.makeVertex(reactant1);
		Vertex vR2 = SimpleVertex.makeVertex(reactant2);
		Vertex vR3 = SimpleVertex.makeVertex(reactant3);
		Vertex vR4 = SimpleVertex.makeVertex(reactant4);
		Vertex vP1 = SimpleVertex.makeVertex(product1);
		Vertex vP2 = SimpleVertex.makeVertex(product2);
		Vertex vP3 = SimpleVertex.makeVertex(product3);

		setOfProductsOfFirstReaction.add(vP1);
		setOfProductsOfSecondReaction.add(vP2);
		setOfProductsOfSecondReaction.add(vP3);

		Set<Vertex> setOfProductsOfCommonSpecies = new HashSet<Vertex>();
		setOfProductsOfCommonSpecies.addAll(setOfProductsOfFirstReaction);
		setOfProductsOfCommonSpecies.addAll(setOfProductsOfSecondReaction);

		checkingMap.put(vR1, setOfProductsOfFirstReaction);
		checkingMap.put(vR2, setOfProductsOfFirstReaction);
		checkingMap.put(vR3, setOfProductsOfCommonSpecies);
		checkingMap.put(vR4, setOfProductsOfSecondReaction);

		Reaction reaction = sbmlModel.createReaction("reaction1_id");
		reaction.setReversible(false);

		// add a test in the learning test to keep the species reference
		reaction.createReactant(reactant1);
		reaction.createReactant(reactant2);
		reaction.createReactant(reactant3);
		reaction.createProduct(product1);

		Reaction anotherReaction = sbmlModel.createReaction("reaction2_id");
		anotherReaction.setReversible(false);

		// add a test in the learning test to keep the species reference
		anotherReaction.createReactant(reactant3);
		anotherReaction.createReactant(reactant4);
		anotherReaction.createProduct(product2);
		anotherReaction.createProduct(product3);

		final Set<Vertex> reactants = new HashSet<Vertex>();
		final Set<Vertex> products = new HashSet<Vertex>();

		Set<Vertex> vertices = connector.readReactions(
				sbmlModel.getListOfReactions(),
				new VertexHandlingWithSourceListener() {

					@Override
					public void vertexHandled(Vertex vertex) {
					}

					@Override
					public void reactantVertexHandled(Vertex vertex) {
						reactants.add(vertex);
					}

					@Override
					public void productVertexHandled(Vertex vertex) {
						products.add(vertex);
					}
				});

		assertEquals(7, vertices.size());

		assertEquals(4, reactants.size());
		assertEquals(3, products.size());

		for (Vertex vertex : reactants) {
			assertTrue(vertex.isYourNeighborhoodEquals(checkingMap.get(vertex)));
		}

		for (Vertex vertex : products) {
			assertTrue(vertex.isYourNeighborhoodEmpty());
		}
	}

	@Test
	public void twoReversibleReactionsWithSpeciesInCommonOnReactants() {
		Connector connector = Connector.makeConnector();

		Model sbmlModel = new Model();

		Map<Vertex, Set<Vertex>> reactantsCheckingMap = new HashMap<Vertex, Set<Vertex>>();
		Map<Vertex, Set<Vertex>> productsCheckingMap = new HashMap<Vertex, Set<Vertex>>();

		Set<Vertex> setOfProductsOfFirstReaction = new HashSet<Vertex>();
		Set<Vertex> setOfProductsOfSecondReaction = new HashSet<Vertex>();
		Set<Vertex> setOfReactantsOfFirstReaction = new HashSet<Vertex>();
		Set<Vertex> setOfReactantsOfSecondReaction = new HashSet<Vertex>();

		Compartment compartment = sbmlModel.createCompartment("compartment_id");

		Species reactant1 = sbmlModel.createSpecies("idR1", compartment);
		Species reactant2 = sbmlModel.createSpecies("idR2", compartment);

		// common species
		Species reactant3 = sbmlModel.createSpecies("idR3", compartment);

		Species reactant4 = sbmlModel.createSpecies("idR4", compartment);
		Species product1 = sbmlModel.createSpecies("idP1", compartment);
		Species product2 = sbmlModel.createSpecies("idP2", compartment);
		Species product3 = sbmlModel.createSpecies("idP3", compartment);

		Vertex vR1 = SimpleVertex.makeVertex(reactant1);
		Vertex vR2 = SimpleVertex.makeVertex(reactant2);
		Vertex vR3 = SimpleVertex.makeVertex(reactant3);
		Vertex vR4 = SimpleVertex.makeVertex(reactant4);
		Vertex vP1 = SimpleVertex.makeVertex(product1);
		Vertex vP2 = SimpleVertex.makeVertex(product2);
		Vertex vP3 = SimpleVertex.makeVertex(product3);

		setOfProductsOfFirstReaction.add(vP1);
		setOfProductsOfSecondReaction.add(vP2);
		setOfProductsOfSecondReaction.add(vP3);

		setOfReactantsOfFirstReaction.add(vR1);
		setOfReactantsOfFirstReaction.add(vR2);
		setOfReactantsOfFirstReaction.add(vR3);
		setOfReactantsOfSecondReaction.add(vR3);
		setOfReactantsOfSecondReaction.add(vR4);

		productsCheckingMap.put(vP1, setOfReactantsOfFirstReaction);
		productsCheckingMap.put(vP2, setOfReactantsOfSecondReaction);
		productsCheckingMap.put(vP3, setOfReactantsOfSecondReaction);

		Set<Vertex> setOfProductsOfCommonSpecies = new HashSet<Vertex>();
		setOfProductsOfCommonSpecies.addAll(setOfProductsOfFirstReaction);
		setOfProductsOfCommonSpecies.addAll(setOfProductsOfSecondReaction);

		// Set<Vertex> setOfReactantsOfCommonSpecies = new HashSet<Vertex>();
		// setOfReactantsOfCommonSpecies.addAll(setOfReactantsOfFirstReaction);
		// setOfReactantsOfCommonSpecies.addAll(setOfReactantsOfSecondReaction);

		reactantsCheckingMap.put(vR1, setOfProductsOfFirstReaction);
		reactantsCheckingMap.put(vR2, setOfProductsOfFirstReaction);
		reactantsCheckingMap.put(vR3, setOfProductsOfCommonSpecies);
		reactantsCheckingMap.put(vR4, setOfProductsOfSecondReaction);

		productsCheckingMap.put(vP1, setOfReactantsOfFirstReaction);
		productsCheckingMap.put(vP2, setOfReactantsOfSecondReaction);
		productsCheckingMap.put(vP3, setOfReactantsOfSecondReaction);

		Reaction reaction = sbmlModel.createReaction("reaction1_id");
		reaction.setReversible(true);

		// add a test in the learning test to keep the species reference
		reaction.createReactant(reactant1);
		reaction.createReactant(reactant2);
		reaction.createReactant(reactant3);
		reaction.createProduct(product1);

		Reaction anotherReaction = sbmlModel.createReaction("reaction2_id");
		anotherReaction.setReversible(true);

		// add a test in the learning test to keep the species reference
		anotherReaction.createReactant(reactant3);
		anotherReaction.createReactant(reactant4);
		anotherReaction.createProduct(product2);
		anotherReaction.createProduct(product3);

		final Set<Vertex> reactants = new HashSet<Vertex>();
		final Set<Vertex> products = new HashSet<Vertex>();

		Set<Vertex> vertices = connector.readReactions(
				sbmlModel.getListOfReactions(),
				new VertexHandlingWithSourceListener() {

					@Override
					public void vertexHandled(Vertex vertex) {
					}

					@Override
					public void reactantVertexHandled(Vertex vertex) {
						reactants.add(vertex);
					}

					@Override
					public void productVertexHandled(Vertex vertex) {
						products.add(vertex);
					}
				});

		assertEquals(7, vertices.size());

		assertEquals(4, reactants.size());
		assertEquals(3, products.size());

		for (Vertex vertex : reactants) {
			assertTrue(vertex.isYourNeighborhoodEquals(reactantsCheckingMap
					.get(vertex)));
		}

		for (Vertex vertex : products) {
			assertTrue(vertex.isYourNeighborhoodEquals(productsCheckingMap
					.get(vertex)));
		}
	}
}
