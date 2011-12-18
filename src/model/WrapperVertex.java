package model;

import java.io.Writer;
import java.util.Set;

import org.sbml.jsbml.Species;

import dotInterface.DotExporter;
import dotInterface.LineDecorator;

public abstract class WrapperVertex implements Vertex {

	private final Vertex wrappedVertex;

	@Override
	public void collectVertexLabelOutsideBoxInto(Writer writer) {
		wrappedVertex.collectVertexLabelOutsideBoxInto(writer);
	}

	protected WrapperVertex(Vertex wrappingVertex) {
		wrappedVertex = wrappingVertex;
	}

	Vertex getWrappedVertex() {
		return wrappedVertex;
	}

	@Override
	public Vertex addNeighbour(Vertex neighbour) {
		return wrappedVertex.addNeighbour(neighbour);
	}

	@Override
	public void doOnNeighbors(VertexLogicApplier applier) {
		wrappedVertex.doOnNeighbors(applier);
	}

	@Override
	public void doOnNeighbors(VertexLogicApplierWithNeighborhoodRelation applier) {
		wrappedVertex.doOnNeighbors(applier);
	}

	@Override
	public boolean isYourNeighborhoodEquals(Set<Vertex> products) {
		return wrappedVertex.isYourNeighborhoodEquals(products);
	}

	@Override
	public boolean isYourNeighborhoodEmpty() {
		return wrappedVertex.isYourNeighborhoodEmpty();
	}

	@Override
	public boolean isYourSpeciesId(String speciesId) {
		return wrappedVertex.isYourSpeciesId(speciesId);
	}

	@Override
	public boolean isYourCompartmentId(String compartmentId) {
		return wrappedVertex.isYourCompartmentId(compartmentId);
	}

	@Override
	public int hashCode() {
		return wrappedVertex.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return wrappedVertex.equals(obj);
	}

	@Override
	public boolean haveYouSelfLoop() {
		return wrappedVertex.haveYouSelfLoop();
	}

	@Override
	public boolean isYourOrigin(Species aSpecies) {
		return wrappedVertex.isYourOrigin(aSpecies);
	}

	@Override
	public void acceptExporter(DotExporter exporter) {
		wrappedVertex.acceptExporter(exporter);
	}

	@Override
	public boolean isSink() {
		return wrappedVertex.isSink();
	}

	@Override
	public boolean isSource() {
		return wrappedVertex.isSource();
	}

	@Override
	public boolean isYourNeighbour(Vertex a) {
		return wrappedVertex.isYourNeighbour(a);
	}

	@Override
	public boolean isNeighborsCountEquals(int guess) {
		return wrappedVertex.isNeighborsCountEquals(guess);
	}

	@Override
	public boolean matchCompartmentWith(Vertex otherVertex) {
		return wrappedVertex.matchCompartmentWith(otherVertex);
	}

	@Override
	public boolean matchSpeciesWith(Vertex otherVertex) {
		return wrappedVertex.matchSpeciesWith(otherVertex);
	}

	@Override
	public void collectYourDefinitionInto(Writer writer) {
		wrappedVertex.collectYourDefinitionInto(writer);
	}

	@Override
	public LineDecorator getSourceDecorator() {
		return wrappedVertex.getSourceDecorator();
	}

	@Override
	public LineDecorator getIdentifierDecorator() {
		return wrappedVertex.getIdentifierDecorator();
	}

	@Override
	public LineDecorator getSquareBracketsDecorator() {
		return wrappedVertex.getSquareBracketsDecorator();
	}

	@Override
	public void collectEdgeDefinitionInto(Writer writer, Vertex neighbour) {
		wrappedVertex.collectEdgeDefinitionInto(writer, neighbour);
	}

	@Override
	public int compareYourCompartmentIdWith(String compartment_id) {
		return wrappedVertex.compareYourCompartmentIdWith(compartment_id);
	}

	@Override
	public int compareYourSpeciesIdWith(String species_id) {
		return wrappedVertex.compareYourSpeciesIdWith(species_id);
	}

	@Override
	public void addDirectAncestors(Vertex vertex) {
		wrappedVertex.addDirectAncestors(vertex);
	}

	@Override
	public SimpleVertex asSimpleVertex() {
		return wrappedVertex.asSimpleVertex();
	}

	@Override
	public int compareTo(Vertex o) {
		return wrappedVertex.compareTo(o);
	}

	@Override
	public void publishYourStatsOn(VertexStatsRecorder vertexStatsRecorder) {
		// wrappedVertex.publishYourStatsOn(vertexStatsRecorder);
	}

	@Override
	public boolean isYourAncestorsEmpty() {
		return wrappedVertex.isYourAncestorsEmpty();
	}

	@Override
	public void brokeDirectAncestorRelationWith(Vertex source) {
		wrappedVertex.brokeDirectAncestorRelationWith(source);
	}

	@Override
	public void brokeYourNeighborhoodRelations() {
		wrappedVertex.brokeYourNeighborhoodRelations();
	}

	@Override
	public void voteOn(VertexVoteAccepter voteAccepter) {

		this.wrappedVertex.voteOn(voteAccepter);
	}

}
