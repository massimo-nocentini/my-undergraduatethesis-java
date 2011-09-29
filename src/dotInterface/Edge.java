package dotInterface;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import model.Vertex;

public class Edge {

	private final Vertex vertex;
	private final Vertex neighbour;

	public Edge(Vertex vertex, Vertex neighbour) {
		this.vertex = vertex;
		this.neighbour = neighbour;
	}

	public static Edge makeEdge(Vertex vertex, Vertex neighbour) {

		return new Edge(vertex, neighbour);
	}

	public Edge collectEdgeInto(StringBuilder edgeDefinition,
			DotDecorationApplier dotDecorationApplier) {

		Writer writer = new StringWriter();

		vertex.useFormatter().formatEdgeDefinitionInto(writer, vertex,
				neighbour, dotDecorationApplier);

		try {
			writer.close();
			edgeDefinition.append(writer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return this;
	}
}
