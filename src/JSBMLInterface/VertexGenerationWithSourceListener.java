package JSBMLInterface;

import model.Vertex;

public interface VertexGenerationWithSourceListener extends
		VertexGenerationListener {

	public abstract void newVertexFromReactor(Vertex vertex);

	public abstract void newVertexFromProduct(Vertex vertex);

}