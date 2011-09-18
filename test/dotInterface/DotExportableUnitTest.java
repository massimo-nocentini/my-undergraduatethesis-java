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

		DotExportable exportable = OurModel.makeOurModelFrom(vertices);

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

		expectedEdgeDefinitionPart
				.add(exporter.useDecorationApplier()
						.buildInfixNeighborhoodRelation(v2.provideId(),
								v3.provideId()));

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

		this.MakeTarjanNetworkVertexSetWithRelation(vertices,
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

	/**
	 * This method build the Tarjan network illustrated on page 158 of its
	 * original article.
	 * 
	 * @param vertices
	 * @param expectedVertexDefinitionPart
	 * @param expectedEdgeDefinitionPart
	 * @param exporter
	 */
	private void MakeTarjanNetworkVertexSetWithRelation(Set<Vertex> vertices,
			Set<String> expectedVertexDefinitionPart,
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
