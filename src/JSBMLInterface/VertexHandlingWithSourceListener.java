package JSBMLInterface;

import model.Vertex;

public interface VertexHandlingWithSourceListener extends
		VertexHandlingListener {

	public abstract void reactantVertexHandled(Vertex vertex);

	public abstract void productVertexHandled(Vertex vertex);

}