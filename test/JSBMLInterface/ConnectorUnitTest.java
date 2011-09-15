package JSBMLInterface;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.HashSet;
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
				new VertexGenerationListener() {

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
}
