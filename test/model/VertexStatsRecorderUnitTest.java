package model;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

public class VertexStatsRecorderUnitTest {

	@Test
	public void recordSimpleVerticesVotes() {
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

		expected.put(PlainTextStatsComponents.NOfVertices, 4);
		expected.put(PlainTextStatsComponents.NOfEdges, 6);
		expected.put(PlainTextStatsComponents.NOfSources, 1);
		expected.put(PlainTextStatsComponents.NOfSinks, 1);
		expected.put(PlainTextStatsComponents.NOfWhites, 2);

		Assert.assertTrue(recorder.isSimpleVerticesVotesEquals(expected));

	}

	@Test
	public void recordConnectedComponentsVotes() {
		VertexStatsRecorder recorder = new VertexStatsRecorder();

		ConnectedComponentWrapperVertex simple = VertexFactory
				.makeConnectedComponentWrapperVertex();
		ConnectedComponentWrapperVertex simple2 = VertexFactory
				.makeConnectedComponentWrapperVertex();
		ConnectedComponentWrapperVertex simple3 = VertexFactory
				.makeConnectedComponentWrapperVertex();
		ConnectedComponentWrapperVertex simple4 = VertexFactory
				.makeConnectedComponentWrapperVertex();

		simple.includeMember(VertexFactory.makeSimpleVertex());
		simple.includeMember(VertexFactory.makeSimpleVertex());
		simple.includeMember(VertexFactory.makeSimpleVertex());

		simple.addNeighbour(simple2);

		simple2.includeMember(VertexFactory.makeSimpleVertex());

		simple2.addNeighbour(simple3).addNeighbour(simple4);

		simple3.includeMember(VertexFactory.makeSimpleVertex());
		simple3.includeMember(VertexFactory.makeSimpleVertex());

		simple3.addNeighbour(simple4);

		simple4.includeMember(VertexFactory.makeSimpleVertex());
		simple4.includeMember(VertexFactory.makeSimpleVertex());
		simple4.includeMember(VertexFactory.makeSimpleVertex());

		simple.publishYourStatsOn(recorder);
		simple2.publishYourStatsOn(recorder);
		simple3.publishYourStatsOn(recorder);
		simple4.publishYourStatsOn(recorder);

		Map<PlainTextStatsComponents, Integer> expectedFor3members = new HashMap<PlainTextStatsComponents, Integer>();

		expectedFor3members.put(PlainTextStatsComponents.NOfComponents, 2);
		expectedFor3members.put(PlainTextStatsComponents.NOfSources, 1);
		expectedFor3members.put(PlainTextStatsComponents.NOfSinks, 1);
		expectedFor3members.put(PlainTextStatsComponents.NOfWhites, 0);

		Map<PlainTextStatsComponents, Integer> expectedFor2members = new HashMap<PlainTextStatsComponents, Integer>();

		expectedFor2members.put(PlainTextStatsComponents.NOfComponents, 1);
		expectedFor2members.put(PlainTextStatsComponents.NOfSources, 0);
		expectedFor2members.put(PlainTextStatsComponents.NOfSinks, 0);
		expectedFor2members.put(PlainTextStatsComponents.NOfWhites, 1);

		Map<PlainTextStatsComponents, Integer> expectedFor1members = new HashMap<PlainTextStatsComponents, Integer>();

		expectedFor1members.put(PlainTextStatsComponents.NOfComponents, 1);
		expectedFor1members.put(PlainTextStatsComponents.NOfSources, 0);
		expectedFor1members.put(PlainTextStatsComponents.NOfSinks, 0);
		expectedFor1members.put(PlainTextStatsComponents.NOfWhites, 1);

		Assert.assertTrue(recorder.isComponentsVotesEquals(3,
				expectedFor3members));
		Assert.assertTrue(recorder.isComponentsVotesEquals(2,
				expectedFor2members));
		Assert.assertTrue(recorder.isComponentsVotesEquals(1,
				expectedFor1members));

	}
}
