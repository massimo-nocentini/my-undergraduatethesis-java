package dotInterface;

import java.io.Writer;

public interface DotDocumentPartHandler {

	DotDocumentPartHandler collectGeneralSettingsPart(Writer outputPlugObject);

	DotDocumentPartHandler collectEdgeDefinitionPart(Writer outputPlugObject);

	DotDocumentPartHandler collectVertexDefinitionPart(Writer outputPlugObject);

	DotDocumentPartHandler collectVertexLabelOutsideBoxPart(
			Writer outputPlugObject);
}
