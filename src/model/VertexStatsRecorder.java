package model;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import dotInterface.DotFileUtilHandler;

public class VertexStatsRecorder {

	private final VertexVoteAccepter simpleVertexVoteAccepter;

	private final Map<Integer, VertexVoteAccepter> componentsVoteAccepterByCCCardinality;

	public VertexStatsRecorder() {

		simpleVertexVoteAccepter = VertexVoteAccepter
				.MakeVoteAccepterForSimpleVertices();

		componentsVoteAccepterByCCCardinality = new HashMap<Integer, VertexVoteAccepter>();

	}

	public void writeOn(Writer writer) {

		if (simpleVertexVoteAccepter.isEmpty() == false) {

			// if the simple vertex accepter have at least one vote for one
			// component we can print it
			simpleVertexVoteAccepter.writeOn(writer,
					DotFileUtilHandler.getTabString());
		}

		for (Entry<Integer, VertexVoteAccepter> entry : componentsVoteAccepterByCCCardinality
				.entrySet()) {

			if (entry.getValue().isEmpty() == true) {

				// we draw only if the vote accepter has all its components
				// empty
				continue;
			}

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

	public VertexVoteAccepter recordSimpleVertex() {

		return simpleVertexVoteAccepter.startVoteRequest();
	}

	public VertexVoteAccepter recordConnectedComponent(int cardinality) {

		VertexVoteAccepter voteAccepter = null;

		if (componentsVoteAccepterByCCCardinality.containsKey(cardinality) == false) {

			voteAccepter = VertexVoteAccepter
					.MakeVoteAccepterForConnectedComponents();

			componentsVoteAccepterByCCCardinality
					.put(cardinality, voteAccepter);
		} else {
			voteAccepter = componentsVoteAccepterByCCCardinality
					.get(cardinality);
		}

		return voteAccepter.startVoteRequest();

	}

	public boolean isSimpleVerticesVotesEquals(
			Map<PlainTextStatsComponents, Integer> expected) {

		return simpleVertexVoteAccepter.areVotesEquals(expected);
	}

	public boolean isComponentsVotesEquals(int cardinality,
			Map<PlainTextStatsComponents, Integer> expected) {

		return componentsVoteAccepterByCCCardinality.get(cardinality)
				.areVotesEquals(expected);
	}
}
