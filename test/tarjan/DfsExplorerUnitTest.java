package tarjan;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.Assert;
import model.OurModel;
import model.Vertex;

import org.junit.Test;

import util.CallbackSignalRecorder;

public class DfsExplorerUnitTest {

	/**
	 * <ul>
	 * <li>Given: a empty model
	 * <li>When: method runDepthFirstSearch called
	 * <li>Then: we have to be notified first on start handle, next on complete
	 * handle, in this exactly order.
	 * </ul>
	 */
	@Test
	public void checkingStartEndNotificationDfsSearchWithEmptyModel() {

		OurModel emptyModel = OurModel.makeEmptyModel();

		final String startSearchNotificationName = "Search Started";

		final String completeSearchNotificationName = "Search Completed";

		final List<String> actualSearchEventNotifications = new LinkedList<String>();

		final List<String> expectedSearchEventNotifications = new LinkedList<String>();
		expectedSearchEventNotifications.add(startSearchNotificationName);
		expectedSearchEventNotifications.add(completeSearchNotificationName);

		DfsEventsListener dfsEventListener = new DfsEventsListener() {

			@Override
			public void postVisit(Vertex v) {
			}

			@Override
			public void preVisit(Vertex v) {
			}

			@Override
			public void searchCompleted(Map<Vertex, VertexDfsMetadata> map) {
				actualSearchEventNotifications
						.add(completeSearchNotificationName);
			}

			@Override
			public void searchStarted(
					Map<Vertex, VertexDfsMetadata> exploredVertexMetadatasMap) {
				actualSearchEventNotifications.add(startSearchNotificationName);
			}

			@Override
			public void newVertexExplored(Vertex explorationCauseVertex,
					Vertex vertex) {

			}
		};

		DfsExplorer dfsExplorer = DfsExplorerDefaultImplementor.make();

		dfsExplorer.acceptDfsEventsListener(dfsEventListener);

		emptyModel.runDepthFirstSearch(dfsExplorer);

		Assert.assertEquals(2, actualSearchEventNotifications.size());

		Assert.assertEquals(expectedSearchEventNotifications,
				actualSearchEventNotifications);

		Collections.reverse(actualSearchEventNotifications);

		Assert.assertFalse(expectedSearchEventNotifications
				.equals(actualSearchEventNotifications));
	}

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
	 * <li>Given: a Tarjan model
	 * <li>When: method call of runDepthFirstSearch is finished
	 * <li>Then: all the vertices must be in explored state
	 * </ul>
	 */
	@Test
	public void assuringAllVerticesAreExploredAtTheEndOfDFS() {

		Set<Vertex> vertices = new TreeSet<Vertex>();
		OurModel tarjanModel = OurModel.makeTarjanModel(vertices);

		final CallbackSignalRecorder callbackSignalRecorder = new CallbackSignalRecorder();

		DfsEventsListener dfsEventListener = new DfsEventsListener() {

			@Override
			public void postVisit(Vertex v) {
			}

			@Override
			public void preVisit(Vertex v) {
			}

			@Override
			public void searchCompleted(Map<Vertex, VertexDfsMetadata> map) {

				for (Entry<Vertex, VertexDfsMetadata> entry : map.entrySet()) {
					Assert.assertTrue(entry.getValue().isExplored());
					callbackSignalRecorder.signal();
				}

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

		tarjanModel.runDepthFirstSearch(dfsExplorer);

		Assert.assertTrue(callbackSignalRecorder.isSignaled());
		Assert.assertTrue(callbackSignalRecorder.isCountOfSignals(vertices
				.size()));

	}

	@Test(expected = UnsupportedOperationException.class)
	public void expectingExceptionWhenTryingUpdateSearchMapsOnSearchStarted() {

		final Vertex v = Vertex.makeVertex();

		OurModel tarjanModel = OurModel.makeOurModelFrom(new HashSet<Vertex>(
				Arrays.<Vertex> asList(v)));

		DfsEventsListener dfsEventListener = new DfsEventsListener() {

			@Override
			public void postVisit(Vertex vertex) {
			}

			@Override
			public void preVisit(Vertex vertex) {
			}

			@Override
			public void searchCompleted(Map<Vertex, VertexDfsMetadata> map) {
				Vertex erroneousVerte = Vertex.makeVertex();
				map.put(erroneousVerte, new VertexDfsMetadata(erroneousVerte));
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

		@SuppressWarnings("unused")
		OurModel returnedtarjanModel = tarjanModel
				.runDepthFirstSearch(dfsExplorer);

	}

	@Test(expected = UnsupportedOperationException.class)
	public void expectingExceptionWhenTryingUpdateSearchMapsOnSearchCompleted() {

		final Vertex v = Vertex.makeVertex();

		OurModel tarjanModel = OurModel.makeOurModelFrom(new HashSet<Vertex>(
				Arrays.<Vertex> asList(v)));

		DfsEventsListener dfsEventListener = new DfsEventsListener() {

			@Override
			public void postVisit(Vertex vertex) {
			}

			@Override
			public void preVisit(Vertex vertex) {
			}

			@Override
			public void searchCompleted(Map<Vertex, VertexDfsMetadata> map) {
			}

			@Override
			public void searchStarted(Map<Vertex, VertexDfsMetadata> map) {
				Vertex erroneousVerte = Vertex.makeVertex();
				map.put(erroneousVerte, new VertexDfsMetadata(erroneousVerte));
			}

			@Override
			public void newVertexExplored(Vertex explorationCauseVertex,
					Vertex vertex) {

			}
		};

		DfsExplorer dfsExplorer = DfsExplorerDefaultImplementor.make();

		dfsExplorer.acceptDfsEventsListener(dfsEventListener);

		@SuppressWarnings("unused")
		OurModel returnedtarjanModel = tarjanModel
				.runDepthFirstSearch(dfsExplorer);

	}

	@Test
	public void checkingPreVisitPostVisitModelWithThreeVertices() {

		final Vertex v = Vertex.makeVertex();
		final Vertex v2 = Vertex.makeVertex();
		final Vertex v3 = Vertex.makeVertex();

		v.addNeighbour(v2);
		v.addNeighbour(v3);

		v3.addNeighbour(v2);

		OurModel tarjanModel = OurModel.makeOurModelFrom(new TreeSet<Vertex>(
				Arrays.<Vertex> asList(v, v2, v3)));

		final List<Vertex> expectedOrderedPrevisitInvocation = new LinkedList<Vertex>();
		expectedOrderedPrevisitInvocation.add(v);
		expectedOrderedPrevisitInvocation.add(v2);
		expectedOrderedPrevisitInvocation.add(v3);

		final List<Vertex> expectedOrderedPostvisitInvocation = new LinkedList<Vertex>();
		expectedOrderedPostvisitInvocation.add(v2);
		expectedOrderedPostvisitInvocation.add(v3);
		expectedOrderedPostvisitInvocation.add(v);

		final List<Vertex> actualOrderedPrevisitInvocation = new LinkedList<Vertex>();
		final List<Vertex> actualOrderedPostvisitInvocation = new LinkedList<Vertex>();

		DfsEventsListener dfsEventListener = new DfsEventsListener() {

			@Override
			public void postVisit(Vertex vertex) {
				actualOrderedPostvisitInvocation.add(vertex);
			}

			@Override
			public void preVisit(Vertex vertex) {
				actualOrderedPrevisitInvocation.add(vertex);
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

		@SuppressWarnings("unused")
		OurModel returnedtarjanModel = tarjanModel
				.runDepthFirstSearch(dfsExplorer);

		Assert.assertEquals(3, expectedOrderedPrevisitInvocation.size());
		Assert.assertEquals(3, expectedOrderedPostvisitInvocation.size());

		Assert.assertEquals(expectedOrderedPrevisitInvocation,
				actualOrderedPrevisitInvocation);

		Assert.assertEquals(expectedOrderedPostvisitInvocation,
				actualOrderedPostvisitInvocation);

	}

}
