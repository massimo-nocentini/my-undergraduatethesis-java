package tarjan;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import junit.framework.Assert;
import model.OurModel;
import model.Vertex;

import org.junit.Test;

public class DfsEventsListenerTreeBuilderUnitTest {

	@Test
	public void checkingExploredNewVertexAndAlreadyKnownWithThreeVertices() {

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

		DfsEventsListener dfsEventListener = new DfsEventsListenerTreeBuilder();

		DfsExplorer dfsExplorer = DfsExplorerDefaultImplementor.Make();

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
