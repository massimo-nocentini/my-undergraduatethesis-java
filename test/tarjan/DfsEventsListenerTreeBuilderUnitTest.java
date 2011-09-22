package tarjan;

import junit.framework.Assert;

import org.junit.Test;

public class DfsEventsListenerTreeBuilderUnitTest {

	@Test
	public void checkingExploredNewVertexAndAlreadyKnownWithThreeVertices() {

		// OurModel papadimitriouModel = OurModel
		// .makeOurModelFrom(new TreeSet<Vertex>(Arrays.<Vertex> asList(
		// vA, vB, vC, vD, vE, vF, vG, vH, vI, vJ, vK, vL)));

		DfsEventsListener dfsEventListener = new DfsEventsListenerTreeBuilder();

		DfsExplorer dfsExplorer = DfsExplorerDefaultImplementor.make();

		dfsExplorer.acceptDfsEventsListener(dfsEventListener);

		// bookmark fail.
		Assert.fail();

		// @SuppressWarnings("unused")
		// OurModel returnedtarjanModel = tarjanModel
		// .runDepthFirstSearch(dfsExplorer);
		//
		// Assert.assertEquals(3, expectedOrderedPrevisitInvocation.size());
		// Assert.assertEquals(3, expectedOrderedPostvisitInvocation.size());
		//
		// Assert.assertEquals(expectedOrderedPrevisitInvocation,
		// actualOrderedPrevisitInvocation);
		//
		// Assert.assertEquals(expectedOrderedPostvisitInvocation,
		// actualOrderedPostvisitInvocation);

	}
}
