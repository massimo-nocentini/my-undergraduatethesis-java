package dotInterface;

import java.io.Writer;

public interface DotExporter extends DotDocumentPartHandler {

	DotExporter buildVertexDefinition(VertexDotInfoProvider vertexInfo);

	DotExporter buildEdgeDefinition(Edge vertexInfo);

	DotExporter collectCompleteContent(Writer outputPlugObject);

	// TODO: doesn't seem this class is the right home for this method.
	String decoreWithSourceSinkAttributes(String string);

	String buildInfixNeighborRelation(String string);
}
