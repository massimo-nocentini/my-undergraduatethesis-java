package tarjan;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Stack;

import model.ConnectedComponentWrapperVertex;
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

		Assert.assertFalse(vertex.areYouMemberInSomeConnectedComponent());

	}

	@Test
	public void checkCompleteState() {
		TarjanWrapperVertex vertex = VertexFactory
				.makeTarjanWrapperVertex(VertexFactory.makeSimpleVertex());

		ConnectedComponentWrapperVertex connectedComponentWrapperVertex = VertexFactory
				.makeConnectedComponentWrapperVertex();

		vertex.joinConnectedComponent(connectedComponentWrapperVertex);

		Assert.assertTrue(connectedComponentWrapperVertex.isMember(vertex));

		Assert.assertTrue(vertex.areYouMemberInSomeConnectedComponent());

	}

	@Test
	public void checkBridgeConnectedComponents() {
		TarjanWrapperVertex vertex = VertexFactory
				.makeTarjanWrapperVertex(VertexFactory.makeSimpleVertex());
		TarjanWrapperVertex vertex2 = VertexFactory
				.makeTarjanWrapperVertex(VertexFactory.makeSimpleVertex());

		ConnectedComponentWrapperVertex connectedComponentWrapperVertex = VertexFactory
				.makeConnectedComponentWrapperVertex();
		ConnectedComponentWrapperVertex connectedComponentWrapperVertex2 = VertexFactory
				.makeConnectedComponentWrapperVertex();

		vertex.joinConnectedComponent(connectedComponentWrapperVertex);
		vertex2.joinConnectedComponent(connectedComponentWrapperVertex2);

		vertex.bridgeConnectedComponentOf(vertex2);

		Assert.assertTrue(connectedComponentWrapperVertex
				.isYourNeighborhoodEquals(new HashSet<Vertex>(Arrays
						.asList(connectedComponentWrapperVertex2))));

	}

	@Test
	public void collectingOutput() {

		Vertex vertex = VertexFactory.makeSimpleVertex();
		Vertex vertex2 = VertexFactory.makeSimpleVertex();
		Vertex vertex3 = VertexFactory.makeSimpleVertex();
		Vertex vertex4 = VertexFactory.makeSimpleVertex();

		vertex.addNeighbour(vertex2).addNeighbour(vertex3)
				.addNeighbour(vertex4);

		vertex2.addNeighbour(vertex).addNeighbour(vertex3);

		TarjanWrapperVertex wrappedVertex = VertexFactory
				.makeTarjanWrapperVertex(vertex);
		TarjanWrapperVertex wrappedVertex2 = VertexFactory
				.makeTarjanWrapperVertex(vertex2);
		TarjanWrapperVertex wrappedVertex3 = VertexFactory
				.makeTarjanWrapperVertex(vertex3);
		TarjanWrapperVertex wrappedVertex4 = VertexFactory
				.makeTarjanWrapperVertex(vertex4);

		ConnectedComponentWrapperVertex connectedComponent1 = VertexFactory
				.makeConnectedComponentWrapperVertex();
		ConnectedComponentWrapperVertex connectedComponent2 = VertexFactory
				.makeConnectedComponentWrapperVertex();
		ConnectedComponentWrapperVertex connectedComponent3 = VertexFactory
				.makeConnectedComponentWrapperVertex();

		wrappedVertex.joinConnectedComponent(connectedComponent1);
		wrappedVertex2.joinConnectedComponent(connectedComponent1);
		wrappedVertex3.joinConnectedComponent(connectedComponent2);
		wrappedVertex4.joinConnectedComponent(connectedComponent3);

		TarjanEventsListenerTreeBuilder listener = new TarjanEventsListenerTreeBuilder();

		Map<Vertex, TarjanWrapperVertex> map = new LinkedHashMap<Vertex, TarjanWrapperVertex>();
		map.put(vertex, wrappedVertex);
		map.put(vertex2, wrappedVertex2);
		map.put(vertex3, wrappedVertex3);
		map.put(vertex4, wrappedVertex4);

		Map<Vertex, ConnectedComponentWrapperVertex> componentsMembershipMap = new HashMap<Vertex, ConnectedComponentWrapperVertex>();
		componentsMembershipMap.put(vertex, connectedComponent1);
		componentsMembershipMap.put(vertex2, connectedComponent1);
		componentsMembershipMap.put(vertex3, connectedComponent2);
		componentsMembershipMap.put(vertex4, connectedComponent3);

		LinkedHashSet<Vertex> vertices = new LinkedHashSet<Vertex>();
		listener.fillCollectedVertices(vertices, map, componentsMembershipMap);

		Assert.assertEquals(3, vertices.size());

		int i = 1;
		Vertex outputVertex1 = null;
		Vertex outputVertex2 = null;
		Vertex outputVertex3 = null;
		for (Vertex runningVertex : vertices) {

			if (i == 1) {
				outputVertex1 = runningVertex;
			} else if (i == 2) {
				outputVertex2 = runningVertex;
			} else if (i == 3) {
				outputVertex3 = runningVertex;
			}

			i = i + 1;
		}

		Assert.assertTrue(outputVertex1
				.isYourNeighborhoodEquals(new HashSet<Vertex>(Arrays.asList(
						outputVertex2, outputVertex3))));

		Assert.assertTrue(outputVertex2.isYourNeighborhoodEmpty());
		Assert.assertTrue(outputVertex3.isYourNeighborhoodEmpty());

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
