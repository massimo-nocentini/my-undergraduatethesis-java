package dotInterface;

import junit.framework.Assert;
import model.SimpleVertex;
import model.SimpleVertex.SourceSinkLineDecorator;
import model.SimpleVertex.SquareBracketsLineDecorator;
import model.Vertex;
import model.VertexFactory;

import org.junit.Test;

public class LineDecoratorUnitTest {

	@Test
	public void identifierLineDecoration() {

		String species_id = "species_id";
		String compartment_id = "compartment_id";
		Vertex vertex = VertexFactory.makeSimpleVertex(species_id, compartment_id);

		Assert.assertEquals(SimpleVertex.IdentifierLineDecorator.class, vertex
				.getIdentifierDecorator().getClass());

		Assert.assertEquals(
				SimpleVertex.composeIdentifier(species_id, compartment_id),
				vertex.getIdentifierDecorator().decore(""));
	}

	@Test
	public void sourceSinkLineDecoration() {

		String species_id = "species_id";
		String compartment_id = "compartment_id";
		Vertex vertex = VertexFactory.makeSimpleVertex(species_id, compartment_id);

		Vertex neighbour = VertexFactory.makeSimpleVertex();
		vertex.addNeighbour(neighbour);

		LineDecorator sourceDecorator = vertex.getSourceDecorator();

		Assert.assertEquals(SimpleVertex.SourceSinkLineDecorator.class,
				sourceDecorator.getClass());

		Assert.assertEquals(SimpleVertex.getSourceSinkToken(),
				sourceDecorator.decore(""));

		Assert.assertEquals(SimpleVertex.SourceSinkLineDecorator.class,
				neighbour.getSourceDecorator().getClass());

	}

	@Test
	public void noSourceSinkLineDecoration() {

		String species_id = "species_id";
		String compartment_id = "compartment_id";

		Vertex vertex = VertexFactory.makeSimpleVertex(species_id, compartment_id);

		vertex.addNeighbour(vertex);

		LineDecorator sourceDecorator = vertex.getSourceDecorator();

		Assert.assertFalse(SourceSinkLineDecorator.class.equals(sourceDecorator
				.getClass()));

		Assert.assertEquals(NullObjectLineDecorator.class,
				sourceDecorator.getClass());

		String empty = "";
		Assert.assertEquals(empty, sourceDecorator.decore(empty));

	}

	@Test
	public void openingClosingSquaredParanthesisEmptyInputLineDecoration() {

		String species_id = "species_id";
		String compartment_id = "compartment_id";

		Vertex vertex = VertexFactory.makeSimpleVertex(species_id, compartment_id);

		vertex.addNeighbour(vertex);

		LineDecorator squareBracketsDecorator = vertex
				.getSquareBracketsDecorator();

		Assert.assertTrue(SquareBracketsLineDecorator.class
				.equals(squareBracketsDecorator.getClass()));

		String someString = "";
		Assert.assertEquals(someString,
				squareBracketsDecorator.decore(someString));

	}

	@Test
	public void openingClosingSquaredParanthesisNonEmptyInputLineDecoration() {

		String species_id = "species_id";
		String compartment_id = "compartment_id";

		Vertex vertex = VertexFactory.makeSimpleVertex(species_id, compartment_id);

		vertex.addNeighbour(vertex);

		LineDecorator squareBracketsDecorator = vertex
				.getSquareBracketsDecorator();

		Assert.assertTrue(SquareBracketsLineDecorator.class
				.equals(squareBracketsDecorator.getClass()));

		String someString = "someString";
		Assert.assertEquals(
				DotFileUtilHandler
						.getOpeningDotDecorationString()
						.concat(someString)
						.concat(DotFileUtilHandler
								.getClosingDotDecorationString()),
				squareBracketsDecorator.decore(someString));

	}

}
