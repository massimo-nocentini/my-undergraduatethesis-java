package model;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;
import java.util.TreeSet;

import org.sbml.jsbml.Species;

import dotInterface.DotExporter;
import dotInterface.DotFileUtilHandler;
import dotInterface.LineDecorator;
import dotInterface.NullObjectLineDecorator;

public class SimpleVertex implements Vertex {

	/**
	 * Dummy value
	 */
	static String DummySpeciesId = "dummy_species_id";
	static String DummyCompartmentId = "dummy_compartment_id";

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

	private final Set<Vertex> neighbors;

	private final String species_id;

	private final String species_name;

	private final String compartment_id;

	private final Set<Vertex> directAncestors;

	private SimpleVertex(String species_id, String compartment_id) {
		this(species_id, "", compartment_id);
	}

	private SimpleVertex(String species_id, String species_name,
			String compartment_id) {

		this.species_id = species_id;
		this.species_name = species_name;
		this.compartment_id = compartment_id;

		neighbors = new TreeSet<Vertex>();
		directAncestors = new TreeSet<Vertex>();
	}

	@Override
	public Vertex addNeighbour(Vertex neighbour) {
		this.neighbors.add(neighbour);
		neighbour.addDirectAncestors(this);
		return this;
	}

	@Override
	public void doOnNeighbors(VertexLogicApplier applier) {
		for (Vertex vertex : neighbors) {
			applier.apply(vertex);
		}

	}

	@Override
	public void doOnNeighbors(VertexLogicApplierWithNeighborhoodRelation applier) {
		for (Vertex neighbour : neighbors) {
			applier.apply(this, neighbour);
		}

	}

	@Override
	public boolean isYourNeighborhoodEquals(Set<Vertex> products) {
		return this.neighbors.equals(products);
	}

	@Override
	public boolean isYourNeighborhoodEmpty() {
		return this.neighbors.size() == 0;
	}

	@Override
	public boolean isYourSpeciesId(String speciesId) {
		return this.species_id.equals(speciesId);
	}

	@Override
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

	@Override
	public boolean haveYouSelfLoop() {
		return this.neighbors.contains(this);
	}

	@Override
	public boolean isYourOrigin(Species aSpecies) {
		return this.isYourSpeciesId(aSpecies.getId())
				&& this.isYourCompartmentId(aSpecies.getCompartment());
	}

	@Override
	public void acceptExporter(DotExporter exporter) {
		exporter.buildVertexDefinition(this);

		for (Vertex neighbour : neighbors) {
			exporter.buildEdgeDefinition(this, neighbour);
		}
	}

	@Override
	public boolean isSink() {
		return neighbors.size() == 0;// && directAncestors.size() > 0;
	}

	@Override
	public boolean isSource() {
		return directAncestors.size() == 0 && neighbors.size() > 0;
	}

	@Override
	public boolean isYourNeighbour(Vertex a) {
		return neighbors.contains(a);
	}

	@Override
	public boolean isNeighborsCountEquals(int guess) {
		return neighbors.size() == guess;
	}

	@Override
	public boolean matchCompartmentWith(Vertex otherVertex) {
		return otherVertex.isYourCompartmentId(compartment_id);
	}

	@Override
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

	static SimpleVertex makeVertex(String species_id, String compartment_id) {
		return makeVertex(species_id, "", compartment_id);
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
		// Inoltre questa correttezza potrebbe essere catturata da un test
		// che assicura che la ricorsione termina (quindi e' corretto anche in
		// questo
		// caso aggiungere ai neighbour di vertex l'istanza this, pero va
		// controllato
		// se nell'insieme non e' presente l'elemento e solo in quel caso e'
		// corretto
		// invocare ricorsivamente.
		this.directAncestors.add(vertex);
	}

	@Override
	public void collectYourDefinitionInto(Writer writer) {

		String result = "";

		result = getSourceDecorator().decore(result);

		result = getSquareBracketsDecorator().decore(result);

		result = DotFileUtilHandler.getBlankString().concat(result);

		result = getIdentifierDecorator().decore(result);

		try {
			writer.append(result);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String composeIdentifier(String species_id,
			String compartment_id) {

		return species_id.trim().concat(compartment_id.trim());
	}

	public static String getSourceSinkToken() {
		return "color=\"black\", style=filled";
	}

	@Override
	public LineDecorator getSourceDecorator() {

		LineDecorator lineDecorator = new NullObjectLineDecorator();

		if (isSink() || isSource()) {
			lineDecorator = new SourceSinkLineDecorator();
		}

		return lineDecorator;
	}

	public class SourceSinkLineDecorator implements LineDecorator {

		@Override
		public String decore(String line) {
			return line.concat(getSourceSinkToken());
		}

	}

	public class IdentifierLineDecorator implements LineDecorator {

		@Override
		public String decore(String line) {
			return composeIdentifier(species_id, compartment_id).concat(line);
		}

	}

	public class SquareBracketsLineDecorator implements LineDecorator {

		@Override
		public String decore(String line) {
			if (line == null || "".equals(line)) {
				return line;
			}
			return DotFileUtilHandler.composeSquareBracketsWrapping(line);
		}
	}

	@Override
	public LineDecorator getIdentifierDecorator() {
		return new IdentifierLineDecorator();
	}

	@Override
	public LineDecorator getSquareBracketsDecorator() {
		return new SquareBracketsLineDecorator();
	}

	@Override
	public void collectEdgeDefinitionInto(Writer writer, Vertex neighbour) {

		String myIdentifier = getIdentifierDecorator().decore("");

		String neighbourIdentifier = neighbour.getIdentifierDecorator().decore(
				"");

		try {
			writer.append(SimpleVertex.composeNeighbourRelationInfixRelation(
					myIdentifier, neighbourIdentifier));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String composeNeighbourRelationInfixRelation(
			String sourceIdentifier, String neighbourIdentifier) {

		return sourceIdentifier.concat(
				DotFileUtilHandler.getNeighbourRelationInfixToken()).concat(
				neighbourIdentifier);
	}

	@Override
	public void collectVertexLabelOutsideBoxInto(Writer writer) {
		// this implementation doesn't have any additional information
		// to draw near the vertex box.
	}

	@Override
	public void publishYourStatsOn(VertexStatsRecorder vertexStatsRecorder) {

		VertexVoteAccepter voteAccepter = vertexStatsRecorder
				.recordSimpleVertex();

		this.voteOn(voteAccepter);
	}

	@Override
	public void brokeDirectAncestorRelationWith(Vertex source) {

		if (directAncestors.contains(source)) {
			directAncestors.remove(source);
		}
	}

	@Override
	public void brokeYourNeighborhoodRelations() {

		final VertexLogicApplierWithNeighborhoodRelation directAncestorBroker = new VertexLogicApplierWithNeighborhoodRelation() {

			@Override
			public void apply(Vertex source, Vertex neighbour) {

				neighbour.brokeDirectAncestorRelationWith(source);
			}
		};

		doOnNeighbors(directAncestorBroker);

		// we have to send the following message here and not in the
		// anonymous implementation of the applier otherwise we get a
		// concurrent exception because we are trying to modify the source
		// object
		neighbors.clear();
	}

	@Override
	public boolean isYourAncestorsEmpty() {
		return directAncestors.size() == 0;
	}

	@Override
	public void voteOn(VertexVoteAccepter voteAccepter) {

		voteAccepter.pushEdges(neighbors.size());

		if (isSink()) {
			voteAccepter.pushSink();
		} else if (isSource()) {
			voteAccepter.pushSource();
		} else {
			voteAccepter.pushWhite();
		}

	}

	@Override
	public void doWithVertexType(DoAction<VertexType> doer) {

		VertexType type = VertexType.Whites;

		if (this.isSink()) {
			type = VertexType.Sinks;
		} else if (this.isSource()) {
			type = VertexType.Sources;
		}

		doer.apply(type, this.species_id, this.species_name,
				this.compartment_id);
	}

	@Override
	public boolean isYourSpeciesName(String species_name) {

		if (this.species_name == null) {
			return false;
		}

		return this.species_name.equals(species_name);
	}

	@Override
	public String buildVertexUniqueIdentifier() {
		String identifier = this.species_id;

		identifier = identifier + "-("
				+ (this.species_name != null ? this.species_name : "") + ")";

		identifier = identifier + "-(" + this.compartment_id + ")";

		return identifier.toUpperCase();
	}

	static SimpleVertex makeVertex(String species_id, String species_name,
			String compartment_id) {

		return new SimpleVertex(species_id, species_name, compartment_id);
	}
}
