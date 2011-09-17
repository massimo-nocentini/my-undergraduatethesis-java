package dotInterface;

import java.io.Writer;
import java.util.Set;

public interface DotDocumentPartHandler {

	boolean isVertexDefinitionPartEquals(Set<String> part);

	DotDocumentPartHandler collectGeneralSettingsPart(Writer outputPlugObject);

	DotDocumentPartHandler collectEdgeDefinitionPart(Writer outputPlugObject);

	DotDocumentPartHandler collectVertexDefinitionPart(Writer outputPlugObject);
}
