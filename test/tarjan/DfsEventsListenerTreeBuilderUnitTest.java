package tarjan;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;
import model.DfsWrapperVertex;
import model.ExploreStatedWrapperVertex;
import model.ModelsRepository;
import model.OurModel;
import model.Vertex;
import model.VertexFactory;

import org.junit.Test;

import util.CallbackSignalRecorder;

public class DfsEventsListenerTreeBuilderUnitTest {
	/**
	 * <ul>
	 * <li>Given: a Papadimitriou model
	 * <li>When: method call of runDepthFirstSearch finished
	 * <li>Then: the search must have visited the nodes in a fixed,
	 * deterministic order because we have the neighbors stored in a TreeSet, so
	 * they are ordered by topological ordering.
	 * </ul>
	 */
	@Test
	public void checkingNewExploredNotificationOnPapadimitriouModel() {

		LinkedHashSet<Vertex> expectedSearchEventNotifications = new LinkedHashSet<Vertex>();
		OurModel papadimitriouModel = ModelsRepository
				.makePapadimitriouModel(expectedSearchEventNotifications);

		final LinkedHashSet<Vertex> actualSearchEventNotifications = new LinkedHashSet<Vertex>();

		DfsEventsListener dfsEventListener = new DfsEventsListener() {

			@Override
			public void postVisit(Vertex v) {
			}

			@Override
			public void preVisit(Vertex v) {

				actualSearchEventNotifications.add(v);
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
			}

			@Override
			public void fillCollectedVertices(Set<Vertex> vertices) {
			}

			@Override
			public void alreadyKnownVertex(Vertex vertex) {
			}
		};

		papadimitriouModel.runDepthFirstSearch(dfsEventListener);

		Assert.assertEquals(expectedSearchEventNotifications,
				actualSearchEventNotifications);
	}

	/**
	 * <ul>
	 * <li>Given: a Papadimitriou model
	 * <li>When: method call of runDepthFirstSearch finished
	 * <li>Then: the listener must had tracked the "clock" start and finish
	 * instants for each vertex
	 * </ul>
	 */
	@Test
	public void checkingClockIntervalsOnPapadimitriouModel() {

		LinkedHashSet<Vertex> expectedSearchEventNotifications = new LinkedHashSet<Vertex>();
		OurModel papadimitriouModel = ModelsRepository
				.makePapadimitriouModel(expectedSearchEventNotifications);

		DfsEventsListenerTreeBuilder dfsEventListener = new DfsEventsListenerTreeBuilder();

		papadimitriouModel.runDepthFirstSearch(dfsEventListener);

		CallbackSignalRecorder signalRecorder = new CallbackSignalRecorder();

		for (Vertex vertex : expectedSearchEventNotifications) {
			if (vertex.isYourSpeciesId("A")) {
				Assert.assertTrue(dfsEventListener.isVertexClockInterval(
						vertex, 1, 10));
				signalRecorder.signal();
			} else if (vertex.isYourSpeciesId("B")) {
				Assert.assertTrue(dfsEventListener.isVertexClockInterval(
						vertex, 2, 3));
				signalRecorder.signal();
			} else if (vertex.isYourSpeciesId("E")) {
				Assert.assertTrue(dfsEventListener.isVertexClockInterval(
						vertex, 4, 9));
				signalRecorder.signal();
			} else if (vertex.isYourSpeciesId("I")) {
				Assert.assertTrue(dfsEventListener.isVertexClockInterval(
						vertex, 5, 8));
				signalRecorder.signal();
			} else if (vertex.isYourSpeciesId("J")) {
				Assert.assertTrue(dfsEventListener.isVertexClockInterval(
						vertex, 6, 7));
				signalRecorder.signal();
			} else if (vertex.isYourSpeciesId("C")) {
				Assert.assertTrue(dfsEventListener.isVertexClockInterval(
						vertex, 11, 22));
				signalRecorder.signal();
			} else if (vertex.isYourSpeciesId("D")) {
				Assert.assertTrue(dfsEventListener.isVertexClockInterval(
						vertex, 12, 21));
				signalRecorder.signal();
			} else if (vertex.isYourSpeciesId("H")) {
				Assert.assertTrue(dfsEventListener.isVertexClockInterval(
						vertex, 13, 20));
				signalRecorder.signal();
			} else if (vertex.isYourSpeciesId("G")) {
				Assert.assertTrue(dfsEventListener.isVertexClockInterval(
						vertex, 14, 17));
				signalRecorder.signal();
			} else if (vertex.isYourSpeciesId("K")) {
				Assert.assertTrue(dfsEventListener.isVertexClockInterval(
						vertex, 15, 16));
				signalRecorder.signal();
			} else if (vertex.isYourSpeciesId("L")) {
				Assert.assertTrue(dfsEventListener.isVertexClockInterval(
						vertex, 18, 19));
				signalRecorder.signal();
			} else if (vertex.isYourSpeciesId("F")) {
				Assert.assertTrue(dfsEventListener.isVertexClockInterval(
						vertex, 23, 24));
				signalRecorder.signal();
			}
		}

		Assert.assertTrue(signalRecorder
				.isCountOfSignals(expectedSearchEventNotifications.size()));

	}

	@Test
	public void settingPreVisitClockInfo() {

		final Vertex v = VertexFactory.makeSimpleVertex();
		DfsWrapperVertex metadata = VertexFactory.makeDfsWrapperVertex(v);

		DfsWrapperVertex outputMetadata = metadata.previsitedAt(3);

		Assert.assertSame(outputMetadata, metadata);
		Assert.assertTrue(metadata.isYourPreVisitClock(3));
		Assert.assertFalse(metadata.isYourPreVisitClock(5));
	}

	@Test
	public void settingPostVisitClockInfo() {

		final Vertex v = VertexFactory.makeSimpleVertex();
		DfsWrapperVertex metadata = VertexFactory.makeDfsWrapperVertex(v);

		DfsWrapperVertex outputMetadata = metadata.postvisitedAt(3);

		Assert.assertSame(outputMetadata, metadata);
		Assert.assertTrue(metadata.isYourPostVisitClock(3));
		Assert.assertFalse(metadata.isYourPostVisitClock(5));
	}
}
