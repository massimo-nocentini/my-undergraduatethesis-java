package model;

import java.util.HashSet;
import java.util.Set;

import org.sbml.jsbml.Species;

import dotInterface.DotExportable;
import dotInterface.DotExporter;
import dotInterface.Edge;
import dotInterface.VertexDotInfoProvider;

public class Vertex implements DotExportable, VertexDotInfoProvider {

	static String IdPrefix = "id_";

	static class VertexInstancesCounter {
		private static int count;

		static {
			count = 0;
		}

		synchronized static int makeNewId() {
			count = count + 1;
			return count;
		}

		static int getCount() {
			return count;
		}
	}

	private Set<Vertex> neighbors;

	private String species_id;

	private String compartment_id;

	private Set<Vertex> directAncestors;

	private Vertex(String id) {
		this.species_id = id;
		neighbors = new HashSet<Vertex>();
		directAncestors = new HashSet<Vertex>();
	}

	public static Vertex makeVertex() {
		int id = VertexInstancesCounter.makeNewId();
		return Vertex.makeVertex(IdPrefix + String.valueOf(id));
	}

	public Vertex addNeighbour(Vertex neighbour) {
		this.neighbors.add(neighbour);
		neighbour.directAncestors.add(this);
		return this;
	}

	public void doOnNeighbours(INeighbourApplier applier) {
		for (Vertex vertex : this.neighbors) {
			applier.apply(vertex);
		}

	}

	public static Vertex makeVertex(String id) {
		return new Vertex(id);
	}

	// TODO: this method have no more sense to exists
	public String getId() {
		return species_id;
	}

	public boolean isYourNeighborhoodEquals(Set<Vertex> products) {
		return this.neighbors.equals(products);
	}

	public boolean isYourNeighborhoodEmpty() {
		return this.neighbors.size() == 0;
	}

	public static Vertex makeVertex(Species species) {
		Vertex result = Vertex.makeVertex();

		result.species_id = species.getId();
		result.compartment_id = species.getCompartment();

		return result;
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

	@Override
	public String provideId() {
		return species_id.trim().concat(compartment_id.trim());
	}

	public boolean isSink() {
		// TODO: ask if this condition is sufficient for the truth of this
		// predicate
		return neighbors.size() == 0;// && directAncestors.size() > 0;
	}

	public boolean isSource() {
		return directAncestors.size() == 0 && neighbors.size() > 0;
	}

	@Override
	public Vertex getVertexInstance() {
		return this;
	}

	public boolean isYourNeighbour(Vertex a) {
		return neighbors.contains(a);
	}

	public int countNeighbors() {
		return neighbors.size();
	}
}
