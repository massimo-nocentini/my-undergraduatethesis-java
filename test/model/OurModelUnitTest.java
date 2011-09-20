package model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import tarjan.DfsEventsListener;
import tarjan.DfsEventsListenerNullImplementor;
import dotInterface.DotExportableUnitTest;

public class OurModelUnitTest {

	@Test
	public void creation() {
		OurModel model = OurModel.makeEmptyModel();
		assertNotNull(model);
	}

	@Test
	public void emptyAfterCreation() {
		OurModel model = OurModel.makeEmptyModel();
		assertTrue(model.isEmpty());
	}

	/**
	 * This test method establish the protocol of messages which a client have
	 * to send to a OurModel object in order to request the run of a Depth First
	 * Search algorithm on the network encapsulated by the OurModel object. <br>
	 * <br>
	 * This method ensures that the method {@link OurModel}
	 * .runDepthFirstSearch(DfsEventsListener) return a reference to an instance
	 * of the class OurModel and that instance must be the same instance which
	 * receive the message (in other words the method runDepthFirstSearch is
	 * fluent respect to its class).
	 */
	@Test
	public void applyDfsSearch() {

		OurModel tarjanModel = DotExportableUnitTest.makeTarjanModel();

		DfsEventsListener dfsEventListener = new DfsEventsListenerNullImplementor();

		OurModel returnedtarjanModel = tarjanModel
				.runDepthFirstSearch(dfsEventListener);

		Assert.assertNotNull(returnedtarjanModel);
		Assert.assertSame(tarjanModel, returnedtarjanModel);

	}

	@Test(expected = NullPointerException.class)
	public void applyDfsSearchWithNullListenerThrowsException() {

		OurModel tarjanModel = DotExportableUnitTest.makeTarjanModel();

		DfsEventsListener dfsEventListener = null;

		tarjanModel.runDepthFirstSearch(dfsEventListener);
	}

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
		};

		tarjanModel.runDepthFirstSearch(dfsEventListener);

		Assert.assertEquals(2, expectedSearchEventNotifications.size());

		Assert.assertTrue(expectedSearchEventNotifications
				.contains(startSearchNotificationName));

		Assert.assertTrue(expectedSearchEventNotifications
				.contains(completeSearchNotificationName));
	}

	@Test
	public void checkingPreVisitPostVisitOneVertex() {

		Vertex v = Vertex.makeVertex();

		OurModel tarjanModel = OurModel.makeOurModelFrom(new HashSet<Vertex>(
				Arrays.<Vertex> asList(v)));

		final Set<Integer> expectedSearchEventNotifications = new HashSet<Integer>();

		DfsEventsListener dfsEventListener = new DfsEventsListener() {

			@Override
			public void searchStarted() {
			}

			@Override
			public void searchCompleted() {
			}
		};

		tarjanModel.runDepthFirstSearch(dfsEventListener);

		Assert.assertEquals(2, expectedSearchEventNotifications.size());
	}
}
