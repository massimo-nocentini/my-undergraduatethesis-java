package dotInterface;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashSet;
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

		Writer identifierWriter = new StringWriter();
		v.useFormatter().formatVertexDefinitionInto(identifierWriter, v,
				exporter.useDecorationApplier());
		try {
			identifierWriter.close();
			expectedVertexDefinitionPart.add(identifierWriter.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// expectedVertexDefinitionPart.add(exporter.useDecorationApplier()
		// .decoreWithSourceSinkAttributes(v.provideId()));

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
		DotDecorationApplier dotDecorationApplier = exporter
				.useDecorationApplier();

		exportable.acceptExporter(exporter);

		Set<String> expectedVertexDefinitionPart = new HashSet<String>();

		Writer identifierWriter = null;

		identifierWriter = new StringWriter();
		v.useFormatter().formatVertexDefinitionInto(identifierWriter, v,
				dotDecorationApplier);
		try {
			identifierWriter.close();
			expectedVertexDefinitionPart.add(identifierWriter.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// expectedVertexDefinitionPart.add(exporter.useDecorationApplier()
		// .decoreWithSourceSinkAttributes(v.provideId()));

		identifierWriter = new StringWriter();
		v2.useFormatter().formatVertexDefinitionInto(identifierWriter, v2,
				dotDecorationApplier);
		try {
			identifierWriter.close();
			expectedVertexDefinitionPart.add(identifierWriter.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		// expectedVertexDefinitionPart.add(v2.provideId());

		identifierWriter = new StringWriter();
		v3.useFormatter().formatVertexDefinitionInto(identifierWriter, v3,
				dotDecorationApplier);
		try {
			identifierWriter.close();
			expectedVertexDefinitionPart.add(identifierWriter.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		// expectedVertexDefinitionPart.add(exporter.useDecorationApplier()
		// .decoreWithSourceSinkAttributes(v3.provideId()));

		Set<String> expectedEdgeDefinitionPart = new HashSet<String>();

		identifierWriter = new StringWriter();
		v.useFormatter().formatEdgeDefinitionInto(identifierWriter, v, v2,
				dotDecorationApplier);
		try {
			identifierWriter.close();
			expectedEdgeDefinitionPart.add(identifierWriter.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		// expectedEdgeDefinitionPart.add(exporter.useDecorationApplier()
		// .buildInfixNeighborhoodRelation(v.provideId(), v2.provideId()));

		identifierWriter = new StringWriter();
		v2.useFormatter().formatEdgeDefinitionInto(identifierWriter, v2, v3,
				dotDecorationApplier);
		try {
			identifierWriter.close();
			expectedEdgeDefinitionPart.add(identifierWriter.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		// expectedEdgeDefinitionPart
		// .add(exporter.useDecorationApplier()
		// .buildInfixNeighborhoodRelation(v2.provideId(),
		// v3.provideId()));

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

	@Test
	public void testVerticesLabelOutsideBox() {

		Set<Vertex> vertices = new HashSet<Vertex>();

		Set<String> expectedVertexLabelOutsideBoxPart = new HashSet<String>();
		DotExporter exporter = new SimpleExporter();

		OurModel.makeTarjanNetworkVertexSetWithRelation(vertices,
				new HashSet<String>(), new HashSet<String>(),
				expectedVertexLabelOutsideBoxPart, exporter);

		DfsEventsListenerTreeBuilder dfsEventListener = new DfsEventsListenerTreeBuilder();

		DfsExplorer dfsExplorer = DfsExplorerDefaultImplementor.make();

		dfsExplorer.acceptDfsEventsListener(dfsEventListener);

		DotExportable exportable = OurModel.makeOurModelFrom(vertices)
				.runDepthFirstSearch(dfsExplorer);

		exportable.acceptExporter(exporter);

		Assert.assertEquals(12, expectedVertexLabelOutsideBoxPart.size());

		Assert.assertTrue(exporter
				.isVertexLabelOutsideBoxPartEquals(expectedVertexLabelOutsideBoxPart));

		DotFileUtilHandler.MakeHandler("testVerticesLabelOutsideBox")
				.writeDotRepresentationInTestFolder(exporter)
				.produceSvgOutput();
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
