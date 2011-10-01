package dotInterface;

import java.io.StringWriter;
import java.io.Writer;

import junit.framework.Assert;
import model.SimpleVertex;
import model.Vertex;

import org.junit.Test;

public class SimpleVertexDotExportingUnitTest {

	@Test
	public void VertexDefinitionWithoutLabel() {

		String species_id = "species_id";
		String compartment_id = "compartment_id";
		Vertex vertex = SimpleVertex.makeVertex(species_id, compartment_id);

		Writer writer = new StringWriter();
		vertex.collectYourDefinitionInto(writer);

		Assert.assertEquals(
				SimpleVertex
						.composeIdentifier(species_id, compartment_id)
						.concat(DotFileUtilHandler.getBlankString())
						.concat(SimpleVertex
								.composeSquareBracketsWrapping(SimpleVertex
										.getSourceSinkToken())), writer
						.toString());
	}

	@Test
	public void edgeDefinitionWithoutLabel() {

		String species_id = "species_id";
		String compartment_id = "compartment_id";
		Vertex vertex = SimpleVertex.makeVertex(species_id, compartment_id);

		String neighbourSpeciesId = "neighbour_species_id";
		Vertex neighbour = SimpleVertex.makeVertex(neighbourSpeciesId,
				compartment_id);

		vertex.addNeighbour(neighbour);

		Writer writer = new StringWriter();
		vertex.collectEdgeDefinitionInto(writer, neighbour);

		Assert.assertEquals(SimpleVertex.composeNeighbourRelationInfixRelation(
				SimpleVertex.composeIdentifier(species_id, compartment_id),
				SimpleVertex.composeIdentifier(neighbourSpeciesId,
						compartment_id)), writer.toString());
	}
}
