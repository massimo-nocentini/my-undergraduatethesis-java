package model;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;
import java.util.TreeSet;

import org.sbml.jsbml.Species;

import dotInterface.DotDecorationApplier;
import dotInterface.DotExporter;
import dotInterface.Edge;

public class SimpleVertex implements Vertex {

	/**
	 * Dummy value
	 */
	private static String DummySpeciesId = "dummy_species_id";
	private static String DummyCompartmentId = "dummy_compartment_id";

	static class VertexIntegerEnumerator {
		private static int count;

		static {
			count = 0;
		}

		synchronized static int enumerateNewVertex() {
			count = count + 1;
			return count;
		}

		static int getCurrentEnumerationValue() {
			return count;
		}
	}

	public class SimpleFormatter implements VertexFormatter {

		private SimpleFormatter() {
		}

		@Override
		public VertexFormatter formatVertexDefinitionInto(Writer writer,
				Vertex vertex, DotDecorationApplier useDecorationApplier) {

			StringBuilder sourceIdentifierStringBuilder = new StringBuilder();

			vertex.collectYourIdentifierInto(sourceIdentifierStringBuilder);

			String vertexRepresentation = sourceIdentifierStringBuilder
					.toString();

			if (isSink() || isSource()) {

				vertexRepresentation = useDecorationApplier
						.decoreWithSourceSinkAttributes(vertexRepresentation);
			}

			try {
				writer.append(vertexRepresentation);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return this;
		}

		@Override
		public VertexFormatter formatEdgeDefinitionInto(Writer writer,
				Vertex source, Vertex neighbour,
				DotDecorationApplier dotDecorationApplier) {

			StringBuilder sourceIdentifierStringBuilder = new StringBuilder();
			StringBuilder neighbourIdentifierStringBuilder = new StringBuilder();

			source.collectYourIdentifierInto(sourceIdentifierStringBuilder);

			neighbour
					.collectYourIdentifierInto(neighbourIdentifierStringBuilder);

			String composedString = dotDecorationApplier
					.buildInfixNeighborhoodRelation(
							sourceIdentifierStringBuilder.toString(),
							neighbourIdentifierStringBuilder.toString());

			try {
				writer.append(composedString);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return this;
		}
	}

	public VertexFormatter useFormatter() {
		return new SimpleFormatter();
	}

	private Set<Vertex> neighbors;

	private String species_id;

	private String compartment_id;

	private Set<Vertex> directAncestors;

	private SimpleVertex(String species_id, String compartment_id) {
		this.species_id = species_id;
		this.compartment_id = compartment_id;

		neighbors = new TreeSet<Vertex>();
		directAncestors = new TreeSet<Vertex>();
	}

	public static Vertex makeVertex() {
		int id = VertexIntegerEnumerator.enumerateNewVertex();

		Vertex createdVertex = makeVertex(
				DummySpeciesId.concat(String.valueOf(id)), DummyCompartmentId);

		return createdVertex;
	}

	public Vertex addNeighbour(Vertex neighbour) {
		this.neighbors.add(neighbour);
		neighbour.addDirectAncestors(this);
		return this;
	}

	public void doOnNeighbors(VertexLogicApplier applier) {
		for (Vertex vertex : neighbors) {
			applier.apply(vertex);
		}

	}

	public void doOnNeighbors(VertexLogicApplierWithNeighborhoodRelation applier) {
		for (Vertex neighbour : neighbors) {
			applier.apply(this, neighbour);
		}

	}

	public static Vertex makeVertex(String species_id, String compartment_id) {
		return new SimpleVertex(species_id, compartment_id);
	}

	public boolean isYourNeighborhoodEquals(Set<Vertex> products) {
		return this.neighbors.equals(products);
	}

	public boolean isYourNeighborhoodEmpty() {
		return this.neighbors.size() == 0;
	}

	public static Vertex makeVertex(Species species) {
		return makeVertex(species.getId(), species.getCompartment());
	}

	public boolean isYourSpeciesId(String speciesId) {
		return this.species_id.equals(speciesId);
	}

	public boolean isYourCompartmentId(String compartmentId) {
		return this.compartment_id.equals(compartmentId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((compartment_id == null) ? 0 : compartment_id.hashCode());
		result = prime * result
				+ ((species_id == null) ? 0 : species_id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof SimpleVertex)) {
			return false;
		}
		SimpleVertex other = (SimpleVertex) obj;
		if (compartment_id == null) {
			if (other.compartment_id != null) {
				return false;
			}
		} else if (compartment_id.equals(other.compartment_id) == false) {
			return false;
		}
		if (species_id == null) {
			if (other.species_id != null) {
				return false;
			}
		} else if (species_id.equals(other.species_id) == false) {
			return false;
		}
		return true;
	}

	public boolean haveYouSelfLoop() {
		return this.neighbors.contains(this);
	}

	public boolean isYourOrigin(Species aSpecies) {
		return this.isYourSpeciesId(aSpecies.getId())
				&& this.isYourCompartmentId(aSpecies.getCompartment());
	}

	@Override
	public void acceptExporter(DotExporter exporter) {
		exporter.buildVertexDefinition(this);

		for (Vertex neighbour : neighbors) {
			exporter.buildEdgeDefinition(Edge.makeEdge(this, neighbour));
		}
	}

	public boolean isSink() {
		// TODO: ask if this condition is sufficient for the truth of this
		// predicate
		return neighbors.size() == 0;// && directAncestors.size() > 0;
	}

	public boolean isSource() {
		return directAncestors.size() == 0 && neighbors.size() > 0;
	}

	public boolean isYourNeighbour(Vertex a) {
		return neighbors.contains(a);
	}

	public boolean isNeighborsCountEquals(int guess) {
		return neighbors.size() == guess;
	}

	public boolean matchCompartmentWith(Vertex otherVertex) {
		return otherVertex.isYourCompartmentId(compartment_id);
	}

	public boolean matchSpeciesWith(Vertex otherVertex) {
		return otherVertex.isYourSpeciesId(species_id);
	}

	@Override
	public int compareTo(Vertex o) {
		int comparison = o.compareYourCompartmentIdWith(compartment_id);

		if (comparison == 0) {
			comparison = o.compareYourSpeciesIdWith(species_id);
		}

		return -1 * comparison;
	}

	public static Vertex makeVertex(Vertex vertex) {

		return makeVertex(vertex.asSimpleVertex().species_id,
				vertex.asSimpleVertex().compartment_id);
	}

	@Override
	public int compareYourCompartmentIdWith(String compartment_id) {
		return this.compartment_id.compareTo(compartment_id);
	}

	@Override
	public int compareYourSpeciesIdWith(String species_id) {
		return this.species_id.compareTo(species_id);
	}

	@Override
	public void addDirectAncestors(Vertex vertex) {

		// TODO: controllare se per completezza si dovrebbe anche invocare
		// vertex.addNeighbour(this)
		// ma potrebbe provocare una sequenza ricorsiva infinita di chiamate.
		this.directAncestors.add(vertex);
	}

	@Override
	public void collectYourIdentifierInto(StringBuilder collectingBuilder) {
		collectingBuilder.append(species_id.trim()
				.concat(compartment_id.trim()));
	}

	@Override
	public SimpleVertex asSimpleVertex() {
		return this;
	}

}
