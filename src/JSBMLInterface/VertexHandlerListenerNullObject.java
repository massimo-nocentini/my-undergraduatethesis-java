package JSBMLInterface;

import model.Vertex;

public class VertexHandlerListenerNullObject implements
		VertexHandlingWithSourceListener {

	@Override
	public void vertexHandled(Vertex vertex) {
		// my way to do nothing when a new vertex is generated is do actually
		// nothing

	}

	@Override
	public void reactantVertexHandled(Vertex vertex) {
	}

	@Override
	public void productVertexHandled(Vertex vertex) {
	}

}
