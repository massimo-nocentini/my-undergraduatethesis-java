package tarjan;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;
import model.OurModel;
import model.Vertex;

import org.junit.Test;

public class DfsExplorerUnitTest {

	@Test
	public void checkingStartEndNotificationDfsSearchWithEmptyModel() {

		OurModel tarjanModel = OurModel.makeEmptyModel();

		final String startSearchNotificationName = "Search Started";

		final String completeSearchNotificationName = "Search Completed";

		final Set<String> expectedSearchEventNotifications = new HashSet<String>();

		DfsEventsListener dfsEventListener = new DfsEventsListener() {

			@Override
			public void searchStarted() {
				expectedSearchEventNotifications
						.add(startSearchNotificationName);
			}

			@Override
			public void searchCompleted() {
				expectedSearchEventNotifications
						.add(completeSearchNotificationName);
			}

			@Override
			public void postVisit(Vertex v) {
				// TODO Auto-generated method stub

			}

			@Override
			public void preVisit(Vertex v) {
				// TODO Auto-generated method stub

			}
		};

		DfsExplorer dfsExplorer = DfsExplorerDefaultImplementor.Make();

		dfsExplorer.acceptDfsEventsListener(dfsEventListener);

		tarjanModel.runDepthFirstSearch(dfsExplorer);

		Assert.assertEquals(2, expectedSearchEventNotifications.size());

		Assert.assertTrue(expectedSearchEventNotifications
				.contains(startSearchNotificationName));

		Assert.assertTrue(expectedSearchEventNotifications
				.contains(completeSearchNotificationName));
	}

	@Test
	public void checkingPreVisitPostVisitModelWithOneVertexAndNoEdges() {

		final Vertex v = Vertex.makeVertex();

		final String preVisitFlag = "preVisit handle called";
		final String postVisitFlag = "postVisit handle called";

		OurModel tarjanModel = OurModel.makeOurModelFrom(new HashSet<Vertex>(
				Arrays.<Vertex> asList(v)));

		final Set<String> expectedListenedEvents = new HashSet<String>();
		expectedListenedEvents.add(preVisitFlag);
		expectedListenedEvents.add(postVisitFlag);

		final Set<String> listenedEvents = new HashSet<String>();

		DfsEventsListener dfsEventListener = new DfsEventsListener() {

			@Override
			public void searchStarted() {
			}

			@Override
			public void searchCompleted() {
			}

			@Override
			public void postVisit(Vertex vertex) {
				Assert.assertEquals(v, vertex);
				listenedEvents.add(postVisitFlag);
			}

			@Override
			public void preVisit(Vertex vertex) {
				Assert.assertEquals(v, vertex);
				listenedEvents.add(preVisitFlag);
			}
		};

		DfsExplorer dfsExplorer = DfsExplorerDefaultImplementor.Make();

		dfsExplorer.acceptDfsEventsListener(dfsEventListener);

		OurModel returnedtarjanModel = tarjanModel
				.runDepthFirstSearch(dfsExplorer);

		Assert.assertEquals(expectedListenedEvents, listenedEvents);

	}

	@Test
	public void checkingPreVisitPostVisitModelWithThreeVertices() {

		final Vertex v = Vertex.makeVertex();
		final Vertex v2 = Vertex.makeVertex();
		final Vertex v3 = Vertex.makeVertex();

		OurModel tarjanModel = OurModel.makeOurModelFrom(new HashSet<Vertex>(
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
			public void searchStarted() {
			}

			@Override
			public void searchCompleted() {
			}

			@Override
			public void postVisit(Vertex vertex) {
				actualOrderedPostvisitInvocation.add(vertex);
			}

			@Override
			public void preVisit(Vertex vertex) {
				actualOrderedPrevisitInvocation.add(vertex);
			}
		};

		DfsExplorer dfsExplorer = DfsExplorerDefaultImplementor.Make();

		dfsExplorer.acceptDfsEventsListener(dfsEventListener);

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
