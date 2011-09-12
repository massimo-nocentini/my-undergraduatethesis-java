package JSBMLInterface;

import model.Vertex;

public class VertexHandlerListenerNullObject implements
		VertexGenerationWithSourceListener {

	@Override
	public void newVertexGenerated(Vertex vertex) {
		// my way to do nothing when a new vertex is generated is do actually
		// nothing

	}

	@Override
	public void newVertexFromReactor(Vertex vertex) {
	}

	@Override
	public void newVertexFromProduct(Vertex vertex) {
	}

}
