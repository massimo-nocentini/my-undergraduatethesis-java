package dotInterface;

import java.util.HashSet;
import java.util.Set;

public class SimpleExporter implements DotExporter {

	Set<String> verticesDefinitionDotRepresentation;

	public SimpleExporter() {
		verticesDefinitionDotRepresentation = new HashSet<String>();
	}

	@Override
	public Set<String> getGraphDotBody() {
		Set<String> result = new HashSet<String>();

		result.addAll(verticesDefinitionDotRepresentation);

		return result;
	}

	@Override
	public void buildVertexDefinition(
			VertexDotInfoProvider vertexDotInfoProvider) {

		verticesDefinitionDotRepresentation.add(vertexDotInfoProvider
				.provideId());
	}

	@Override
	public String getCompleteContent() {

		StringBuilder result = new StringBuilder();

		result.append("digraph G {" + DotFileUtilHandler.getNewLineSeparator());

		collectSetOfElementsInto(result, verticesDefinitionDotRepresentation);

		result.append("}");

		return result.toString();
	}

	private void collectSetOfElementsInto(StringBuilder result,
			Set<String> elements) {
		for (String dotElement : elements) {
			result.append(dotElement.concat(";").concat(
					DotFileUtilHandler.getNewLineSeparator()));
		}
	}

	@Override
	public String toString() {
		return this.getCompleteContent();
	}

}
