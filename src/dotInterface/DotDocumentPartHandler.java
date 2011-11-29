package dotInterface;

import java.io.Writer;

/**
 * This interface establish the messages that an handler object who want to
 * create part of a dot document is obliged to be able to respond to.
 * 
 * It is a low level interface because it fix the messaging protocol in order to
 * build all the necessary part to build a nice dot representation of the graph.
 * 
 */
public interface DotDocumentPartHandler {

	DotDocumentPartHandler collectGeneralSettingsPart(Writer outputPlugObject);

	DotDocumentPartHandler collectEdgeDefinitionPart(Writer outputPlugObject);

	DotDocumentPartHandler collectVertexDefinitionPart(Writer outputPlugObject);

	DotDocumentPartHandler collectVertexLabelOutsideBoxPart(
			Writer outputPlugObject);
}
