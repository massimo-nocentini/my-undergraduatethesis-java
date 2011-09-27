package tarjan;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;
import model.OurModel;
import model.Vertex;

import org.junit.Test;

public class DfsEventsListenerTreeBuilderUnitTest {

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
			public void postVisit(Vertex vertex) {
				Assert.assertEquals(v, vertex);
				listenedEvents.add(postVisitFlag);
			}

			@Override
			public void preVisit(Vertex vertex) {
				Assert.assertEquals(v, vertex);
				listenedEvents.add(preVisitFlag);
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

		Assert.assertEquals(expectedListenedEvents, listenedEvents);

	}
}
