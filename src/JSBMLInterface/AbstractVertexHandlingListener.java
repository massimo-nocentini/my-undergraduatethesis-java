package JSBMLInterface;

import model.Vertex;

public abstract class AbstractVertexHandlingListener implements
		VertexHandlingListener {

	private final VertexHandlingWithSourceListener listener;

	protected VertexHandlingWithSourceListener getListener() {
		return listener;
	}

	public AbstractVertexHandlingListener(
			VertexHandlingWithSourceListener listener) {
		this.listener = listener;
	}

	@Override
	public void vertexHandled(Vertex vertex) {
		this.getListener().vertexHandled(vertex);
		this.onVertexHandled(vertex);
	}

	public abstract void onVertexHandled(Vertex vertex);

}
