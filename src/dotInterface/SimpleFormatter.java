package dotInterface;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import model.Vertex;
import model.VertexFormatter;

public class SimpleFormatter implements VertexFormatter {

	public SimpleFormatter() {
	}

	@Override
	public VertexFormatter formatVertexDefinitionInto(Writer writer,
			Vertex vertex, DotDecorationApplier useDecorationApplier) {

		Writer sourceIdentifierStringBuilder = new StringWriter();

		vertex.collectYourIdentifierInto(sourceIdentifierStringBuilder);

		String vertexRepresentation = sourceIdentifierStringBuilder.toString();

		if (vertex.isSink() || vertex.isSource()) {

			vertexRepresentation = useDecorationApplier
					.decoreWithSourceSinkAttributes(vertexRepresentation);
		}

		try {
			writer.append(vertexRepresentation);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return this;
	}

	@Override
	public VertexFormatter formatEdgeDefinitionInto(Writer writer,
			Vertex source, Vertex neighbour,
			DotDecorationApplier dotDecorationApplier) {

		Writer sourceIdentifierStringBuilder = new StringWriter();
		Writer neighbourIdentifierStringBuilder = new StringWriter();

		source.collectYourIdentifierInto(sourceIdentifierStringBuilder);

		neighbour.collectYourIdentifierInto(neighbourIdentifierStringBuilder);

		String composedString = dotDecorationApplier
				.buildInfixNeighborhoodRelation(
						sourceIdentifierStringBuilder.toString(),
						neighbourIdentifierStringBuilder.toString());

		try {
			writer.append(composedString);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return this;
	}

}
