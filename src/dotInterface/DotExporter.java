package dotInterface;

import java.io.Writer;

public interface DotExporter extends DotDocumentPartHandler {

	DotExporter buildVertexDefinition(VertexDotInfoProvider vertexInfo);

	DotExporter collectCompleteContent(Writer outputPlugObject);
}
