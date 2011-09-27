package dotInterface;

import java.io.Writer;
import java.util.Set;

public interface DotDocumentPartHandler {

	boolean isVertexDefinitionPartEquals(Set<String> part);

	boolean isEdgeDefinitionPartEquals(Set<String> part);

	boolean isVertexLabelOutsideBoxPartEquals(Set<String> Part);

	DotDocumentPartHandler collectGeneralSettingsPart(Writer outputPlugObject);

	DotDocumentPartHandler collectEdgeDefinitionPart(Writer outputPlugObject);

	DotDocumentPartHandler collectVertexDefinitionPart(Writer outputPlugObject);

	DotDocumentPartHandler collectVertexLabelOutsideBoxPart(
			Writer outputPlugObject);
}
