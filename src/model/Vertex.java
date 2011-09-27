package model;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;
import java.util.TreeSet;

import org.sbml.jsbml.Species;

import dotInterface.DotDecorationApplier;
import dotInterface.DotExportable;
import dotInterface.DotExporter;
import dotInterface.Edge;

public class Vertex implements DotExportable, Comparable<Vertex> {

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

	public class Formatter {

		private Formatter() {
		}

		public Formatter formatVertexDefinitionInto(Writer writer,
				DotDecorationApplier useDecorationApplier) {

			String vertexRepresentation = species_id.trim().concat(
					compartment_id.trim());

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

		public Formatter formatEdgeDefinitionInto(Writer writer,
				Vertex neighbour, DotDecorationApplier dotDecorationApplier) {

			String myComposedId = species_id.trim().concat(
					compartment_id.trim());

			String neighbourComposedId = neighbour.species_id.trim().concat(
					neighbour.compartment_id.trim());

			String composedString = dotDecorationApplier
					.buildInfixNeighborhoodRelation(myComposedId,
							neighbourComposedId);

			try {
				writer.append(composedString);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return this;

		}

	}

	public Formatter useFormatter() {
		return new Formatter();
	}

	private Set<Vertex> neighbors;

	private String species_id;

	private String compartment_id;

	private Set<Vertex> directAncestors;

	private Vertex(String species_id, String compartment_id) {
		this.species_id = species_id;
		this.compartment_id = compartment_id;

		neighbors = new TreeSet<Vertex>();
		directAncestors = new TreeSet<Vertex>();
	}

	public static Vertex makeVertex() {
		int id = VertexIntegerEnumerator.enumerateNewVertex();

		Vertex createdVertex = Vertex.makeVertex(
				Vertex.DummySpeciesId.concat(String.valueOf(id)),
				Vertex.DummyCompartmentId);

		return createdVertex;
	}

	public Vertex addNeighbour(Vertex neighbour) {
		this.neighbors.add(neighbour);
		neighbour.directAncestors.add(this);
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
		return new Vertex(species_id, compartment_id);
	}

	public boolean isYourNeighborhoodEquals(Set<Vertex> products) {
		return this.neighbors.equals(products);
	}

	public boolean isYourNeighborhoodEmpty() {
		return this.neighbors.size() == 0;
	}

	public static Vertex makeVertex(Species species) {
		return Vertex.makeVertex(species.getId(), species.getCompartment());
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
		if (!(obj instanceof Vertex)) {
			return false;
		}
		Vertex other = (Vertex) obj;
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
		return compartment_id.equals(otherVertex.compartment_id);
	}

	public boolean matchSpeciesWith(Vertex otherVertex) {
		return species_id.equals(otherVertex.species_id);
	}

	@Override
	public int compareTo(Vertex o) {
		int comparison = this.compartment_id.compareTo(o.compartment_id);

		if (comparison == 0) {
			comparison = this.species_id.compareTo(o.species_id);
		}

		return comparison;
	}

	public static Vertex cloneOnlyCharacteristicsFields(Vertex vertex) {

		return Vertex.makeVertex(vertex.species_id, vertex.compartment_id);
	}
}
