package dotInterface;

import java.io.Writer;

import model.Vertex;

public interface DotExporter extends DotDocumentPartHandler {

	DotExporter buildVertexDefinition(Vertex vertex);

	DotExporter buildEdgeDefinition(Edge vertexInfo);

	DotExporter collectCompleteContent(Writer outputPlugObject);

	DotDecorationApplier useDecorationApplier();

}
