package JSBMLInterface;

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
	public void importOneToOneReaction() {
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

		Vertex a = Vertex.makeVertex(idReactant);
		Vertex b = Vertex.makeVertex(idProduct);

		// assertTrue(vertices.contains(a));
		// assertTrue(vertices.contains(b));
		// assertEquals(2, vertices.size());

	}

}
