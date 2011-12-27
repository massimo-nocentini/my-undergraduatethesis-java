package model;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import util.IntegerCounter;
import dotInterface.DotFileUtilHandler;

public class VertexVoteAccepter {

	static VertexVoteAccepter MakeVoteAccepterForConnectedComponents() {
		return new VertexVoteAccepter(
				new ConnectedComponentVertexVoteAccepterHookMethodsSupplier());
	}

	static VertexVoteAccepter MakeVoteAccepterForSimpleVertices() {

		return new VertexVoteAccepter(
				new SimpleVertexVoteAccepterHookMethodsSupplier());
	}

	private final VertexVoteAccepterHookMethods hookMethodsInterface;
	private final Map<PlainTextStatsComponents, IntegerCounter> votesMap;

	private VertexVoteAccepter(
			VertexVoteAccepterHookMethods hookMethodsInterface) {

		// here we use a LinkedHashMap to be able to have the components
		// enumerated in the order in which they are inserted in the map,
		// namely the order under.
		this.votesMap = new LinkedHashMap<PlainTextStatsComponents, IntegerCounter>();

		this.hookMethodsInterface = hookMethodsInterface;

		initializeVoteMap(votesMap);
	}

	private void initializeVoteMap(
			Map<PlainTextStatsComponents, IntegerCounter> map) {

		// with this polymorphic message we give the possibility to the
		// specialized object that supply the hook methods to put in in the
		// map its dedicated component.
		this.hookMethodsInterface.putDedicateComponent(map);

		map.put(PlainTextStatsComponents.NOfVertices, new IntegerCounter());

		// the following four components are required for both
		// characterization, that is for simple and component vertices
		map.put(PlainTextStatsComponents.NOfEdges, new IntegerCounter());
		map.put(PlainTextStatsComponents.NOfSources, new IntegerCounter());
		map.put(PlainTextStatsComponents.NOfSinks, new IntegerCounter());
		map.put(PlainTextStatsComponents.NOfWhites, new IntegerCounter());
	}

	public void pushEdges(int neighbors) {
		votesMap.get(PlainTextStatsComponents.NOfEdges).increment(neighbors);
	}

	public void pushSink() {
		votesMap.get(PlainTextStatsComponents.NOfSinks).increment();
	}

	public void pushSource() {
		votesMap.get(PlainTextStatsComponents.NOfSources).increment();
	}

	public void pushWhite() {
		votesMap.get(PlainTextStatsComponents.NOfWhites).increment();
	}

	public void writeOn(Writer writer, String prefix) {

		StringBuilder line = new StringBuilder();

		for (Entry<PlainTextStatsComponents, IntegerCounter> entry : votesMap
				.entrySet()) {

			if (hookMethodsInterface.ignoreComponent(entry.getKey()) == true) {
				continue;
			}

			line.append(prefix
					+ entry.getKey().name()
					+ ":"
					+ DotFileUtilHandler.getTabString().concat(
							String.valueOf(entry.getValue().getCount())));

			line.append(DotFileUtilHandler.getNewLineSeparator());

		}

		try {

			writer.append(line.toString().concat(
					DotFileUtilHandler.getNewLineSeparator()));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean isEmpty() {

		boolean result = true;
		for (Entry<PlainTextStatsComponents, IntegerCounter> entry : votesMap
				.entrySet()) {

			result = result & (entry.getValue().getCount() == 0);
		}

		return result;
	}

	public VertexVoteAccepter startVoteRequest() {

		hookMethodsInterface.characteristicComponentRequested(votesMap);

		return this;
	}

	public boolean areVotesEquals(
			Map<PlainTextStatsComponents, Integer> expected) {

		return collectVotes(votesMap).equals(expected);
	}

	private Map<PlainTextStatsComponents, Integer> collectVotes(
			Map<PlainTextStatsComponents, IntegerCounter> input) {

		Map<PlainTextStatsComponents, Integer> result = new HashMap<PlainTextStatsComponents, Integer>();

		for (Entry<PlainTextStatsComponents, IntegerCounter> entry : input
				.entrySet()) {

			if (hookMethodsInterface.ignoreComponent(entry.getKey())) {
				continue;
			}

			result.put(entry.getKey(), entry.getValue().getCount());
		}

		return result;
	}

	interface VertexVoteAccepterHookMethods {

		void putDedicateComponent(
				Map<PlainTextStatsComponents, IntegerCounter> map);

		boolean ignoreComponent(PlainTextStatsComponents key);

		void characteristicComponentRequested(
				Map<PlainTextStatsComponents, IntegerCounter> votesMap);

		int supplyValueOfCharacteristicComponent(
				Map<PlainTextStatsComponents, IntegerCounter> votesMap);
	}

	static class SimpleVertexVoteAccepterHookMethodsSupplier implements
			VertexVoteAccepterHookMethods {

		// here we fix the characteristic component that this class have the
		// responsibility to encapsulate
		private final PlainTextStatsComponents characteristicComponent = PlainTextStatsComponents.NOfVertices;

		@Override
		public void putDedicateComponent(
				Map<PlainTextStatsComponents, IntegerCounter> map) {

			map.put(characteristicComponent, new IntegerCounter());
		}

		@Override
		public void characteristicComponentRequested(
				Map<PlainTextStatsComponents, IntegerCounter> votesMap) {

			votesMap.get(characteristicComponent).increment();
		}

		@Override
		public boolean ignoreComponent(PlainTextStatsComponents key) {

			return PlainTextStatsComponents.NOfComponents.equals(key);
		}

		@Override
		public int supplyValueOfCharacteristicComponent(
				Map<PlainTextStatsComponents, IntegerCounter> votesMap) {

			return votesMap.get(characteristicComponent).getCount();
		}
	}

	static class ConnectedComponentVertexVoteAccepterHookMethodsSupplier
			implements VertexVoteAccepterHookMethods {

		// here we fix the characteristic component that this class have the
		// responsibility to encapsulate
		private final PlainTextStatsComponents characteristicComponent = PlainTextStatsComponents.NOfComponents;

		@Override
		public void putDedicateComponent(
				Map<PlainTextStatsComponents, IntegerCounter> map) {

			map.put(characteristicComponent, new IntegerCounter());
		}

		@Override
		public void characteristicComponentRequested(
				Map<PlainTextStatsComponents, IntegerCounter> votesMap) {

			votesMap.get(characteristicComponent).increment();
		}

		@Override
		public boolean ignoreComponent(PlainTextStatsComponents key) {

			return PlainTextStatsComponents.NOfVertices.equals(key);
		}

		@Override
		public int supplyValueOfCharacteristicComponent(
				Map<PlainTextStatsComponents, IntegerCounter> votesMap) {

			return votesMap.get(characteristicComponent).getCount();
		}
	}

	public int getGroupTotal() {

		return hookMethodsInterface
				.supplyValueOfCharacteristicComponent(votesMap);
	}

	public int sumVerticesVotesOverTypePartition() {

		Set<PlainTextStatsComponents> inclusionComponents = new HashSet<PlainTextStatsComponents>();
		inclusionComponents.add(PlainTextStatsComponents.NOfSinks);
		inclusionComponents.add(PlainTextStatsComponents.NOfWhites);
		inclusionComponents.add(PlainTextStatsComponents.NOfSources);

		int result = 0;

		for (Entry<PlainTextStatsComponents, IntegerCounter> entry : votesMap
				.entrySet()) {

			if (inclusionComponents.contains(entry.getKey()) == false) {
				continue;
			}

			result = result + entry.getValue().getCount();
		}

		return result;

	}

	public VertexVoteAccepter average(Integer counter) {

		VertexVoteAccepter vertexVoteAccepter = new VertexVoteAccepter(
				this.hookMethodsInterface);

		for (Entry<PlainTextStatsComponents, IntegerCounter> entry : votesMap
				.entrySet()) {

			// here we have already the entry.getKey() inside the map of the new
			// object because the right entries are added in the constructor
			// using the same hook methods.
			vertexVoteAccepter.votesMap.get(entry.getKey()).increment(
					entry.getValue().getCount() / counter);

		}

		return vertexVoteAccepter;
	}
}
