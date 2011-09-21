package model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.Assert;

import org.junit.Test;

import tarjan.DfsEventsListener;
import tarjan.DfsEventsListenerNullImplementor;
import tarjan.DfsExplorer;
import tarjan.DfsExplorerDefaultImplementor;
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

	@Test
	public void addingNeighborsOrderIsPreservedByVertex() {
		Vertex vertex = Vertex.makeVertex();
		Vertex a1 = Vertex.makeVertex();
		Vertex a2 = Vertex.makeVertex();
		Vertex a3 = Vertex.makeVertex();

		Set<Vertex> vertices = new TreeSet<Vertex>();
		vertices.add(vertex);
		vertices.add(a1);
		vertices.add(a2);
		vertices.add(a3);

		OurModel customModel = OurModel.makeOurModelFrom(vertices);

		List<Vertex> desiredVerticesSequence = new LinkedList<Vertex>(
				Arrays.asList(vertex, a1, a2, a3));

		final List<Vertex> actualVerticesSequence = new LinkedList<Vertex>();

		customModel.doOnVertices(new VertexLogicApplier() {

			@Override
			public void apply(Vertex neighbourVertex) {
				actualVerticesSequence.add(neighbourVertex);
			}
		});

		Assert.assertEquals(desiredVerticesSequence, actualVerticesSequence);
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

		DfsExplorer dfsExplorer = DfsExplorerDefaultImplementor.Make();

		dfsExplorer.acceptDfsEventsListener(dfsEventListener);

		OurModel returnedtarjanModel = tarjanModel
				.runDepthFirstSearch(dfsExplorer);

		Assert.assertNotNull(returnedtarjanModel);
		Assert.assertSame(tarjanModel, returnedtarjanModel);

	}

	@Test(expected = NullPointerException.class)
	public void applyDfsSearchWithNullListenerThrowsException() {

		OurModel tarjanModel = DotExportableUnitTest.makeTarjanModel();

		DfsEventsListener dfsEventListener = null;

		DfsExplorer dfsExplorer = DfsExplorerDefaultImplementor.Make();

		dfsExplorer.acceptDfsEventsListener(dfsEventListener);

		tarjanModel.runDepthFirstSearch(dfsExplorer);

	}

}
