package dotInterface;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import model.OurModel;
import model.Vertex;

import org.junit.After;
import org.junit.Test;
import org.sbml.jsbml.Species;

import JSBMLInterface.Connector;

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

		Set<String> expectedDotModel = new HashSet<String>();

		// TODO: here is present a duplication of the string
		// " [color=\"black\", style=filled]" find a place where is more
		// appropriate to put it in
		expectedDotModel.add(v.provideId().concat(
				" [color=\"black\", style=filled]"));

		Assert.assertTrue(exporter
				.isVertexDefinitionPartEquals(expectedDotModel));

		DotFileUtilHandler.MakeHandler().writeDotRepresentationInTestFolder(
				exporter, "simpleNodeWithoutNeighboursDotExporting");
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

		expectedVertexDefinitionPart.add(v.provideId().concat(
				" [color=\"black\", style=filled]"));
		expectedVertexDefinitionPart.add(v2.provideId());
		expectedVertexDefinitionPart.add(v3.provideId().concat(
				" [color=\"black\", style=filled]"));

		Set<String> expectedEdgeDefinitionPart = new HashSet<String>();

		expectedEdgeDefinitionPart.add(v.provideId().concat(" -> ")
				.concat(v2.provideId()));
		expectedEdgeDefinitionPart.add(v2.provideId().concat(" -> ")
				.concat(v3.provideId()));

		Assert.assertTrue(exporter
				.isVertexDefinitionPartEquals(expectedVertexDefinitionPart));

		Assert.assertTrue(exporter
				.isEdgeDefinitionPartEquals(expectedEdgeDefinitionPart));

		DotFileUtilHandler.MakeHandler().writeDotRepresentationInTestFolder(
				exporter, "simpleThreeNodeChainDotExporting");
	}

	@After
	public void invokeDotCompilationForAllGeneratedFiles() {
		DotFileUtilHandler.MakeHandler().evaluateDotFilesInOutputFolder();

		this.draw_allCpdsMetabSmmReactionsCompoundsSbmlModel();
	}

	public void draw_allCpdsMetabSmmReactionsCompoundsSbmlModel() {
		String path = "sbml-test-files/allCpdsMetabSmmReactionsCompounds.xml";

		Connector connector = Connector.makeConnector();

		DotExportable exportable = connector.makeOurModel(path);

		DotExporter exporter = new SimpleExporter();
		exportable.acceptExporter(exporter);

		DotFileUtilHandler.MakeHandler().writeDotRepresentationInTestFolder(
				exporter, "allCpdsMetabSmmReactionsCompounds");
	}

}
