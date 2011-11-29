package dotInterface;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Set;

import model.Vertex;

public class SimpleExporter implements DotExporter, DotDocumentPartHandler,
		DotDocumentPartHandlerTestingBehaviour {

	private final Set<String> verticesDefinitionDotRepresentation;
	private final Set<String> verticesLabelsDefinitionDotRepresentation;
	private final Set<String> generalSettingsDotRepresentation;
	private final Set<String> edgeDefinitionDotRepresentation;

	public SimpleExporter() {

		// using a LinkedHashSet we can preserve the addition order of the
		// element that is currently inserted into the container.
		// TODO: for a complete ordering should be nice override the method
		// compareTo in the DfsWrapperVertex in order to have a fixed and
		// deterministic order (in this way the order works only because we use
		// a TreeSet to store the neighbors relation on each vertex and
		// the dfs motor use the standard iterator to explore these neighbors.
		verticesDefinitionDotRepresentation = new LinkedHashSet<String>();
		generalSettingsDotRepresentation = new LinkedHashSet<String>();
		edgeDefinitionDotRepresentation = new LinkedHashSet<String>();
		verticesLabelsDefinitionDotRepresentation = new LinkedHashSet<String>();

		initGeneralSettings();
	}

	private void initGeneralSettings() {
		generalSettingsDotRepresentation.add("ratio=1");
		generalSettingsDotRepresentation.add("center = true");
		generalSettingsDotRepresentation
				.add("edge [arrowsize=.5, weight=.1, color=\"gray\", fontsize=8]");
		generalSettingsDotRepresentation
				.add("node [label=\"\",shape=circle,height=0.12,width=0.12,fontsize=1]");
		// .add("node [shape=circle]");
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

	private DotDocumentPartHandler collectSetOfElementsInto(
			Writer outputPlugObject, Set<String> elements) {

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
	public DotDocumentPartHandler collectVertexDefinitionPart(
			Writer outputPlugObject) {

		collectSetOfElementsInto(outputPlugObject,
				verticesDefinitionDotRepresentation);
		return this;
	}

	@Override
	public DotDocumentPartHandler collectGeneralSettingsPart(
			Writer outputPlugObject) {

		collectSetOfElementsInto(outputPlugObject,
				generalSettingsDotRepresentation);

		return this;
	}

	@Override
	public DotDocumentPartHandler collectEdgeDefinitionPart(
			Writer outputPlugObject) {

		collectSetOfElementsInto(outputPlugObject,
				edgeDefinitionDotRepresentation);
		return this;
	}

	@Override
	public DotDocumentPartHandler collectVertexLabelOutsideBoxPart(
			Writer outputPlugObject) {

		collectSetOfElementsInto(outputPlugObject,
				verticesLabelsDefinitionDotRepresentation);

		return this;
	}

	@Override
	public boolean isVertexDefinitionPartEquals(Set<String> otherPart) {
		return verticesDefinitionDotRepresentation.equals(otherPart);
	}

	@Override
	public boolean isEdgeDefinitionPartEquals(Set<String> part) {
		return edgeDefinitionDotRepresentation.equals(part);
	}

	@Override
	public boolean isVertexLabelOutsideBoxPartEquals(Set<String> part) {
		return verticesLabelsDefinitionDotRepresentation.equals(part);
	}

	@Override
	public DotExporter buildEdgeDefinition(Vertex source, Vertex neighbour) {

		try {
			Writer writer = new StringWriter();
			source.collectEdgeDefinitionInto(writer, neighbour);
			writer.close();
			edgeDefinitionDotRepresentation.add(writer.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}

		return this;
	}

	@Override
	public DotExporter buildVertexDefinition(Vertex vertex) {

		try {
			Writer writer = new StringWriter();
			vertex.collectYourDefinitionInto(writer);
			writer.close();
			verticesDefinitionDotRepresentation.add(writer.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}

		return this;
	}

	@Override
	public DotExporter buildVertexLabelOutsideBoxDefinition(Vertex vertex) {
		try {
			Writer writer = new StringWriter();
			vertex.collectVertexLabelOutsideBoxInto(writer);
			writer.close();
			verticesLabelsDefinitionDotRepresentation.add(writer.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}

		return this;
	}

	@Override
	public DotExporter collectCompleteContent(Writer outputPlugObject) {

		// for clarity I cast the object itself to be focus on the task to
		// collect part informations.
		DotDocumentPartHandler dotDocumentPartHandler = this;

		dotDocumentPartHandler.collectGeneralSettingsPart(outputPlugObject);
		dotDocumentPartHandler.collectEdgeDefinitionPart(outputPlugObject);
		dotDocumentPartHandler.collectVertexDefinitionPart(outputPlugObject);
		dotDocumentPartHandler
				.collectVertexLabelOutsideBoxPart(outputPlugObject);

		return this;
	}
}
