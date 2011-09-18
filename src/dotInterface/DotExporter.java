package dotInterface;

import java.io.Writer;

public interface DotExporter extends DotDocumentPartHandler {

	DotExporter buildVertexDefinition(VertexDotInfoProvider vertexInfo);

	DotExporter buildEdgeDefinition(Edge vertexInfo);

	DotExporter collectCompleteContent(Writer outputPlugObject);

	DotDecorationApplier useDecorationApplier();
}
