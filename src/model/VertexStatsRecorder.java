package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class VertexStatsRecorder {
	Map<PlainTextStatsComponents, IntegerCounter> map;

	public VertexStatsRecorder() {
		map = new HashMap<PlainTextStatsComponents, IntegerCounter>();

		for (PlainTextStatsComponents component : PlainTextStatsComponents
				.values()) {

			map.put(component, new IntegerCounter());
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

	public Map<PlainTextStatsComponents, Integer> collectVotes() {
		Map<PlainTextStatsComponents, Integer> result = new HashMap<PlainTextStatsComponents, Integer>();

		for (Entry<PlainTextStatsComponents, IntegerCounter> entry : map
				.entrySet()) {
			result.put(entry.getKey(), entry.getValue().getCount());
		}

		return result;
	}

	public void recordSimpleVertex(int edges, boolean source, boolean sink,
			boolean white) {

		map.get(PlainTextStatsComponents.NumberOfVertices).increment();

		map.get(PlainTextStatsComponents.NumberOfEdges).increment(edges);
		if (source) {
			map.get(PlainTextStatsComponents.NumberOfSources).increment();
		}
		if (sink) {
			map.get(PlainTextStatsComponents.NumberOfSinks).increment();
		}
		if (white) {
			map.get(PlainTextStatsComponents.NumberOfWhites).increment();
		}
	}

}
