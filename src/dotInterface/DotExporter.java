package dotInterface;

import java.util.Set;

public interface DotExporter {

	void buildVertexDefinition(VertexDotInfoProvider vertex);

	Set<String> getGraphDotBody();

	String getCompleteContent();
}
