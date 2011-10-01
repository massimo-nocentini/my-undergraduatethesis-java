package dotInterface;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import junit.framework.Assert;
import model.OurModel;
import model.SimpleVertex;
import model.Vertex;

import org.junit.Test;
import org.sbml.jsbml.Species;

import tarjan.DfsEventsListenerTreeBuilder;
import tarjan.DfsExplorer;
import tarjan.DfsExplorerDefaultImplementor;

public class DotExportableUnitTest {

	@Test
	public void simpleNodeWithoutNeighboursDotExporting() {

		String speciesId = "species_id";
		String compartmentId = "compartment_id";

		Species species = new Species(speciesId);
		species.setCompartment(compartmentId);

		Vertex v = SimpleVertex.makeVertex(species);
		DotExportable exportable = v;

		DotExporter exporter = new SimpleExporter();
		exportable.acceptExporter(exporter);

		Set<String> expectedVertexDefinitionPart = new HashSet<String>();

		OurModel.collectVertexDefinitionInto(expectedVertexDefinitionPart, v);

		Assert.assertTrue(exporter
				.isVertexDefinitionPartEquals(expectedVertexDefinitionPart));

		Assert.assertTrue(exporter
				.isEdgeDefinitionPartEquals(new HashSet<String>()));

		DotFileUtilHandler
				.MakeHandler("simpleNodeWithoutNeighboursDotExporting")
				.writeDotRepresentationInTestFolder(exporter)
				.produceSvgOutput();
	}

	@Test
	public void simpleThreeNodeChainDotExporting() {

		String firstSpeciesId = "first_species_id";
		String secondSpeciesId = "second_species_id";
		String thirdSpeciesId = "third_species_id";

		String compartmentId = "compartment_id";

		Species firstSpecies = new Species(firstSpeciesId);
		Species secondSpecies = new Species(secondSpeciesId);
		Species thirdSpecies = new Species(thirdSpeciesId);

		firstSpecies.setCompartment(compartmentId);
		secondSpecies.setCompartment(compartmentId);
		thirdSpecies.setCompartment(compartmentId);

		Vertex v = SimpleVertex.makeVertex(firstSpecies);
		Vertex v2 = SimpleVertex.makeVertex(secondSpecies);
		Vertex v3 = SimpleVertex.makeVertex(thirdSpecies);

		v.addNeighbour(v2);
		v2.addNeighbour(v3);

		Set<Vertex> vertices = new HashSet<Vertex>();
		vertices.add(v);
		vertices.add(v2);
		vertices.add(v3);

		DotExportable exportable = OurModel.makeOurModelFrom(vertices);

		DotExporter exporter = new SimpleExporter();

		exportable.acceptExporter(exporter);

		Set<String> expectedVertexDefinitionPart = new HashSet<String>();
		Set<String> expectedEdgeDefinitionPart = new HashSet<String>();

		OurModel.collectVertexDefinitionInto(expectedVertexDefinitionPart, v);

		OurModel.collectVertexDefinitionInto(expectedVertexDefinitionPart, v2);

		OurModel.collectVertexDefinitionInto(expectedVertexDefinitionPart, v3);

		OurModel.collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v, v2);

		OurModel.collectEdgeDefinitionInto(expectedEdgeDefinitionPart, v2, v3);

		Assert.assertTrue(exporter
				.isVertexDefinitionPartEquals(expectedVertexDefinitionPart));

		Assert.assertTrue(exporter
				.isEdgeDefinitionPartEquals(expectedEdgeDefinitionPart));

		DotFileUtilHandler.MakeHandler("simpleThreeNodeChainDotExporting")
				.writeDotRepresentationInTestFolder(exporter)
				.produceSvgOutput();
	}

	@Test
	public void tarjanPaperTestNetwork() {

		Set<Vertex> vertices = new HashSet<Vertex>();
		Set<String> expectedVertexDefinitionPart = new HashSet<String>();
		Set<String> expectedEdgeDefinitionPart = new HashSet<String>();
		DotExporter exporter = new SimpleExporter();

		OurModel.makeTarjanNetworkVertexSetWithRelation(vertices,
				expectedVertexDefinitionPart, expectedEdgeDefinitionPart,
				exporter);

		DotExportable exportable = OurModel.makeOurModelFrom(vertices);

		exportable.acceptExporter(exporter);

		Assert.assertEquals(12, expectedEdgeDefinitionPart.size());
		Assert.assertEquals(8, expectedVertexDefinitionPart.size());

		Assert.assertTrue(exporter
				.isVertexDefinitionPartEquals(expectedVertexDefinitionPart));

		Assert.assertTrue(exporter
				.isEdgeDefinitionPartEquals(expectedEdgeDefinitionPart));

		DotFileUtilHandler.MakeHandler("tarjanPaperTestNetwork")
				.writeDotRepresentationInTestFolder(exporter)
				.produceSvgOutput();
	}

	// @Test
	public void testVerticesLabelOutsideBox() {

		// Set<Vertex> vertices = new HashSet<Vertex>();
		//
		// Set<String> expectedVertexLabelOutsideBoxPart = new
		// HashSet<String>();
		// DotExporter exporter = new SimpleExporter();
		//
		// OurModel.makeTarjanNetworkVertexSetWithRelation(vertices,
		// new HashSet<String>(), new HashSet<String>(),
		// expectedVertexLabelOutsideBoxPart, exporter);
		//
		// DfsEventsListenerTreeBuilder dfsEventListener = new
		// DfsEventsListenerTreeBuilder();
		//
		// DfsExplorer dfsExplorer = DfsExplorerDefaultImplementor.make();
		//
		// dfsExplorer.acceptDfsEventsListener(dfsEventListener);
		//
		// DotExportable exportable = OurModel.makeOurModelFrom(vertices)
		// .runDepthFirstSearch(dfsExplorer);
		//
		// exportable.acceptExporter(exporter);
		//
		// Assert.assertEquals(12, expectedVertexLabelOutsideBoxPart.size());
		//
		// Assert.assertTrue(exporter
		// .isVertexLabelOutsideBoxPartEquals(expectedVertexLabelOutsideBoxPart));
		//
		// DotFileUtilHandler.MakeHandler("testVerticesLabelOutsideBox")
		// .writeDotRepresentationInTestFolder(exporter)
		// .produceSvgOutput();

		// bookmark fail.
		Assert.fail();

		DfsEventsListenerTreeBuilder dfsEventListener = new DfsEventsListenerTreeBuilder();

		DfsExplorer dfsExplorer = DfsExplorerDefaultImplementor.make();

		dfsExplorer.acceptDfsEventsListener(dfsEventListener);

		OurModel papadimitriouModel = OurModel.makePapadimitriouModel();
		papadimitriouModel.runDepthFirstSearch(dfsExplorer);

		Set<Vertex> vertices = new LinkedHashSet<Vertex>();
		dfsEventListener.fillCollectedVertices(vertices);

		String compartment_id = OurModel.getDefaultCompartmentId();

		// papadimitriouModel.findVertexByExampleAndApplyLogicOnIt(
		// SimpleVertex.makeVertex("A", compartment_id),
		// new VertexLogicApplier() {
		//
		// @Override
		// public void apply(Vertex vertex) {
		// Writer writer = new StringWriter();
		//
		// vertex.useFormatter().formatVertexLabelOutsideBoxInto(
		// writer, vertex, useDecorationApplier);
		// }
		// });

	}

	// @Test
	public void evaluateExampleSbmlModelDirectory() {
		// DotFileUtilHandler.MakeHandlerForExampleSbmlModel(
		// "allCpdsMetabSmmReactionsCompounds").produceSvgOutput();
		//
		// DotFileUtilHandler.MakeHandlerForExampleSbmlModel(
		// "BartonellaQuintanaToulouse").produceSvgOutput();
		//
		// DotFileUtilHandler.MakeHandlerForExampleSbmlModel(
		// "CandidatusSulciamuelleriSMDSEM").produceSvgOutput();
		//
		// DotFileUtilHandler.MakeHandlerForExampleSbmlModel("EscherichiaColiK12")
		// .produceSvgOutput();
		//
		// DotFileUtilHandler.MakeHandlerForExampleSbmlModel(
		// "MesorhizobiumLotiMAFF303099").produceSvgOutput();
		//
		// DotFileUtilHandler.MakeHandlerForExampleSbmlModel(
		// "PseudomonasAeruginosaPAO1").produceSvgOutput();
		//
		// DotFileUtilHandler.MakeHandlerForExampleSbmlModel(
		// "RhodobacterSphaeroides241").produceSvgOutput();
		//
		DotFileUtilHandler.MakeHandlerForExampleSbmlModel(
				"RickettsiaMassiliaeMTU5").produceSvgOutput();
	}

	@Test
	public void checkGetAbsoluteFileNameInTestOutputFolder() {
		String filename = "printerPipeFilterOutput";

		File expected = new File(DotFileUtilHandler.dotOutputFolderPathName()
				.concat(filename).concat(".dot"));

		Assert.assertEquals(expected,
				DotFileUtilHandler.makeDotOutputFile(filename));
	}

}
