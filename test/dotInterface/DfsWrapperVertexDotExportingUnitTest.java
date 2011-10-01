package dotInterface;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import model.DfsWrapperVertex;
import model.OurModel;
import model.Vertex;
import model.VertexFactory;

import org.junit.Assert;
import org.junit.Test;

import tarjan.DfsEventsListenerTreeBuilder;
import tarjan.DfsExplorer;
import tarjan.DfsExplorerDefaultImplementor;

public class DfsWrapperVertexDotExportingUnitTest {

	@Test
	public void testingSimpleModelVertexInformation() {

		final Vertex v = VertexFactory.makeSimpleVertex();
		final Vertex v2 = VertexFactory.makeSimpleVertex();
		final Vertex v3 = VertexFactory.makeSimpleVertex();

		v.addNeighbour(v2);
		v.addNeighbour(v3);

		v3.addNeighbour(v2);

		OurModel simpleModel = OurModel.makeOurModelFrom(new TreeSet<Vertex>(
				Arrays.<Vertex> asList(v, v2, v3)));

		DfsEventsListenerTreeBuilder dfsEventListener = new DfsEventsListenerTreeBuilder();

		DfsExplorer dfsExplorer = DfsExplorerDefaultImplementor.make();

		dfsExplorer.acceptDfsEventsListener(dfsEventListener);

		simpleModel.runDepthFirstSearch(dfsExplorer);

		Set<Vertex> exploredVertices = new LinkedHashSet<Vertex>();
		dfsEventListener.fillCollectedVertices(exploredVertices);

		for (Vertex exploredVertex : exploredVertices) {

			DfsWrapperVertex dfsWrapper = (DfsWrapperVertex) exploredVertex;
			Writer expectedStringForVertex = new StringWriter();

			try {

				dfsWrapper.collectEdgeDefinitionInto(expectedStringForVertex,
						dfsWrapper);

				expectedStringForVertex.append(DotFileUtilHandler
						.getBlankString());

				expectedStringForVertex.append(DotFileUtilHandler
						.composeSquareBracketsWrapping(DotFileUtilHandler
								.composeVertexLabelOutsideBox(dfsWrapper
										.yourDfsIntervalToString())));
			} catch (IOException e) {
				Assert.fail("Impossible to create the expected object.");
			}

			Writer actualWriterForVertex = new StringWriter();
			dfsWrapper.collectVertexLabelOutsideBoxInto(actualWriterForVertex);

			Assert.assertEquals(expectedStringForVertex.toString(),
					actualWriterForVertex.toString());

		}

	}
}
