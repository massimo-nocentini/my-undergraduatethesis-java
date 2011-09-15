package JSBMLInterface;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import model.Vertex;

import org.junit.Test;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.Species;

public class ReadingNonReversibleReactionsUnitTest {

	@Test
	public void checkVerticesCreationBasedOnOneToOneReaction() {
		Connector connector = Connector.makeConnector();

		Model sbmlModel = new Model();

		Compartment compartment = sbmlModel.createCompartment("compartment_id");

		String idReactant = "id1";
		String idProduct = "id2";
		Species reactant = sbmlModel.createSpecies(idReactant, compartment);
		Species product = sbmlModel.createSpecies(idProduct, compartment);

		Reaction reaction = sbmlModel.createReaction("reaction_id");
		reaction.setReversible(false);

		// add a test in the learning test to keep the species reference
		reaction.createReactant(reactant);
		reaction.createProduct(product);

		final Set<Vertex> reactants = new HashSet<Vertex>();
		final Set<Vertex> products = new HashSet<Vertex>();

		Set<Vertex> vertices = connector.readReaction(reaction,
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

		assertEquals(2, vertices.size());

		assertEquals(1, reactants.size());
		assertEquals(1, products.size());

		for (Vertex vertex : reactants) {
			assertTrue(vertex.isYourNeighborhoodEquals(products));
		}

		for (Vertex vertex : products) {
			assertTrue(vertex.isYourNeighborhoodEmpty());
		}
	}

	@Test
	public void checkVerticesCreationBasedOnManyToManyReaction() {
		Connector connector = Connector.makeConnector();

		Model sbmlModel = new Model();

		Compartment compartment = sbmlModel.createCompartment("compartment_id");

		String idReactant = "id_Reactor_";
		String idProduct = "id_Product_";
		Species reactant1 = sbmlModel.createSpecies(idReactant + "1",
				compartment);
		Species reactant2 = sbmlModel.createSpecies(idReactant + "2",
				compartment);
		Species product1 = sbmlModel
				.createSpecies(idProduct + "1", compartment);
		Species product2 = sbmlModel
				.createSpecies(idProduct + "2", compartment);
		Species product3 = sbmlModel
				.createSpecies(idProduct + "3", compartment);

		Reaction reaction = sbmlModel.createReaction("reaction_id");
		reaction.setReversible(false);

		// add a test in the learning test to keep the species reference
		reaction.createReactant(reactant1);
		reaction.createReactant(reactant2);
		reaction.createProduct(product1);
		reaction.createProduct(product2);
		reaction.createProduct(product3);

		final Set<Vertex> reactants = new HashSet<Vertex>();
		final Set<Vertex> products = new HashSet<Vertex>();

		final Set<Vertex> collectedOneByOneVertices = new HashSet<Vertex>();
		Set<Vertex> vertices = connector.readReaction(reaction,
				new VertexHandlingWithSourceListener() {

					@Override
					public void vertexHandled(Vertex vertex) {
						collectedOneByOneVertices.add(vertex);
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

		assertEquals(5, vertices.size());
		assertEquals(collectedOneByOneVertices, vertices);

		assertEquals(2, reactants.size());
		assertEquals(3, products.size());

		for (Vertex vertex : reactants) {
			assertTrue(vertex.isYourNeighborhoodEquals(products));
		}

		for (Vertex vertex : products) {
			assertTrue(vertex.isYourNeighborhoodEmpty());
		}
	}

	@Test
	public void checkVerticesCreationBasedOnManyToManyReactionWithOneSpeciesInCommon() {
		Connector connector = Connector.makeConnector();

		Model sbmlModel = new Model();

		Compartment compartment = sbmlModel.createCompartment("compartment_id");

		String idReactant = "id_Reactor_";
		String idProduct = "id_Product_";
		Species reactant1 = sbmlModel.createSpecies(idReactant + "1",
				compartment);
		Species commonSpecies = sbmlModel.createSpecies(idReactant + "2",
				compartment);
		Species product1 = sbmlModel
				.createSpecies(idProduct + "1", compartment);
		Species product2 = sbmlModel
				.createSpecies(idProduct + "2", compartment);
		Species product3 = sbmlModel
				.createSpecies(idProduct + "3", compartment);

		Reaction reaction = sbmlModel.createReaction("reaction_id");
		reaction.setReversible(false);

		// add a test in the learning test to keep the species reference
		reaction.createReactant(reactant1);
		reaction.createReactant(commonSpecies);
		reaction.createProduct(commonSpecies);
		reaction.createProduct(product1);
		reaction.createProduct(product2);
		reaction.createProduct(product3);

		final Set<Vertex> reactants = new HashSet<Vertex>();
		final Set<Vertex> products = new HashSet<Vertex>();

		final Set<Vertex> collectedOneByOneVertices = new HashSet<Vertex>();
		Set<Vertex> vertices = connector.readReaction(reaction,
				new VertexHandlingWithSourceListener() {

					@Override
					public void vertexHandled(Vertex vertex) {
						collectedOneByOneVertices.add(vertex);
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

		assertEquals(5, vertices.size());
		assertEquals(collectedOneByOneVertices, vertices);

		assertEquals(2, reactants.size());
		assertEquals(4, products.size());

		for (Vertex vertex : reactants) {
			assertTrue(vertex.isYourNeighborhoodEquals(products));
		}

		// TODO: should be useful to ask a vertex if it match some
		// species instead of checking every time the condition below.
		for (Vertex vertex : products) {

			if (vertex.isYourOrigin(commonSpecies)) {
				assertFalse(vertex.isYourNeighborhoodEmpty());
				continue;
			}

			assertTrue(vertex.isYourNeighborhoodEmpty());
		}
	}

	@Test
	public void checkVertexSelfLoopInOneToOneReaction() {
		Connector connector = Connector.makeConnector();

		Model sbmlModel = new Model();

		Compartment compartment = sbmlModel.createCompartment("compartment_id");

		Species species = sbmlModel.createSpecies("id1", compartment);

		Reaction reaction = sbmlModel.createReaction("reaction_id");
		reaction.setReversible(false);

		// add a test in the learning test to keep the species reference
		reaction.createReactant(species);
		reaction.createProduct(species);

		final Set<Vertex> reactants = new HashSet<Vertex>();
		final Set<Vertex> products = new HashSet<Vertex>();
		final Map<Vertex, Integer> handledVertices = new HashMap<Vertex, Integer>();

		Set<Vertex> vertices = connector.readReaction(reaction,
				new VertexHandlingWithSourceListener() {

					@Override
					public void vertexHandled(Vertex vertex) {
						if (handledVertices.containsKey(vertex) == false) {
							handledVertices.put(vertex, 0);
						}

						Integer previousCount = handledVertices.get(vertex);
						handledVertices.put(vertex,
								previousCount.intValue() + 1);
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

		assertEquals(1, vertices.size());

		assertEquals(handledVertices.keySet(), vertices);

		// I consider only the first argument of the array because by the line
		// above the vertices set has exactly one element.
		assertEquals(2, handledVertices.get((Vertex) (vertices.toArray()[0]))
				.intValue());

		assertEquals(1, reactants.size());
		assertEquals(1, products.size());

		for (Vertex vertex : reactants) {
			assertTrue(vertex.isYourNeighborhoodEquals(products));
		}

		for (Vertex vertex : products) {
			assertFalse(vertex.isYourNeighborhoodEmpty());
		}

		assertTrue(((Vertex) (reactants.toArray()[0])).haveYouSelfLoop());
	}
}
