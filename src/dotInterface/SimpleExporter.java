package dotInterface;

public class SimpleExporter implements DotExporter {

	StringBuilder verticesDefinitionDotRepresentation;

	public SimpleExporter() {
		verticesDefinitionDotRepresentation = new StringBuilder();
	}

	@Override
	public String getOutput() {
		return verticesDefinitionDotRepresentation.toString();
	}

	@Override
	public void buildVertexDefinition(
			VertexDotInfoProvider vertexDotInfoProvider) {

		String id = vertexDotInfoProvider.provideId();

		verticesDefinitionDotRepresentation.append(id);
	}

}
