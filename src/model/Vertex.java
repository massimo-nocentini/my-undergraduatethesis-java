package model;

import java.io.Writer;
import java.util.Set;

import org.sbml.jsbml.Species;

import dotInterface.DotExportable;
import dotInterface.DotExporter;
import dotInterface.LineDecorator;

public interface Vertex extends DotExportable, Comparable<Vertex> {

	public abstract Vertex addNeighbour(Vertex neighbour);

	public abstract void doOnNeighbors(VertexLogicApplier applier);

	public abstract void doOnNeighbors(
			VertexLogicApplierWithNeighborhoodRelation applier);

	public abstract boolean isYourNeighborhoodEquals(Set<Vertex> products);

	public abstract boolean isYourNeighborhoodEmpty();

	public abstract boolean isYourSpeciesId(String speciesId);

	public abstract boolean isYourCompartmentId(String compartmentId);

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object obj);

	public abstract boolean haveYouSelfLoop();

	public abstract boolean isYourOrigin(Species aSpecies);

	@Override
	public abstract void acceptExporter(DotExporter exporter);

	public abstract boolean isSink();

	public abstract boolean isSource();

	public abstract boolean isYourNeighbour(Vertex a);

	public abstract boolean isNeighborsCountEquals(int guess);

	public abstract boolean matchCompartmentWith(Vertex otherVertex);

	public abstract boolean matchSpeciesWith(Vertex otherVertex);

	public abstract void collectYourDefinitionInto(Writer writer);

	public abstract LineDecorator getSourceDecorator();

	public abstract LineDecorator getIdentifierDecorator();

	public abstract LineDecorator getSquareBracketsDecorator();

	public abstract void collectEdgeDefinitionInto(Writer writer,
			Vertex neighbour);

	public void collectVertexLabelOutsideBoxInto(Writer writer);

	// TODO: the following methods need to be unit tested each one
	public abstract int compareYourCompartmentIdWith(String compartment_id);

	public abstract int compareYourSpeciesIdWith(String species_id);

	public abstract void addDirectAncestors(Vertex vertex);

	public abstract SimpleVertex asSimpleVertex();

	public abstract void publishYourStatsOn(
			VertexStatsRecorder vertexStatsRecorder);

	public abstract void brokeDirectAncestorRelationWith(Vertex source);

	public abstract void brokeYourNeighborhoodRelations();

	public abstract boolean isYourAncestorsEmpty();

}
