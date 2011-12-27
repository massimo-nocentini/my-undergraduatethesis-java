package model;

import java.io.Writer;
import java.util.Set;

import org.sbml.jsbml.Species;

import dotInterface.DotExportable;
import dotInterface.LineDecorator;

public interface Vertex extends DotExportable, Comparable<Vertex> {

	interface DoAction<T> {
		void apply(T item, String species_id, String species_name,
				String compartment_id);
	}

	Vertex addNeighbour(Vertex neighbour);

	void doOnNeighbors(VertexLogicApplier applier);

	void doOnNeighbors(VertexLogicApplierWithNeighborhoodRelation applier);

	boolean isYourNeighborhoodEquals(Set<Vertex> products);

	boolean isYourNeighborhoodEmpty();

	boolean isYourSpeciesId(String speciesId);

	boolean isYourCompartmentId(String compartmentId);

	boolean isYourSpeciesName(String species_name);

	@Override
	abstract int hashCode();

	@Override
	abstract boolean equals(Object obj);

	boolean haveYouSelfLoop();

	boolean isYourOrigin(Species aSpecies);

	boolean isSink();

	boolean isSource();

	boolean isYourNeighbour(Vertex a);

	boolean isNeighborsCountEquals(int guess);

	boolean matchCompartmentWith(Vertex otherVertex);

	boolean matchSpeciesWith(Vertex otherVertex);

	void collectYourDefinitionInto(Writer writer);

	LineDecorator getSourceDecorator();

	LineDecorator getIdentifierDecorator();

	LineDecorator getSquareBracketsDecorator();

	void collectEdgeDefinitionInto(Writer writer, Vertex neighbour);

	void collectVertexLabelOutsideBoxInto(Writer writer);

	// TODO: the following methods need to be unit tested each one
	int compareYourCompartmentIdWith(String compartment_id);

	int compareYourSpeciesIdWith(String species_id);

	void addDirectAncestors(Vertex vertex);

	void publishYourStatsOn(VertexStatsRecorder vertexStatsRecorder);

	void voteOn(VertexVoteAccepter voteAccepter);

	void brokeDirectAncestorRelationWith(Vertex source);

	void brokeYourNeighborhoodRelations();

	boolean isYourAncestorsEmpty();

	void doWithVertexType(DoAction<VertexType> doer);

	String buildVertexUniqueIdentifier();

	void doWithParentModel(DoAction<OurModel> action);

	void containedIn(OurModel model);

}
