package model;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

public class VertexStatsRecorderUnitTest {

	@Test
	public void recordSomeVotes() {
		VertexStatsRecorder recorder = new VertexStatsRecorder();

		Vertex simple = VertexFactory.makeSimpleVertex();
		Vertex simple2 = VertexFactory.makeSimpleVertex();
		Vertex simple3 = VertexFactory.makeSimpleVertex();
		Vertex simple4 = VertexFactory.makeSimpleVertex();

		simple.addNeighbour(simple2).addNeighbour(simple3)
				.addNeighbour(simple4);

		simple2.addNeighbour(simple3).addNeighbour(simple4);

		simple3.addNeighbour(simple4);

		simple.publishYourStatsOn(recorder);
		simple2.publishYourStatsOn(recorder);
		simple3.publishYourStatsOn(recorder);
		simple4.publishYourStatsOn(recorder);

		Map<PlainTextStatsComponents, Integer> expected = new HashMap<PlainTextStatsComponents, Integer>();

		expected.put(PlainTextStatsComponents.NumberOfVertices, 4);
		expected.put(PlainTextStatsComponents.NumberOfEdges, 6);
		expected.put(PlainTextStatsComponents.NumberOfSources, 1);
		expected.put(PlainTextStatsComponents.NumberOfSinks, 1);
		expected.put(PlainTextStatsComponents.NumberOfWhites, 2);

		Assert.assertEquals(expected, recorder.collectVotes());

	}
}
