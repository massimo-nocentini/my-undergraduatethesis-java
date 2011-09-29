package model;

import java.io.Writer;

import dotInterface.DotDecorationApplier;

public interface VertexFormatter {
	VertexFormatter formatVertexDefinitionInto(Writer writer, Vertex vertex,
			DotDecorationApplier useDecorationApplier);

	VertexFormatter formatEdgeDefinitionInto(Writer writer, Vertex source,
			Vertex neighbour, DotDecorationApplier dotDecorationApplier);
}
