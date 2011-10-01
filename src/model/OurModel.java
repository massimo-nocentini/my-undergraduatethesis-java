package model;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
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

	public OurModel runDepthFirstSearch(final DfsExplorer dfsVertexExplorer,
			final Vertex startingVertex) {

		final Map<Vertex, VertexDfsMetadata> map = makeDfsVertexMetadataMap();

		dfsVertexExplorer.searchStarted(map);

		findVertexByExampleAndApplyLogicOnIt(startingVertex,
				new VertexLogicApplier() {

					@Override
					public void apply(Vertex vertex) {
						map.get(vertex).ifNotExplored(dfsVertexExplorer);
					}
				});

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

		makeTarjanNetworkVertexSetWithRelation(vertices,
				expectedVertexDefinitionPart, expectedEdgeDefinitionPart,
				new HashSet<String>(), exporter);
	}

	/**
	 * This method build the Tarjan network illustrated on page 158 of its
	 * original article.
	 * 
	 * @return return a Tarjan test model specified in its original paper.
	 */
	public static OurModel makeTarjanModel() {
		return makeTarjanModel(new TreeSet<Vertex>());
	}

	public static OurModel makeTarjanModel(Set<Vertex> vertices) {

		OurModel.makeTarjanNetworkVertexSetWithRelation(vertices,
				new HashSet<String>(), new HashSet<String>(),
				new SimpleExporter());

		return makeOurModelFrom(vertices);
	}

	public static OurModel makePapadimitriouModel() {
		return makePapadimitriouModel(new LinkedHashSet<Vertex>());
	}

	/**
	 * This method makes the Papadimitriou model (described on page 86 of his
	 * book Algorithms). The unique parameter that this method accepts is an
	 * object of a specialized type in the set's hierarchy: this catch the
	 * concept that the addition of vertices to the set preserve the insertion
	 * ordering (this is the only way to require such data structure in order to
	 * maintain the order). This insertion order is the visit order that must be
	 * match by a DFS search run.
	 * 
	 * @param vertices
	 *            collecting parameter. The vertices added to this set preserve
	 *            the addition order
	 * @return a Papadimitriou model described at page 86 of his book
	 */
	public static OurModel makePapadimitriouModel(LinkedHashSet<Vertex> vertices) {

		String compartment_id = getDefaultCompartmentId();

		final Vertex vA = SimpleVertex.makeVertex("A", compartment_id);
		final Vertex vB = SimpleVertex.makeVertex("B", compartment_id);
		final Vertex vE = SimpleVertex.makeVertex("E", compartment_id);
		final Vertex vI = SimpleVertex.makeVertex("I", compartment_id);
		final Vertex vJ = SimpleVertex.makeVertex("J", compartment_id);
		final Vertex vC = SimpleVertex.makeVertex("C", compartment_id);
		final Vertex vD = SimpleVertex.makeVertex("D", compartment_id);
		final Vertex vG = SimpleVertex.makeVertex("G", compartment_id);
		final Vertex vH = SimpleVertex.makeVertex("H", compartment_id);
		final Vertex vK = SimpleVertex.makeVertex("K", compartment_id);
		final Vertex vL = SimpleVertex.makeVertex("L", compartment_id);
		final Vertex vF = SimpleVertex.makeVertex("F", compartment_id);

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

		// this addition sequence order is the order that the DFS
		// must replay during the visit of the graph
		vertices.addAll(Arrays.<Vertex> asList(vA, vB, vE, vI, vJ, vC, vD, vH,
				vG, vK, vL, vF));

		return OurModel.makeOurModelFrom(vertices);

	}

	public static String getDefaultCompartmentId() {
		String compartment_id = "compartment_id";
		return compartment_id;
	}

	public OurModel findVertexByExampleAndApplyLogicOnIt(
			final Vertex exampleVertex, final VertexLogicApplier logicApplier) {

		if (vertices.contains(exampleVertex)) {
			doOnVertices(new VertexLogicApplier() {

				@Override
				public void apply(Vertex vertex) {

					if (vertex.equals(exampleVertex)) {
						logicApplier.apply(vertex);
					}
				}
			});
		}

		return this;
	}

	public static void makeTarjanNetworkVertexSetWithRelation(
			Set<Vertex> vertices, Set<String> expectedVertexDefinitionPart,
			Set<String> expectedEdgeDefinitionPart,
			Set<String> expectedVertexLabelOutsideBoxPart, DotExporter exporter) {

		String speciesId_1 = "species_id_1";
		String speciesId_2 = "species_id_2";
		String speciesId_3 = "species_id_3";
		String speciesId_4 = "species_id_4";
		String speciesId_5 = "species_id_5";
		String speciesId_6 = "species_id_6";
		String speciesId_7 = "species_id_7";
		String speciesId_8 = "species_id_8";

		String compartmentId = getDefaultCompartmentId();

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

		Vertex v = SimpleVertex.makeVertex(species_1);
		Vertex v2 = SimpleVertex.makeVertex(species_2);
		Vertex v3 = SimpleVertex.makeVertex(species_3);
		Vertex v4 = SimpleVertex.makeVertex(species_4);
		Vertex v5 = SimpleVertex.makeVertex(species_5);
		Vertex v6 = SimpleVertex.makeVertex(species_6);
		Vertex v7 = SimpleVertex.makeVertex(species_7);
		Vertex v8 = SimpleVertex.makeVertex(species_8);

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

		collectVertexDefinitionInto(expectedVertexDefinitionPart, v);

		collectVertexDefinitionInto(expectedVertexDefinitionPart, v2);

		collectVertexDefinitionInto(expectedVertexDefinitionPart, v3);

		collectVertexDefinitionInto(expectedVertexDefinitionPart, v4);

		collectVertexDefinitionInto(expectedVertexDefinitionPart, v5);

		collectVertexDefinitionInto(expectedVertexDefinitionPart, v6);

		collectVertexDefinitionInto(expectedVertexDefinitionPart, v7);

		collectVertexDefinitionInto(expectedVertexDefinitionPart, v8);

		// v neighbors
		collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v, v2);

		// v2 neighbors
		collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v2, v8);

		collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v2, v3);

		// v3 neighbors
		collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v3, v4);

		collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v3, v7);

		// v4 neighbors
		collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v4, v5);

		// v5 neighbors
		collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v5, v3);

		collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v5, v6);

		// v6 hasn't any neighbors

		// v7 neighbors
		collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v7, v4);

		collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v7, v6);

		// v8 neighbors
		collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v8, v);

		collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v8, v7);

	}

	public static void collectEdgeDefinitionInto(
			Set<String> expectedEdgeDefinitionPart, Vertex source,
			Vertex neighbour) {

		StringWriter identifierWriter = new StringWriter();

		source.collectEdgeDefinitionInto(identifierWriter, neighbour);

		try {
			identifierWriter.close();
			expectedEdgeDefinitionPart.add(identifierWriter.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void collectVertexDefinitionInto(
			Set<String> expectedVertexDefinitionPart, Vertex vertex) {

		Writer identifierWriter = new StringWriter();

		vertex.collectYourDefinitionInto(identifierWriter);

		try {
			identifierWriter.close();
			expectedVertexDefinitionPart.add(identifierWriter.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
