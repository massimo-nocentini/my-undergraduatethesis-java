package dotInterface;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import model.OurModel;
import model.Vertex;

import org.junit.After;
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

		Set<String> expectedDotModel = new HashSet<String>();

		expectedDotModel.add(v.provideId());

		Assert.assertEquals(expectedDotModel, exporter.getGraphDotBody());

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

		OurModel ourModel = OurModel.makeModel(vertices);

		DotExportable exportable = ourModel;

		DotExporter exporter = new SimpleExporter();
		exportable.acceptExporter(exporter);

		Set<String> expectedDotModel = new HashSet<String>();

		expectedDotModel.add(v.provideId());
		expectedDotModel.add(v2.provideId());
		expectedDotModel.add(v3.provideId());

		Assert.assertEquals(expectedDotModel, exporter.getGraphDotBody());

		DotFileUtilHandler.MakeHandler().writeDotRepresentationInTestFolder(
				exporter, "simpleThreeNodeChainDotExporting");
	}

	@After
	public void invokeDotCompilationForAllGeneratedFiles() {
		DotFileUtilHandler.MakeHandler().evaluateDotFilesInOutputFolder();
	}

}
