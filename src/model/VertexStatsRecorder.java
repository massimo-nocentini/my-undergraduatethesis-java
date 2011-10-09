package model;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import dotInterface.DotFileUtilHandler;

public class VertexStatsRecorder {

	// Map<PlainTextStatsComponents, IntegerCounter> simpleVertexVotesMap;

	private final SimpleVertexVoteAccepter simpleVertexVoteAccepter;

	private final Map<Integer, ComponentVoteAccepter> componentsVoteAccepter;

	public VertexStatsRecorder() {

		simpleVertexVoteAccepter = new SimpleVertexVoteAccepter();
		componentsVoteAccepter = new HashMap<Integer, VertexStatsRecorder.ComponentVoteAccepter>();

	}

	static class SimpleVertexVoteAccepter {

		private final Map<PlainTextStatsComponents, IntegerCounter> simpleVertexVotesMap;

		public SimpleVertexVoteAccepter() {

			simpleVertexVotesMap = new LinkedHashMap<PlainTextStatsComponents, VertexStatsRecorder.IntegerCounter>();

			simpleVertexVotesMap.put(PlainTextStatsComponents.NOfVertices,
					new IntegerCounter());

			simpleVertexVotesMap.put(PlainTextStatsComponents.NOfEdges,
					new IntegerCounter());

			simpleVertexVotesMap.put(PlainTextStatsComponents.NOfSources,
					new IntegerCounter());

			simpleVertexVotesMap.put(PlainTextStatsComponents.NOfSinks,
					new IntegerCounter());

			simpleVertexVotesMap.put(PlainTextStatsComponents.NOfWhites,
					new IntegerCounter());
		}

		private void pushVertex() {
			simpleVertexVotesMap.get(PlainTextStatsComponents.NOfVertices)
					.increment();
		}

		public void pushEdges(int neighbors) {
			simpleVertexVotesMap.get(PlainTextStatsComponents.NOfEdges)
					.increment(neighbors);
		}

		public void pushSink() {
			simpleVertexVotesMap.get(PlainTextStatsComponents.NOfSinks)
					.increment();
		}

		public void pushSource() {
			simpleVertexVotesMap.get(PlainTextStatsComponents.NOfSources)
					.increment();
		}

		public void pushWhite() {
			simpleVertexVotesMap.get(PlainTextStatsComponents.NOfWhites)
					.increment();
		}

		public void writeOn(Writer writer, String prefix) {
			StringBuilder line = new StringBuilder();

			for (Entry<PlainTextStatsComponents, IntegerCounter> component : simpleVertexVotesMap
					.entrySet()) {

				line.append(prefix
						+ component.getKey().name()
						+ ":"
						+ DotFileUtilHandler.getTabString()
								.concat(String.valueOf(component.getValue()
										.getCount())));

				line.append(DotFileUtilHandler.getNewLineSeparator());

			}

			try {

				writer.append(line.toString().concat(
						DotFileUtilHandler.getNewLineSeparator()));

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	static class ComponentVoteAccepter {

		private final Map<PlainTextStatsComponents, IntegerCounter> componentVotesMap;

		public ComponentVoteAccepter() {

			componentVotesMap = new LinkedHashMap<PlainTextStatsComponents, VertexStatsRecorder.IntegerCounter>();

			componentVotesMap.put(PlainTextStatsComponents.NOfComponents,
					new IntegerCounter());

			componentVotesMap.put(PlainTextStatsComponents.NOfSources,
					new IntegerCounter());

			componentVotesMap.put(PlainTextStatsComponents.NOfSinks,
					new IntegerCounter());

			componentVotesMap.put(PlainTextStatsComponents.NOfWhites,
					new IntegerCounter());
		}

		private void pushComponent() {
			componentVotesMap.get(PlainTextStatsComponents.NOfComponents)
					.increment();
		}

		public void pushSink() {
			componentVotesMap.get(PlainTextStatsComponents.NOfSinks)
					.increment();
		}

		public void pushSource() {
			componentVotesMap.get(PlainTextStatsComponents.NOfSources)
					.increment();
		}

		public void pushWhite() {
			componentVotesMap.get(PlainTextStatsComponents.NOfWhites)
					.increment();
		}

		public void writeOn(Writer writer, String prefix) {
			StringBuilder line = new StringBuilder();

			for (Entry<PlainTextStatsComponents, IntegerCounter> component : componentVotesMap
					.entrySet()) {

				line.append(prefix
						+ component.getKey().name()
						+ ":"
						+ DotFileUtilHandler.getTabString()
								.concat(String.valueOf(component.getValue()
										.getCount())));

				line.append(DotFileUtilHandler.getNewLineSeparator());

			}

			try {

				writer.append(line.toString().concat(
						DotFileUtilHandler.getNewLineSeparator()));

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private static class IntegerCounter {
		private int count = 0;

		IntegerCounter increment() {
			count = count + 1;
			return this;
		}

		public void increment(int edges) {
			for (int i = 0; i < edges; i = i + 1) {
				increment();
			}
		}

		public Integer getCount() {
			return count;
		}
	}

	private Map<PlainTextStatsComponents, Integer> collectVotes(
			Map<PlainTextStatsComponents, IntegerCounter> input) {
		Map<PlainTextStatsComponents, Integer> result = new HashMap<PlainTextStatsComponents, Integer>();

		for (Entry<PlainTextStatsComponents, IntegerCounter> entry : input
				.entrySet()) {

			result.put(entry.getKey(), entry.getValue().getCount());
		}

		return result;
	}

	public SimpleVertexVoteAccepter recordSimpleVertex() {

		simpleVertexVoteAccepter.pushVertex();
		return simpleVertexVoteAccepter;
	}

	public void writeOn(Writer writer) {

		simpleVertexVoteAccepter.writeOn(writer,
				DotFileUtilHandler.getTabString());

		for (Entry<Integer, ComponentVoteAccepter> entry : componentsVoteAccepter
				.entrySet()) {

			try {
				writer.append(PlainTextStatsComponents.NOfVertices.name()
						+ ": " + String.valueOf(entry.getKey())
						+ DotFileUtilHandler.getNewLineSeparator());

				entry.getValue().writeOn(writer,
						DotFileUtilHandler.getTabString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public ComponentVoteAccepter recordComponent(int members) {

		ComponentVoteAccepter componentVoteAccepter = null;

		if (componentsVoteAccepter.containsKey(members) == false) {
			componentVoteAccepter = new ComponentVoteAccepter();
			componentsVoteAccepter.put(members, componentVoteAccepter);
		} else {
			componentVoteAccepter = componentsVoteAccepter.get(members);
		}

		componentVoteAccepter.pushComponent();

		return componentVoteAccepter;
	}

	public boolean isSimpleVerticesVotesEquals(
			Map<PlainTextStatsComponents, Integer> expected) {

		return collectVotes(simpleVertexVoteAccepter.simpleVertexVotesMap)
				.equals(expected);
	}

	public boolean isComponentsVotesEquals(int i,
			Map<PlainTextStatsComponents, Integer> other) {

		return collectVotes(componentsVoteAccepter.get(i).componentVotesMap)
				.equals(other);
	}
}
