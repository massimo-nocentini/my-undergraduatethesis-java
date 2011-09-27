package tarjan;

import java.util.LinkedHashSet;
import java.util.Map;

import junit.framework.Assert;
import model.OurModel;
import model.Vertex;

import org.junit.Test;

import tarjan.DfsEventsListenerTreeBuilder.DataContainer;

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
		OurModel papadimitriouModel = OurModel
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
			public void searchCompleted(Map<Vertex, VertexDfsMetadata> map) {
			}

			@Override
			public void searchStarted(
					Map<Vertex, VertexDfsMetadata> exploredVertexMetadatasMap) {
			}

			@Override
			public void newVertexExplored(Vertex explorationCauseVertex,
					Vertex vertex) {
			}
		};

		DfsExplorer dfsExplorer = DfsExplorerDefaultImplementor.make();

		dfsExplorer.acceptDfsEventsListener(dfsEventListener);

		papadimitriouModel.runDepthFirstSearch(dfsExplorer);

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
		OurModel papadimitriouModel = OurModel
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
			public void searchCompleted(Map<Vertex, VertexDfsMetadata> map) {
			}

			@Override
			public void searchStarted(
					Map<Vertex, VertexDfsMetadata> exploredVertexMetadatasMap) {
			}

			@Override
			public void newVertexExplored(Vertex explorationCauseVertex,
					Vertex vertex) {
			}
		};

		DfsExplorer dfsExplorer = DfsExplorerDefaultImplementor.make();

		dfsExplorer.acceptDfsEventsListener(dfsEventListener);

		papadimitriouModel.runDepthFirstSearch(dfsExplorer);

		Assert.assertEquals(expectedSearchEventNotifications,
				actualSearchEventNotifications);
	}

	@Test
	public void settingPreVisitClockInfo() {

		final Vertex v = Vertex.makeVertex();
		DfsEventsListenerTreeBuilder.DataContainer metadata = new DfsEventsListenerTreeBuilder.DataContainer(
				v);

		DataContainer outputMetadata = metadata.previsitedAt(3);

		Assert.assertSame(outputMetadata, metadata);
		Assert.assertTrue(metadata.isYourPreVisitClock(3));
		Assert.assertFalse(metadata.isYourPreVisitClock(5));
	}

	@Test
	public void settingPostVisitClockInfo() {

		final Vertex v = Vertex.makeVertex();
		DfsEventsListenerTreeBuilder.DataContainer metadata = new DfsEventsListenerTreeBuilder.DataContainer(
				v);

		DataContainer outputMetadata = metadata.postvisitedAt(3);

		Assert.assertSame(outputMetadata, metadata);
		Assert.assertTrue(metadata.isYourPostVisitClock(3));
		Assert.assertFalse(metadata.isYourPostVisitClock(5));
	}
}
