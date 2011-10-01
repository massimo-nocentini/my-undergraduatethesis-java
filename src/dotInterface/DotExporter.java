package dotInterface;

import java.io.Writer;

import model.Vertex;

public interface DotExporter extends DotDocumentPartHandler {

	DotExporter buildVertexDefinition(Vertex vertex);

	DotExporter collectCompleteContent(Writer outputPlugObject);

	DotExporter buildEdgeDefinition(Vertex source, Vertex neighbour);
}
