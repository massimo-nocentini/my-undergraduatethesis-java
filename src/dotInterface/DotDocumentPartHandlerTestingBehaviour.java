package dotInterface;

import java.util.Set;

interface DotDocumentPartHandlerTestingBehaviour {

	boolean isVertexDefinitionPartEquals(Set<String> part);

	boolean isEdgeDefinitionPartEquals(Set<String> part);

	boolean isVertexLabelOutsideBoxPartEquals(Set<String> Part);

}