package model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.sbml.jsbml.Species;

import tarjan.DfsExplorer;
import tarjan.VertexDfsMetadata;
import JSBMLInterface.Connector;
import dotInterface.DotExportable;
import dotInterface.DotExporter;
import dotInterface.SimpleExporter;

public class OurModel implements DotExportable {

	private Set<Vertex> vertices;

	private OurModel(Set<Vertex> vertices) {

		this.vertices = new TreeSet<Vertex>();

		for (Vertex vertex : vertices) {
			this.vertices.add(vertex);
		}
	}

	public boolean isEmpty() {
		return vertices.size() == 0;
	}

	/**
	 * Build an empty model: the encapsulated network has both the vertex set
	 * both the edge set equals to the empty set.
	 */
	public static OurModel makeEmptyModel() {
		return new OurModel(new HashSet<Vertex>());
	}

	public static OurModel makeOurModelFrom(Set<Vertex> vertices) {
		return new OurModel(vertices);
	}

	public static OurModel makeOurModelFrom(String path) {
		Connector connector = Connector.makeConnector(path);
		Set<Vertex> vertices = connector.readModel().parseModel();

		return OurModel.makeOurModelFrom(vertices);
	}

	@Override
	public void acceptExporter(final DotExporter exporter) {

		doOnVertices(new VertexLogicApplier() {

			@Override
			public void apply(Vertex vertex) {
				vertex.acceptExporter(exporter);
			}
		});
	}

	public OurModel runDepthFirstSearch(DfsExplorer dfsVertexExplorer) {

		Map<Vertex, VertexDfsMetadata> map = makeDfsVertexMetadataMap();

		dfsVertexExplorer.searchStarted(map);

		for (Entry<Vertex, VertexDfsMetadata> entry : map.entrySet()) {

			entry.getValue().ifNotExplored(dfsVertexExplorer);
		}

		dfsVertexExplorer.searchCompleted(map);

		return this;
	}

	private Map<Vertex, VertexDfsMetadata> makeDfsVertexMetadataMap() {

		final Map<Vertex, VertexDfsMetadata> map = new TreeMap<Vertex, VertexDfsMetadata>();

		doOnVertices(new VertexLogicApplier() {

			@Override
			public void apply(Vertex vertex) {
				map.put(vertex, new VertexDfsMetadata(vertex));
			}
		});

		return Collections.unmodifiableMap(map);
	}

	public void doOnVertices(VertexLogicApplier vertexLogicApplier) {
		for (Vertex vertex : vertices) {
			vertexLogicApplier.apply(vertex);
		}

	}

	/**
	 * This method build the Tarjan network illustrated on page 158 of its
	 * original article. This method is intended only for test scope because it
	 * accepts 'collecting parameters' sets, in order to specify the real
	 * vertices that the network is composed with, and other set of strings that
	 * represents the expected representations in dot language (this is the part
	 * 'test only')
	 * 
	 * @param vertices
	 *            collecting parameter to fill with network vertices.
	 * @param expectedVertexDefinitionPart
	 *            dot representation of the vertex definition compartment
	 * @param expectedEdgeDefinitionPart
	 *            dot representation of the edge (reachability relation)
	 *            definition compartment
	 * @param exporter
	 *            the dot exporter to use in order to format properly the dot
	 *            strings.
	 */
	public static void makeTarjanNetworkVertexSetWithRelation(
			Set<Vertex> vertices, Set<String> expectedVertexDefinitionPart,
			Set<String> expectedEdgeDefinitionPart, DotExporter exporter) {

		String speciesId_1 = "species_id_1";
		String speciesId_2 = "species_id_2";
		String speciesId_3 = "species_id_3";
		String speciesId_4 = "species_id_4";
		String speciesId_5 = "species_id_5";
		String speciesId_6 = "species_id_6";
		String speciesId_7 = "species_id_7";
		String speciesId_8 = "species_id_8";

		String compartmentId = "compartment_id";

		Species species_1 = new Species(speciesId_1);
		Species species_2 = new Species(speciesId_2);
		Species species_3 = new Species(speciesId_3);
		Species species_4 = new Species(speciesId_4);
		Species species_5 = new Species(speciesId_5);
		Species species_6 = new Species(speciesId_6);
		Species species_7 = new Species(speciesId_7);
		Species species_8 = new Species(speciesId_8);

		species_1.setCompartment(compartmentId);
		species_2.setCompartment(compartmentId);
		species_3.setCompartment(compartmentId);
		species_4.setCompartment(compartmentId);
		species_5.setCompartment(compartmentId);
		species_6.setCompartment(compartmentId);
		species_7.setCompartment(compartmentId);
		species_8.setCompartment(compartmentId);

		Vertex v = Vertex.makeVertex(species_1);
		Vertex v2 = Vertex.makeVertex(species_2);
		Vertex v3 = Vertex.makeVertex(species_3);
		Vertex v4 = Vertex.makeVertex(species_4);
		Vertex v5 = Vertex.makeVertex(species_5);
		Vertex v6 = Vertex.makeVertex(species_6);
		Vertex v7 = Vertex.makeVertex(species_7);
		Vertex v8 = Vertex.makeVertex(species_8);

		v.addNeighbour(v2);

		v2.addNeighbour(v3);
		v2.addNeighbour(v8);

		v3.addNeighbour(v4);
		v3.addNeighbour(v7);

		v4.addNeighbour(v5);

		v5.addNeighbour(v3);
		v5.addNeighbour(v6);

		v7.addNeighbour(v4);
		v7.addNeighbour(v6);

		v8.addNeighbour(v);
		v8.addNeighbour(v7);

		vertices.add(v);
		vertices.add(v2);
		vertices.add(v3);
		vertices.add(v4);
		vertices.add(v5);
		vertices.add(v6);
		vertices.add(v7);
		vertices.add(v8);

		expectedVertexDefinitionPart.add(v.provideId());
		expectedVertexDefinitionPart.add(v2.provideId());
		expectedVertexDefinitionPart.add(v3.provideId());
		expectedVertexDefinitionPart.add(v4.provideId());
		expectedVertexDefinitionPart.add(v5.provideId());
		expectedVertexDefinitionPart.add(exporter.useDecorationApplier()
				.decoreWithSourceSinkAttributes(v6.provideId()));
		expectedVertexDefinitionPart.add(v7.provideId());
		expectedVertexDefinitionPart.add(v8.provideId());

		// v neighbors
		expectedEdgeDefinitionPart.add(exporter.useDecorationApplier()
				.buildInfixNeighborhoodRelation(v.provideId(), v2.provideId()));

		// v2 neighbors
		expectedEdgeDefinitionPart
				.add(exporter.useDecorationApplier()
						.buildInfixNeighborhoodRelation(v2.provideId(),
								v8.provideId()));
		expectedEdgeDefinitionPart
				.add(exporter.useDecorationApplier()
						.buildInfixNeighborhoodRelation(v2.provideId(),
								v3.provideId()));

		// v3 neighbors
		expectedEdgeDefinitionPart
				.add(exporter.useDecorationApplier()
						.buildInfixNeighborhoodRelation(v3.provideId(),
								v4.provideId()));
		expectedEdgeDefinitionPart
				.add(exporter.useDecorationApplier()
						.buildInfixNeighborhoodRelation(v3.provideId(),
								v7.provideId()));

		// v4 neighbors
		expectedEdgeDefinitionPart
				.add(exporter.useDecorationApplier()
						.buildInfixNeighborhoodRelation(v4.provideId(),
								v5.provideId()));

		// v5 neighbors
		expectedEdgeDefinitionPart
				.add(exporter.useDecorationApplier()
						.buildInfixNeighborhoodRelation(v5.provideId(),
								v3.provideId()));
		expectedEdgeDefinitionPart
				.add(exporter.useDecorationApplier()
						.buildInfixNeighborhoodRelation(v5.provideId(),
								v6.provideId()));

		// v6 hasn't any neighbors

		// v7 neighbors
		expectedEdgeDefinitionPart
				.add(exporter.useDecorationApplier()
						.buildInfixNeighborhoodRelation(v7.provideId(),
								v4.provideId()));
		expectedEdgeDefinitionPart
				.add(exporter.useDecorationApplier()
						.buildInfixNeighborhoodRelation(v7.provideId(),
								v6.provideId()));

		// v8 neighbors
		expectedEdgeDefinitionPart.add(exporter.useDecorationApplier()
				.buildInfixNeighborhoodRelation(v8.provideId(), v.provideId()));
		expectedEdgeDefinitionPart
				.add(exporter.useDecorationApplier()
						.buildInfixNeighborhoodRelation(v8.provideId(),
								v7.provideId()));
	}

	/**
	 * This method build the Tarjan network illustrated on page 158 of its
	 * original article.
	 * 
	 * @return return a Tarjan test model specified in its original paper.
	 */
	public static OurModel makeTarjanModel() {
		HashSet<Vertex> vertices = new HashSet<Vertex>();
		OurModel.makeTarjanNetworkVertexSetWithRelation(vertices,
				new HashSet<String>(), new HashSet<String>(),
				new SimpleExporter());

		return makeOurModelFrom(vertices);
	}

	public static OurModel makePapadimitriouModel() {

		final Vertex vA = Vertex.makeVertex();
		final Vertex vB = Vertex.makeVertex();
		final Vertex vE = Vertex.makeVertex();
		final Vertex vI = Vertex.makeVertex();
		final Vertex vJ = Vertex.makeVertex();
		final Vertex vC = Vertex.makeVertex();
		final Vertex vD = Vertex.makeVertex();
		final Vertex vG = Vertex.makeVertex();
		final Vertex vH = Vertex.makeVertex();
		final Vertex vK = Vertex.makeVertex();
		final Vertex vL = Vertex.makeVertex();
		final Vertex vF = Vertex.makeVertex();

		vA.addNeighbour(vB);
		vA.addNeighbour(vE);

		vB.addNeighbour(vA);

		vE.addNeighbour(vA);
		vE.addNeighbour(vI);
		vE.addNeighbour(vJ);

		vI.addNeighbour(vE);
		vI.addNeighbour(vJ);

		vJ.addNeighbour(vE);
		vJ.addNeighbour(vI);

		vC.addNeighbour(vD);
		vC.addNeighbour(vH);
		vC.addNeighbour(vG);

		vD.addNeighbour(vH);
		vD.addNeighbour(vC);

		vG.addNeighbour(vH);
		vG.addNeighbour(vC);
		vG.addNeighbour(vK);

		vH.addNeighbour(vD);
		vH.addNeighbour(vC);
		vH.addNeighbour(vG);
		vH.addNeighbour(vK);
		vH.addNeighbour(vL);

		vK.addNeighbour(vH);
		vK.addNeighbour(vG);

		vL.addNeighbour(vH);

		return OurModel.makeOurModelFrom(new TreeSet<Vertex>(
				Arrays.<Vertex> asList(vA, vB, vC, vD, vE, vF, vG, vH, vI, vJ,
						vK, vL)));

	}

}
