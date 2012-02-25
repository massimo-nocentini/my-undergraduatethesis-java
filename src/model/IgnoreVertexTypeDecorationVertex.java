package model;

import dotInterface.DotExporter;
import dotInterface.LineDecorator;

public class IgnoreVertexTypeDecorationVertex extends WrapperVertex {

	private final LineDecorator line_decorator;

	public IgnoreVertexTypeDecorationVertex(Vertex wrappingVertex,
			LineDecorator line_decorator) {

		super(wrappingVertex);
		this.line_decorator = line_decorator;

	}

	@Override
	public void acceptExporter(final DotExporter exporter) {

		exporter.buildVertexDefinition(this);

		this.doOnNeighbors(new VertexLogicApplier() {

			@Override
			public void apply(Vertex vertex) {

				exporter.buildEdgeDefinition(
						IgnoreVertexTypeDecorationVertex.this, vertex);
			}
		});

	}

	@Override
	public LineDecorator getSourceDecorator() {

		return line_decorator;
	}

}
