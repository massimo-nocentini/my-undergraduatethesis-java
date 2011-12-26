package model;

import java.io.Writer;
import java.util.Set;

import org.sbml.jsbml.Species;

import dotInterface.DotExportable;
import dotInterface.LineDecorator;

public interface Vertex extends DotExportable, Comparable<Vertex> {

	public interface DoAction<T> {
		void apply(T item, String species_id, String species_name,
				String compartment_id);
	}

	public Vertex addNeighbour(Vertex neighbour);

	public void doOnNeighbors(VertexLogicApplier applier);

	public void doOnNeighbors(VertexLogicApplierWithNeighborhoodRelation applier);

	public boolean isYourNeighborhoodEquals(Set<Vertex> products);

	public boolean isYourNeighborhoodEmpty();

	public boolean isYourSpeciesId(String speciesId);

	public boolean isYourCompartmentId(String compartmentId);

	public boolean isYourSpeciesName(String species_name);

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object obj);

	public boolean haveYouSelfLoop();

	public boolean isYourOrigin(Species aSpecies);

	public boolean isSink();

	public boolean isSource();

	public boolean isYourNeighbour(Vertex a);

	public boolean isNeighborsCountEquals(int guess);

	public boolean matchCompartmentWith(Vertex otherVertex);

	public boolean matchSpeciesWith(Vertex otherVertex);

	public void collectYourDefinitionInto(Writer writer);

	public LineDecorator getSourceDecorator();

	public LineDecorator getIdentifierDecorator();

	public LineDecorator getSquareBracketsDecorator();

	public void collectEdgeDefinitionInto(Writer writer, Vertex neighbour);

	public void collectVertexLabelOutsideBoxInto(Writer writer);

	// TODO: the following methods need to be unit tested each one
	public int compareYourCompartmentIdWith(String compartment_id);

	public int compareYourSpeciesIdWith(String species_id);

	public void addDirectAncestors(Vertex vertex);

	public void publishYourStatsOn(VertexStatsRecorder vertexStatsRecorder);

	public void voteOn(VertexVoteAccepter voteAccepter);

	public void brokeDirectAncestorRelationWith(Vertex source);

	public void brokeYourNeighborhoodRelations();

	public boolean isYourAncestorsEmpty();

	public void doWithVertexType(DoAction<VertexType> doer);

	public String buildVertexUniqueIdentifier();

}
