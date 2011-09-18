package dotInterface;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import model.OurModel;
import model.Vertex;

import org.junit.Test;
import org.sbml.jsbml.Species;

public class DotExportableUnitTest {

	@Test
	public void simpleNodeWithoutNeighboursDotExporting() {

		String speciesId = "species_id";
		String compartmentId = "compartment_id";

		Species species = new Species(speciesId);
		species.setCompartment(compartmentId);

		Vertex v = Vertex.makeVertex(species);
		DotExportable exportable = v;

		DotExporter exporter = new SimpleExporter();
		exportable.acceptExporter(exporter);

		Set<String> expectedVertexDefinitionPart = new HashSet<String>();

		expectedVertexDefinitionPart.add(exporter.useDecorationApplier()
				.decoreWithSourceSinkAttributes(v.provideId()));

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

		Vertex v = Vertex.makeVertex(firstSpecies);
		Vertex v2 = Vertex.makeVertex(secondSpecies);
		Vertex v3 = Vertex.makeVertex(thirdSpecies);

		v.addNeighbour(v2);
		v2.addNeighbour(v3);

		Set<Vertex> vertices = new HashSet<Vertex>();
		vertices.add(v);
		vertices.add(v2);
		vertices.add(v3);

		DotExportable exportable = OurModel.makeModel(vertices);

		DotExporter exporter = new SimpleExporter();
		exportable.acceptExporter(exporter);

		Set<String> expectedVertexDefinitionPart = new HashSet<String>();

		expectedVertexDefinitionPart.add(exporter.useDecorationApplier()
				.decoreWithSourceSinkAttributes(v.provideId()));

		expectedVertexDefinitionPart.add(v2.provideId());

		expectedVertexDefinitionPart.add(exporter.useDecorationApplier()
				.decoreWithSourceSinkAttributes(v3.provideId()));

		Set<String> expectedEdgeDefinitionPart = new HashSet<String>();

		expectedEdgeDefinitionPart.add(exporter.useDecorationApplier()
				.buildInfixNeighborhoodRelation(v.provideId(), v2.provideId()));

		expectedEdgeDefinitionPart.add(exporter.useDecorationApplier()
				.buildInfixNeighborhoodRelation(v2.provideId(), v3.provideId()));

		Assert.assertTrue(exporter
				.isVertexDefinitionPartEquals(expectedVertexDefinitionPart));

		Assert.assertTrue(exporter
				.isEdgeDefinitionPartEquals(expectedEdgeDefinitionPart));

		DotFileUtilHandler.MakeHandler("simpleThreeNodeChainDotExporting")
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

}
