package JSBMLInterface;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import model.Vertex;

import org.junit.Test;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.Species;

public class ConnectorUnitTest {

	@Test
	public void creation() {
		Connector connector = Connector.makeConnector();
		assertNotNull(connector);
	}

	@Test
	public void checkVerticesCreationBasedOnOneToOneReaction() {
		Connector connector = Connector.makeConnector();
		Model sbmlModel = new Model();

		String idReactant = "id1";
		String idProduct = "id2";
		Species reactant = sbmlModel.createSpecies(idReactant);
		Species product = sbmlModel.createSpecies(idProduct);

		Reaction reaction = sbmlModel.createReaction("reaction_id");
		// add a test in the learning test to keep the species reference
		reaction.createReactant(reactant);
		reaction.createProduct(product);

		Set<Vertex> vertices = connector.readReaction(reaction);

		Vertex a = Vertex.makeVertex();
		Vertex b = Vertex.makeVertex();
		Vertex outSide = Vertex.makeVertex();

		// assertEquals(2, vertices.size());
		// assertTrue(vertices.contains(a));
		// assertTrue(vertices.contains(b));
		// assertFalse(vertices.contains(outSide));

	}

	@Test
	public void convertListOfSpeciesReferenceInSetOfVertices() {
		Connector connector = Connector.makeConnector();
		Model sbmlModel = new Model();

		Species reactant1 = sbmlModel.createSpecies("id1");
		Species reactant2 = sbmlModel.createSpecies("id2");

		Reaction reaction = sbmlModel.createReaction("reaction_id");

		// I'll use the reactants list just because I want a list of
		// species references to work with
		reaction.createReactant(reactant1);
		reaction.createReactant(reactant2);

		Set<Vertex> vertices = connector.convertToVertexSet(reaction
				.getListOfReactants());

		Vertex a = Vertex.makeVertex();
		Vertex b = Vertex.makeVertex();
		Vertex outSide = Vertex.makeVertex();

		assertEquals(2, vertices.size());
		// assertTrue(vertices.contains(a));
		// assertTrue(vertices.contains(b));
		// assertFalse(vertices.contains(outSide));

	}

}
