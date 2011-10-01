package dotInterface;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.Assert;
import model.DfsWrapperVertex;
import model.OurModel;
import model.Vertex;
import model.VertexFactory;

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

		Vertex v = VertexFactory.makeSimpleVertex(species);
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

		Vertex v = VertexFactory.makeSimpleVertex(firstSpecies);
		Vertex v2 = VertexFactory.makeSimpleVertex(secondSpecies);
		Vertex v3 = VertexFactory.makeSimpleVertex(thirdSpecies);

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

	@Test
	public void testVerticesLabelOutsideBox() {

		Set<String> expectedVertexLabelOutsideBoxPart = new HashSet<String>();

		final Vertex v = VertexFactory.makeSimpleVertex();
		final Vertex v2 = VertexFactory.makeSimpleVertex();
		final Vertex v3 = VertexFactory.makeSimpleVertex();

		v.addNeighbour(v2);
		v.addNeighbour(v3);

		v3.addNeighbour(v2);

		OurModel simpleModel = OurModel.makeOurModelFrom(new TreeSet<Vertex>(
				Arrays.<Vertex> asList(v, v2, v3)));

		DfsEventsListenerTreeBuilder dfsEventListener = new DfsEventsListenerTreeBuilder();

		DfsExplorer dfsExplorer = DfsExplorerDefaultImplementor.make();

		dfsExplorer.acceptDfsEventsListener(dfsEventListener);

		simpleModel.runDepthFirstSearch(dfsExplorer);

		Set<Vertex> exploredVertices = new LinkedHashSet<Vertex>();
		dfsEventListener.fillCollectedVertices(exploredVertices);

		for (Vertex exploredVertex : exploredVertices) {

			DfsWrapperVertex dfsWrapper = (DfsWrapperVertex) exploredVertex;

			Writer actualWriterForVertex = new StringWriter();
			dfsWrapper.collectVertexLabelOutsideBoxInto(actualWriterForVertex);

			expectedVertexLabelOutsideBoxPart.add(actualWriterForVertex
					.toString());

		}

		Assert.assertEquals(3, expectedVertexLabelOutsideBoxPart.size());

		DotExportable exportable = OurModel.makeOurModelFrom(exploredVertices);
		DotExporter exporter = new SimpleExporter();
		exportable.acceptExporter(exporter);

		// Assert.assertTrue(exporter
		// .isVertexLabelOutsideBoxPartEquals(expectedVertexLabelOutsideBoxPart));

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
