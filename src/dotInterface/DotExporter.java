package dotInterface;

import java.io.Writer;

import model.Vertex;
import model.VertexFormatter;

public interface DotExporter extends DotDocumentPartHandler {

	DotExporter buildVertexDefinition(Vertex vertex);

	// TODO: get rid of this method and the Edge class
	DotExporter buildEdgeDefinition(Edge vertexInfo);

	DotExporter collectCompleteContent(Writer outputPlugObject);

	// TODO: the following two methods should be deleted
	DotDecorationApplier useDecorationApplier();

	VertexFormatter getVertexFormatter();

	DotExporter buildEdgeDefinition(Vertex source, Vertex neighbour);
}
