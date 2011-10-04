package tarjan;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.Assert;
import model.ExploreStatedWrapperVertex;
import model.OurModel;
import model.Vertex;
import model.VertexFactory;

import org.junit.Test;

import util.CallbackSignalRecorder;

public class VertexDfsMetadataUnitTest {

	@Test
	public void creation() {
		Vertex v = VertexFactory.makeSimpleVertex();
		ExploreStatedWrapperVertex metadata = new ExploreStatedWrapperVertex(v);

		Assert.assertFalse(metadata.isExplored());
	}

	@Test
	public void isExploredSideEffect() {
		final Vertex v = VertexFactory.makeSimpleVertex();
		ExploreStatedWrapperVertex metadata = new ExploreStatedWrapperVertex(v);

		DfsEventsListener eventListener = new DfsEventsListener() {

			@Override
			public void searchCompleted(
					Map<Vertex, ExploreStatedWrapperVertex> map) {
				Assert.assertEquals(1, map.size());
			}

			@Override
			public void preVisit(Vertex vertex) {
				Assert.assertEquals(v, vertex);

			}

			@Override
			public void postVisit(Vertex vertex) {
				Assert.assertEquals(v, vertex);
			}

			@Override
			public void searchStarted(
					Map<Vertex, ExploreStatedWrapperVertex> exploredVertexMetadatasMap) {

			}

			@Override
			public void newVertexExplored(Vertex explorationCauseVertex,
					Vertex vertex) {
			}

			@Override
			public void fillCollectedVertices(Set<Vertex> vertices) {
			}

			@Override
			public void alreadyKnownVertex(Vertex vertex) {
			}
		};

		DfsExplorer vertexExplorer = new DfsExplorer() {

			@Override
			public void searchStarted(
					Map<Vertex, ExploreStatedWrapperVertex> map) {

			}

			@Override
			public void searchCompleted(
					Map<Vertex, ExploreStatedWrapperVertex> map) {

			}

			@Override
			public void newVertexExplored(Vertex explorationCauseVertex,
					Vertex vertex) {

			}

			@Override
			public void exploreVertex(Vertex v) {

			}

			@Override
			public void acceptDfsEventsListener(DfsEventsListener listener) {

			}

			@Override
			public void alreadyKnownVertex(Vertex vertex) {
			}
		};
		vertexExplorer.acceptDfsEventsListener(eventListener);
		metadata.ifNotExplored(vertexExplorer);

		Assert.assertTrue(metadata.isExplored());
	}

	@Test
	public void checkingExploredNewVertexAndAlreadyKnownWithThreeVertices() {

		final Vertex v = VertexFactory.makeSimpleVertex();
		final Vertex v2 = VertexFactory.makeSimpleVertex();
		final Vertex v3 = VertexFactory.makeSimpleVertex();

		final CallbackSignalRecorder callbackSignalRecorder = new CallbackSignalRecorder();

		v.addNeighbour(v2);
		v.addNeighbour(v3);

		v3.addNeighbour(v2);

		OurModel tarjanModel = OurModel.makeOurModelFrom(new TreeSet<Vertex>(
				Arrays.<Vertex> asList(v, v2, v3)));

		final Map<Vertex, Set<Vertex>> expectedExplorationMap = new HashMap<Vertex, Set<Vertex>>();
		Set<Vertex> expectedSet = new TreeSet<Vertex>();
		expectedSet.add(v2);
		expectedSet.add(v3);

		expectedExplorationMap.put(v, expectedSet);

		final Map<Vertex, Set<Vertex>> actualExplorationMap = new HashMap<Vertex, Set<Vertex>>();
		final Set<Vertex> actualSet = new HashSet<Vertex>();

		DfsEventsListener dfsEventListener = new DfsEventsListener() {

			@Override
			public void postVisit(Vertex vertex) {
			}

			@Override
			public void preVisit(Vertex vertex) {
			}

			@Override
			public void searchCompleted(
					Map<Vertex, ExploreStatedWrapperVertex> map) {
			}

			@Override
			public void searchStarted(
					Map<Vertex, ExploreStatedWrapperVertex> exploredVertexMetadatasMap) {

			}

			@Override
			public void newVertexExplored(Vertex explorationCauseVertex,
					Vertex vertex) {

				callbackSignalRecorder.signal();

				if (actualExplorationMap.containsKey(explorationCauseVertex) == false) {
					actualExplorationMap.put(explorationCauseVertex, actualSet);
				}
				actualExplorationMap.get(explorationCauseVertex).add(vertex);
			}

			@Override
			public void fillCollectedVertices(Set<Vertex> vertices) {
			}

			@Override
			public void alreadyKnownVertex(Vertex vertex) {
			}
		};

		@SuppressWarnings("unused")
		OurModel returnedtarjanModel = tarjanModel
				.runDepthFirstSearch(dfsEventListener);

		Assert.assertTrue(callbackSignalRecorder.isSignaled());

		Assert.assertEquals(1, expectedExplorationMap.size());
		Assert.assertEquals(1, actualExplorationMap.size());

		Assert.assertEquals(expectedExplorationMap.get(v),
				actualExplorationMap.get(v));
	}

	@Test
	public void checkToggleMethod() {

		final Vertex v = VertexFactory.makeSimpleVertex();
		ExploreStatedWrapperVertex metadata = new ExploreStatedWrapperVertex(v);

		Assert.assertFalse(metadata.isExplored());
		Assert.assertTrue(metadata.toggle().isExplored());
		Assert.assertTrue(metadata.toggle().isExplored());
	}

}
