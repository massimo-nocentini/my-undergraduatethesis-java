package JSBMLInterface;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import model.OurModel;
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

		final Set<Vertex> collectedVertices = new HashSet<Vertex>();

		Vertex outSide = Vertex.makeVertex();

		Set<Vertex> vertices = connector.convertToVertexSet(
				reaction.getListOfReactants(), new HashMap<Vertex, Vertex>(),
				new VertexHandlingListener() {

					@Override
					public void vertexHandled(Vertex vertex) {
						collectedVertices.add(vertex);
					}
				});

		// here I repeat the control on the size because I'm using a different
		// overload of the test below.
		assertEquals(2, vertices.size());
		assertEquals(2, collectedVertices.size());

		assertEquals(collectedVertices, vertices);
		assertFalse(vertices.contains(outSide));

	}

	@Test
	public void parseModel() {
		String path = "sbml-test-files/allCpdsMetabSmmReactionsCompounds.xml";

		Connector connector = Connector.makeConnector(path).readModel();

		Assert.assertTrue(connector.hadYouReadSuccessfully());
	}

	@Test
	public void parseModelInducingErrors() {
		String path = null;

		Connector connector = Connector.makeConnector(path).readModel();

		Assert.assertFalse(connector.hadYouReadSuccessfully());
	}

	@Test
	public void parseModelInducingErrorsExceptionTest() {
		String path = null;
		Connector connector = Connector.makeConnector(path).readModel();
		try {
			connector.parseModel();
			Assert.fail("Impossible to had successfully read a SBML model from nowhere.");
		} catch (NullPointerException e) {
			Assert.assertFalse(connector.hadYouReadSuccessfully());
		}
	}

	@Test
	public void makeOurModel() {
		String path = "sbml-test-files/allCpdsMetabSmmReactionsCompounds.xml";

		OurModel model = OurModel.makeOurModel(path);

		Assert.assertNotNull(model);
		Assert.assertFalse(model.isEmpty());
	}

	@Test
	public void makeOurModelInducingError() {
		String path = null;

		try {
			@SuppressWarnings("unused")
			OurModel model = OurModel.makeOurModel(path);
			Assert.fail("Impossible to had successfully create a OurModel object from nowhere.");
		} catch (NullPointerException e) {
		}
	}
}
