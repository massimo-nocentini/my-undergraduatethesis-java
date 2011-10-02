package model;

import java.io.IOException;
import java.io.Writer;

import dotInterface.DotExporter;
import dotInterface.DotFileUtilHandler;

abstract class VertexWithLabelWrapperVertex extends WrapperVertex {

	protected VertexWithLabelWrapperVertex(Vertex wrappingVertex) {
		super(wrappingVertex);
	}

	@Override
	public void acceptExporter(DotExporter exporter) {
		super.acceptExporter(exporter);
		exporter.buildVertexLabelOutsideBoxDefinition(this);
	}

	@Override
	public void collectVertexLabelOutsideBoxInto(Writer writer) {
		try {
			super.collectVertexLabelOutsideBoxInto(writer);

			collectEdgeDefinitionInto(writer, this);

			writer.append(DotFileUtilHandler.getBlankString());

			writer.append(DotFileUtilHandler.composeSquareBracketsWrapping(DotFileUtilHandler
					.composeVertexLabelOutsideBox(provideOutsideLabel())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected abstract String provideOutsideLabel();

}
