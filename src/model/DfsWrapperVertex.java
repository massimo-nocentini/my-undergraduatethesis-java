package model;

import java.io.IOException;
import java.io.Writer;

import dotInterface.DotExporter;
import dotInterface.DotFileUtilHandler;

public class DfsWrapperVertex extends WrapperVertex {

	private int preVisitClock;
	private int postVisitClock;

	public DfsWrapperVertex previsitedAt(int instant) {
		preVisitClock = instant;
		return this;
	}

	public boolean isYourPreVisitClock(int otherClock) {
		return preVisitClock == otherClock;
	}

	public DfsWrapperVertex postvisitedAt(int other) {
		postVisitClock = other;
		return this;
	}

	public boolean isYourPostVisitClock(int otherClock) {
		return postVisitClock == otherClock;
	}

	DfsWrapperVertex(Vertex wrappingVertex) {
		super(wrappingVertex);
	}

	public String yourDfsIntervalToString() {

		return "(".concat(String.valueOf(preVisitClock)).concat(", ")
				.concat(String.valueOf(postVisitClock)).concat(")");
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
					.composeVertexLabelOutsideBox(yourDfsIntervalToString())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}