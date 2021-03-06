package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import org.sbml.jsbml.Species;

import dotInterface.DotExporter;
import dotInterface.SimpleExporter;

public class ModelsRepository {

	public static OurModel makeCrescenziModel() {
		return ModelsRepository.makeCrescenziModel(new LinkedHashSet<Vertex>());
	}

	/**
	 * Network model described at page 274 of Crescenzi book.
	 * 
	 * @param vertices
	 * @return
	 */
	public static OurModel makeCrescenziModel(LinkedHashSet<Vertex> vertices) {
	
		String compartment_id = OurModel.getDefaultCompartmentId();
	
		final Vertex vA = VertexFactory.makeSimpleVertex("A", compartment_id);
		final Vertex vB = VertexFactory.makeSimpleVertex("B", compartment_id);
		final Vertex vC = VertexFactory.makeSimpleVertex("C", compartment_id);
		final Vertex vD = VertexFactory.makeSimpleVertex("D", compartment_id);
		final Vertex vE = VertexFactory.makeSimpleVertex("E", compartment_id);
		final Vertex vF = VertexFactory.makeSimpleVertex("F", compartment_id);
		final Vertex vG = VertexFactory.makeSimpleVertex("G", compartment_id);
		final Vertex vH = VertexFactory.makeSimpleVertex("H", compartment_id);
		final Vertex vI = VertexFactory.makeSimpleVertex("I", compartment_id);
		final Vertex vL = VertexFactory.makeSimpleVertex("L", compartment_id);
		final Vertex vM = VertexFactory.makeSimpleVertex("M", compartment_id);
		final Vertex vN = VertexFactory.makeSimpleVertex("N", compartment_id);
		final Vertex vO = VertexFactory.makeSimpleVertex("O", compartment_id);
		final Vertex vP = VertexFactory.makeSimpleVertex("P", compartment_id);
		final Vertex vQ = VertexFactory.makeSimpleVertex("Q", compartment_id);
		final Vertex vR = VertexFactory.makeSimpleVertex("R", compartment_id);
		final Vertex vS = VertexFactory.makeSimpleVertex("S", compartment_id);
		final Vertex vT = VertexFactory.makeSimpleVertex("T", compartment_id);
	
		vA.addNeighbour(vB);
		vA.addNeighbour(vD);
	
		vB.addNeighbour(vA);
		vB.addNeighbour(vC);
	
		vC.addNeighbour(vB);
		vC.addNeighbour(vG);
	
		vD.addNeighbour(vF);
		vD.addNeighbour(vE);
	
		vE.addNeighbour(vG);
	
		vF.addNeighbour(vE);
		vF.addNeighbour(vG);
	
		vG.addNeighbour(vD);
	
		vH.addNeighbour(vI);
		vH.addNeighbour(vM);
		vH.addNeighbour(vN);
	
		vI.addNeighbour(vL);
	
		vL.addNeighbour(vH);
		vL.addNeighbour(vN);
	
		vM.addNeighbour(vL);
	
		vN.addNeighbour(vF);
	
		vO.addNeighbour(vP);
		vO.addNeighbour(vQ);
	
		vP.addNeighbour(vR);
	
		vQ.addNeighbour(vP);
	
		vR.addNeighbour(vO);
		vR.addNeighbour(vN);
		vR.addNeighbour(vS);
	
		vS.addNeighbour(vT);
	
		vT.addNeighbour(vS);
	
		vertices.addAll(Arrays.<Vertex> asList(vA, vB, vC, vD, vE, vF, vG, vH,
				vI, vL, vM, vN, vO, vP, vQ, vR, vS, vT));
	
		return OurModel.makeOurModelFrom(vertices);
	
	}

	/**
	 * This method build the Tarjan network illustrated on page 158 of its
	 * original article.
	 * 
	 * @return return a Tarjan test model specified in its original paper.
	 */
	public static OurModel makeTarjanModel() {
		return ModelsRepository.makeTarjanModel(new TreeSet<Vertex>());
	}

	public static OurModel makeTarjanModel(Set<Vertex> vertices) {
	
		ModelsRepository.makeTarjanNetworkVertexSetWithRelation(vertices,
				new HashSet<String>(), new HashSet<String>(),
				new SimpleExporter());
	
		return OurModel.makeOurModelFrom(vertices);
	}

	public static OurModel makePapadimitriouModel() {
		return ModelsRepository.makePapadimitriouModel(new LinkedHashSet<Vertex>());
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
	
		ModelsRepository.makeTarjanNetworkVertexSetWithRelation(vertices,
				expectedVertexDefinitionPart, expectedEdgeDefinitionPart,
				new HashSet<String>(), exporter);
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
	
		String compartment_id = OurModel.getDefaultCompartmentId();
	
		final Vertex vA = VertexFactory.makeSimpleVertex("A", compartment_id);
		final Vertex vB = VertexFactory.makeSimpleVertex("B", compartment_id);
		final Vertex vE = VertexFactory.makeSimpleVertex("E", compartment_id);
		final Vertex vI = VertexFactory.makeSimpleVertex("I", compartment_id);
		final Vertex vJ = VertexFactory.makeSimpleVertex("J", compartment_id);
		final Vertex vC = VertexFactory.makeSimpleVertex("C", compartment_id);
		final Vertex vD = VertexFactory.makeSimpleVertex("D", compartment_id);
		final Vertex vG = VertexFactory.makeSimpleVertex("G", compartment_id);
		final Vertex vH = VertexFactory.makeSimpleVertex("H", compartment_id);
		final Vertex vK = VertexFactory.makeSimpleVertex("K", compartment_id);
		final Vertex vL = VertexFactory.makeSimpleVertex("L", compartment_id);
		final Vertex vF = VertexFactory.makeSimpleVertex("F", compartment_id);
	
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
	
		String compartmentId = OurModel.getDefaultCompartmentId();
	
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
	
		Vertex v = VertexFactory.makeSimpleVertex(species_1);
		Vertex v2 = VertexFactory.makeSimpleVertex(species_2);
		Vertex v3 = VertexFactory.makeSimpleVertex(species_3);
		Vertex v4 = VertexFactory.makeSimpleVertex(species_4);
		Vertex v5 = VertexFactory.makeSimpleVertex(species_5);
		Vertex v6 = VertexFactory.makeSimpleVertex(species_6);
		Vertex v7 = VertexFactory.makeSimpleVertex(species_7);
		Vertex v8 = VertexFactory.makeSimpleVertex(species_8);
	
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
	
		OurModel.collectVertexDefinitionInto(expectedVertexDefinitionPart, v);
	
		OurModel.collectVertexDefinitionInto(expectedVertexDefinitionPart, v2);
	
		OurModel.collectVertexDefinitionInto(expectedVertexDefinitionPart, v3);
	
		OurModel.collectVertexDefinitionInto(expectedVertexDefinitionPart, v4);
	
		OurModel.collectVertexDefinitionInto(expectedVertexDefinitionPart, v5);
	
		OurModel.collectVertexDefinitionInto(expectedVertexDefinitionPart, v6);
	
		OurModel.collectVertexDefinitionInto(expectedVertexDefinitionPart, v7);
	
		OurModel.collectVertexDefinitionInto(expectedVertexDefinitionPart, v8);
	
		// v neighbors
		OurModel.collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v, v2);
	
		// v2 neighbors
		OurModel.collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v2, v8);
	
		OurModel.collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v2, v3);
	
		// v3 neighbors
		OurModel.collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v3, v4);
	
		OurModel.collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v3, v7);
	
		// v4 neighbors
		OurModel.collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v4, v5);
	
		// v5 neighbors
		OurModel.collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v5, v3);
	
		OurModel.collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v5, v6);
	
		// v6 hasn't any neighbors
	
		// v7 neighbors
		OurModel.collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v7, v4);
	
		OurModel.collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v7, v6);
	
		// v8 neighbors
		OurModel.collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v8, v);
	
		OurModel.collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v8, v7);
	
	}

}
