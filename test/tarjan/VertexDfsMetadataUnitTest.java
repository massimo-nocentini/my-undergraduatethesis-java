package tarjan;

import java.util.Map;

import junit.framework.Assert;
import model.Vertex;

import org.junit.Test;

public class VertexDfsMetadataUnitTest {

	@Test
	public void creation() {
		Vertex v = Vertex.makeVertex();
		VertexDfsMetadata metadata = new VertexDfsMetadata(v);

		Assert.assertFalse(metadata.isExplored());
	}

	@Test
	public void isExploredSideEffect() {
		final Vertex v = Vertex.makeVertex();
		VertexDfsMetadata metadata = new VertexDfsMetadata(v);

		DfsEventsListener eventListener = new DfsEventsListener() {

			@Override
			public void searchCompleted(Map<Vertex, VertexDfsMetadata> map) {
				Assert.assertEquals(1, map.size());
			}

			@Override
			public void preVisit(Vertex vertex) {
				Assert.assertEquals(v, vertex);

			}

			@Override
			public void postVisit(Vertex v) {
			}

			@Override
			public void searchStarted(
					Map<Vertex, VertexDfsMetadata> exploredVertexMetadatasMap) {

			}
		};

		DfsExplorer vertexExplorer = new DfsExplorer() {

			@Override
			public void searchStarted(Map<Vertex, VertexDfsMetadata> map) {

			}

			@Override
			public void searchCompleted(Map<Vertex, VertexDfsMetadata> map) {

			}

			@Override
			public void exploreVertex(Vertex v) {

			}

			@Override
			public void acceptDfsEventsListener(DfsEventsListener listener) {

			}
		};

		vertexExplorer.acceptDfsEventsListener(eventListener);
		metadata.ifNotExplored(vertexExplorer);

		Assert.assertTrue(metadata.isExplored());
	}

}
