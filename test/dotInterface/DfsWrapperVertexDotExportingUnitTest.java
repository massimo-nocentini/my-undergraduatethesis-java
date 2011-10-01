package dotInterface;

import java.io.StringWriter;
import java.io.Writer;

import junit.framework.Assert;
import model.SimpleVertex;
import model.Vertex;
import model.VertexFactory;

import org.junit.Test;

public class DfsWrapperVertexDotExportingUnitTest {

	@Test
	public void test() {
		String species_id = "species_id";
		String compartment_id = "compartment_id";
		Vertex vertex = VertexFactory.makeSimpleVertex(species_id,
				compartment_id);

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

}
