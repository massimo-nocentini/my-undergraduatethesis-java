package model;

import java.util.Set;

import org.sbml.jsbml.Species;

import dotInterface.DotExportable;
import dotInterface.DotExporter;

public interface Vertex extends DotExportable, Comparable<Vertex> {

	public abstract VertexFormatter useFormatter();

	public abstract Vertex addNeighbour(Vertex neighbour);

	public abstract void doOnNeighbors(VertexLogicApplier applier);

	public abstract void doOnNeighbors(
			VertexLogicApplierWithNeighborhoodRelation applier);

	public abstract boolean isYourNeighborhoodEquals(Set<Vertex> products);

	public abstract boolean isYourNeighborhoodEmpty();

	public abstract boolean isYourSpeciesId(String speciesId);

	public abstract boolean isYourCompartmentId(String compartmentId);

	public abstract int hashCode();

	public abstract boolean equals(Object obj);

	public abstract boolean haveYouSelfLoop();

	public abstract boolean isYourOrigin(Species aSpecies);

	public abstract void acceptExporter(DotExporter exporter);

	public abstract boolean isSink();

	public abstract boolean isSource();

	public abstract boolean isYourNeighbour(Vertex a);

	public abstract boolean isNeighborsCountEquals(int guess);

	public abstract boolean matchCompartmentWith(Vertex otherVertex);

	public abstract boolean matchSpeciesWith(Vertex otherVertex);

	// TODO: these methods need to be unit tested each one
	public abstract int compareYourCompartmentIdWith(String compartment_id);

	public abstract int compareYourSpeciesIdWith(String species_id);

	public abstract void addDirectAncestors(Vertex vertex);

	// TODO: delete this method from this interface because it allow
	// to retrieve some encapsulated information. Make a private method
	// in Vertex class that provide the collection of the composite identifier
	// in order to be called directly from another object of typeSimpleVertex.
	public abstract void collectYourIdentifierInto(
			StringBuilder collectingBuilder);

	public abstract SimpleVertex asSimpleVertex();

}
