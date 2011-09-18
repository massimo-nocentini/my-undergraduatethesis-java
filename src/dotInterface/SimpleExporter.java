package dotInterface;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

public class SimpleExporter implements DotExporter {

	private Set<String> verticesDefinitionDotRepresentation;
	private Set<String> generalSettingsDotRepresentation;
	private Set<String> edgeDefinitionDotRepresentation;

	public SimpleExporter() {
		verticesDefinitionDotRepresentation = new HashSet<String>();
		generalSettingsDotRepresentation = new HashSet<String>();
		edgeDefinitionDotRepresentation = new HashSet<String>();

		initGeneralSettings();
	}

	@Override
	public DotExporter buildVertexDefinition(
			VertexDotInfoProvider vertexDotInfoProvider) {

		String vertexRepresentation = vertexDotInfoProvider.provideId();

		if (vertexDotInfoProvider.getVertexInstance().isSink()
				|| vertexDotInfoProvider.getVertexInstance().isSource()) {

			vertexRepresentation = decoreWithSourceSinkAttributes(vertexRepresentation);
		}

		verticesDefinitionDotRepresentation.add(vertexRepresentation);

		return this;
	}

	@Override
	public String decoreWithSourceSinkAttributes(String string) {
		return string.concat(" [color=\"black\", style=filled]");
	}

	private DotExporter collectSetOfElementsInto(Writer outputPlugObject,
			Set<String> elements) {
		for (String dotElement : elements) {
			try {
				outputPlugObject.append(dotElement);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return this;
	}

	@Override
	public String toString() {
		Writer stringWriter = new StringWriter();
		collectCompleteContent(EndLineFillerWriter.MakeWrapper(stringWriter));
		try {
			stringWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stringWriter.toString();
	}

	@Override
	public boolean isVertexDefinitionPartEquals(Set<String> otherPart) {
		return verticesDefinitionDotRepresentation.equals(otherPart);
	}

	@Override
	public DotDocumentPartHandler collectVertexDefinitionPart(
			Writer outputPlugObject) {

		collectSetOfElementsInto(outputPlugObject,
				verticesDefinitionDotRepresentation);
		return this;
	}

	@Override
	public DotExporter collectCompleteContent(Writer outputPlugObject) {

		// for clarity I cast the object itself to be focus on the task to
		// collect part informations.
		DotDocumentPartHandler dotDocumentPartHandler = (DotDocumentPartHandler) this;

		dotDocumentPartHandler.collectGeneralSettingsPart(outputPlugObject);
		dotDocumentPartHandler.collectEdgeDefinitionPart(outputPlugObject);
		dotDocumentPartHandler.collectVertexDefinitionPart(outputPlugObject);

		return this;
	}

	@Override
	public DotDocumentPartHandler collectGeneralSettingsPart(
			Writer outputPlugObject) {

		collectSetOfElementsInto(outputPlugObject,
				generalSettingsDotRepresentation);
		return this;
	}

	private void initGeneralSettings() {
		generalSettingsDotRepresentation.add("ratio=1");
		generalSettingsDotRepresentation.add("center = true");
		generalSettingsDotRepresentation
				.add("edge [arrowsize=.5, weight=.1, color=\"gray\"]");
		generalSettingsDotRepresentation
				.add("node [label=\"\",shape=circle,height=0.12,width=0.12,fontsize=1]");
	}

	@Override
	public DotDocumentPartHandler collectEdgeDefinitionPart(
			Writer outputPlugObject) {

		collectSetOfElementsInto(outputPlugObject,
				edgeDefinitionDotRepresentation);
		return this;
	}

	@Override
	public boolean isEdgeDefinitionPartEquals(Set<String> part) {
		return edgeDefinitionDotRepresentation.equals(part);
	}

	@Override
	public DotExporter buildEdgeDefinition(Edge edge) {
		StringBuilder edgeDefinition = new StringBuilder();

		edge.collectEdgeInto(edgeDefinition);

		edgeDefinitionDotRepresentation.add(edgeDefinition.toString());

		return this;
	}

	@Override
	public String buildInfixNeighborRelation(String string) {
		// TODO Auto-generated method stub
		return null;
	}

}
