package dotInterface;

public class Edge {

	private final VertexDotInfoProvider vertex;
	private final VertexDotInfoProvider neighbour;

	public Edge(VertexDotInfoProvider vertex, VertexDotInfoProvider neighbour) {
		this.vertex = vertex;
		this.neighbour = neighbour;
	}

	public static Edge makeEdge(VertexDotInfoProvider vertex,
			VertexDotInfoProvider neighbour) {

		return new Edge(vertex, neighbour);
	}

	public Edge collectEdgeInto(StringBuilder edgeDefinition,
			DotDecorationApplier dotDecorationApplier) {

		String composedString = dotDecorationApplier
				.buildInfixNeighborhoodRelation(vertex.provideId(),
						neighbour.provideId());

		edgeDefinition.append(composedString);

		return this;
	}
}
