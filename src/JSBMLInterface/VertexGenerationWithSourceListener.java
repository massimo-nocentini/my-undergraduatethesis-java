package JSBMLInterface;

import model.Vertex;

public interface VertexGenerationWithSourceListener extends
		VertexGenerationListener {

	public abstract void reactantVertexHandled(Vertex vertex);

	public abstract void productVertexHandled(Vertex vertex);

}