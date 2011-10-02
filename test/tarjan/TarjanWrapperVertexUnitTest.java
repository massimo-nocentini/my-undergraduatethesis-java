package tarjan;

import java.util.Stack;

import model.TarjanWrapperVertex;
import model.Vertex;
import model.VertexFactory;

import org.junit.Assert;
import org.junit.Test;

public class TarjanWrapperVertexUnitTest {

	@Test
	public void checkExploredInstant() {
		TarjanWrapperVertex vertex = VertexFactory
				.makeTarjanWrapperVertex(VertexFactory.makeSimpleVertex());

		int instant = 4;
		vertex.exploredAt(instant);

		Assert.assertTrue(vertex.isYourExploredInstantEquals(instant));
		Assert.assertFalse(vertex.isYourExploredInstantEquals(6));

	}

	@Test
	public void checkCompleteStateAfterConstruction() {
		TarjanWrapperVertex vertex = VertexFactory
				.makeTarjanWrapperVertex(VertexFactory.makeSimpleVertex());

		Assert.assertFalse(vertex.haveYouParentComponent());

	}

	@Test
	public void checkCompleteState() {
		TarjanWrapperVertex vertex = VertexFactory
				.makeTarjanWrapperVertex(VertexFactory.makeSimpleVertex());

		vertex.joinConnectedComponent(VertexFactory.makeSimpleVertex());

		Assert.assertTrue(vertex.haveYouParentComponent());

	}

	@Test
	public void stackLearningTest() {
		Vertex v1 = VertexFactory.makeSimpleVertex();
		Vertex v2 = VertexFactory.makeSimpleVertex();

		Stack<Vertex> stack = new Stack<Vertex>();

		stack.push(v1);
		stack.push(v2);

		Assert.assertEquals(v2, stack.lastElement());

		stack.pop();

		Assert.assertEquals(v1, stack.lastElement());

		stack.pop();

		Assert.assertEquals(0, stack.size());

	}

	@Test
	public void checkQueryDiscoverMethod() {
		TarjanWrapperVertex vertex = VertexFactory
				.makeTarjanWrapperVertex(VertexFactory.makeSimpleVertex());
		TarjanWrapperVertex vertexAfter = VertexFactory
				.makeTarjanWrapperVertex(VertexFactory.makeSimpleVertex());

		int instant = 4;
		vertex.exploredAt(instant);

		instant = instant + 1;
		vertexAfter.exploredAt(instant);

		Assert.assertTrue(vertex.hadYouBeenDiscoveredFirstThan(vertexAfter));
		Assert.assertFalse(vertexAfter.hadYouBeenDiscoveredFirstThan(vertex));

	}
}
