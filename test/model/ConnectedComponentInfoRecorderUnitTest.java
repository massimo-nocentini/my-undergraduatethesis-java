package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import junit.framework.Assert;
import model.ConnectedComponentInfoRecorder.ConnectedComponentInfoDataStructure;
import model.ConnectedComponentInfoRecorder.ConnectedComponentInfoDataStructure.ConnectedComponentPairCounter;

import org.junit.Test;

import dotInterface.DotFileUtilHandler;

public class ConnectedComponentInfoRecorderUnitTest {

	@Test
	public void just_created_recorder_should_have_empty_map() {

		ConnectedComponentInfoRecorder recorder = new ConnectedComponentInfoRecorder();

		String species = "my_species_identifier";

		TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> someMap = new TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>();

		someMap.put(species,
				new TreeMap<String, SortedMap<Integer, SortedSet<String>>>());

		Assert.assertTrue(recorder
				.isDataStructureEquals(ConnectedComponentInfoDataStructure
						.make_empty_datastructure()));

		Assert.assertFalse(recorder
				.isDataStructureEquals(ConnectedComponentInfoDataStructure
						.make_datastructure_with_species_map(someMap)));

		Assert.assertFalse(recorder.isDataStructureEquals(null));
	}

	@Test
	public void putting_a_species_into_new_recorder_should_contains_only_the_putted_species() {

		ConnectedComponentInfoRecorder recorder = new ConnectedComponentInfoRecorder();

		String species = "my_species";
		String componentType = VertexType.Sources.toString();
		int cardinality = 1;
		String modelName = "model1";

		recorder.recordTupleBySpecies(species, componentType, cardinality,
				modelName);

		TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> expectedMap = new TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>();

		ConnectedComponentInfoDataStructure.put_tuples_by_species_into(
				expectedMap, species, componentType, cardinality, modelName);

		TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> unexpectedMap = new TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>();

		// do some permutation on the expected values in order to distinguish
		// this map from the previous one
		ConnectedComponentInfoDataStructure.put_tuples_by_species_into(
				unexpectedMap, species + "hello", componentType + "hello",
				cardinality + 2, modelName);

		Assert.assertTrue(recorder
				.isDataStructureEquals(ConnectedComponentInfoDataStructure
						.make_datastructure_with_species_map(expectedMap)));

		Assert.assertFalse(recorder
				.isDataStructureEquals(ConnectedComponentInfoDataStructure
						.make_datastructure_with_species_map(unexpectedMap)));

		Assert.assertFalse(recorder.isDataStructureEquals(null));
	}

	@Test
	public void serializing_the_map_should_produce_an_non_empty_file() {

		ConnectedComponentInfoRecorder recorder = new ConnectedComponentInfoRecorder();

		String species = "my_species";
		String componentType = VertexType.Sources.toString();
		int cardinality = 1;
		String modelName = "model1";

		recorder.recordTupleBySpecies(species, componentType, cardinality,
				modelName);

		String pathname = DotFileUtilHandler
				.dotOutputFolderPathName()
				.concat("simple-serialization-of-connected-component-data-structure.txt");

		File destinationFile = new File(pathname);

		if (destinationFile.exists() == true) {
			destinationFile.delete();
		}

		try {
			Assert.assertFalse(destinationFile.exists());

			FileOutputStream fileOutputStream = new FileOutputStream(
					destinationFile);

			recorder.toJavaSerialization(fileOutputStream);

			fileOutputStream.close();

			Assert.assertTrue(destinationFile.exists());

		} catch (FileNotFoundException e) {
			Assert.fail("The file should be written in the destination path.");
		} catch (IOException e) {
			Assert.fail("The file should be written in the destination path.");
		}
	}

	@Test
	public void serializing_the_map_and_deserializing_it_should_produce_an_identity() {

		ConnectedComponentInfoRecorder recorder = new ConnectedComponentInfoRecorder();

		String species = "my_species";
		String componentType = VertexType.Sources.toString();
		int cardinality = 1;
		String modelName = "model1";

		recorder.recordTupleBySpecies(species, componentType, cardinality,
				modelName);

		String pathname = DotFileUtilHandler
				.dotOutputFolderPathName()
				.concat("serialization-for-inverse-checking-of-connected-component-data-structure.txt");

		File destinationFile = new File(pathname);

		if (destinationFile.exists() == true) {
			destinationFile.delete();
		}

		try {
			Assert.assertFalse(destinationFile.exists());

			FileOutputStream fileOutputStream = new FileOutputStream(
					destinationFile);

			recorder.toJavaSerialization(fileOutputStream);

			fileOutputStream.close();

			Assert.assertTrue(destinationFile.exists());

		} catch (FileNotFoundException e) {
			Assert.fail("The file should be written in the destination path.");
		} catch (IOException e) {
			Assert.fail("The file should be written in the destination path.");
		}

		try {
			InputStream fileInputStream = new FileInputStream(destinationFile);
			ConnectedComponentInfoDataStructure deserialized = new ConnectedComponentInfoDataStructure(
					fileInputStream);

			Assert.assertTrue(recorder.isDataStructureEquals(deserialized));
		} catch (FileNotFoundException e) {
			Assert.fail("The file should be present, inconsistency with the previous assertions.");
		}
	}

	@Test
	public void recording_an_isolated_connected_component_should_modify_the_data_structure() {

		ConnectedComponentInfoRecorder recorder = new ConnectedComponentInfoRecorder();

		ConnectedComponentWrapperVertex componentWrapperVertex = VertexFactory
				.makeConnectedComponentWrapperVertex();

		String species = "my_species_identifier";
		String componentType = VertexType.Sinks.toString();
		int cardinality = 1;
		String model_name = "model";

		// setting up the connected component to have one member and one
		// neighbor in order to be a source (otherwise we cannot distinguish if
		// it is a source or a sink)
		Vertex vertex = VertexFactory.makeSimpleVertex(species,
				OurModel.getDefaultCompartmentId());

		Set<Vertex> vertices = new HashSet<Vertex>();
		vertices.add(vertex);

		@SuppressWarnings("unused")
		OurModel model = OurModel.makeOurModelFrom(vertices, model_name);

		componentWrapperVertex.includeMember(vertex);

		componentWrapperVertex.publishYourContentOn(recorder);

		SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> expected_species_map = new TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>();

		ConnectedComponentInfoDataStructure.put_tuples_by_species_into(
				expected_species_map, "MY_SPECIES_IDENTIFIER-(COMPARTMENT_ID)",
				componentType, cardinality, model_name);

		SortedMap<String, SortedMap<String, ConnectedComponentPairCounter>> expected_models_map = new TreeMap<String, SortedMap<String, ConnectedComponentPairCounter>>();
		ConnectedComponentInfoDataStructure.put_tuples_by_models_into(
				expected_models_map, model_name, VertexType.Sinks.toString(),
				cardinality);

		Assert.assertTrue(recorder
				.isDataStructureEquals(ConnectedComponentInfoDataStructure
						.make_datastructure_with_both_maps(
								expected_species_map, expected_models_map)));
	}

	@Test(expected = RuntimeException.class)
	public void connected_component_without_members_should_throw_exception_when_asked_for_model_name() {

		ConnectedComponentWrapperVertex connected_component = VertexFactory
				.makeConnectedComponentWrapperVertex();

		connected_component.findModelName();
	}

	@Test
	public void connected_component_with_some_members_should_find_something_when_asked_for_model_name() {

		ConnectedComponentWrapperVertex componentWrapperVertex = VertexFactory
				.makeConnectedComponentWrapperVertex();

		String modelName = "model";

		Vertex vertex_one = VertexFactory.makeSimpleVertex("species_one",
				OurModel.getDefaultCompartmentId());

		Vertex vertex_two = VertexFactory.makeSimpleVertex("species_two",
				OurModel.getDefaultCompartmentId());

		Vertex vertex_three = VertexFactory.makeSimpleVertex("species_three",
				OurModel.getDefaultCompartmentId());

		Set<Vertex> vertices = new HashSet<Vertex>();
		vertices.add(vertex_one);
		vertices.add(vertex_two);
		vertices.add(vertex_three);

		@SuppressWarnings("unused")
		OurModel model = OurModel.makeOurModelFrom(vertices, modelName);

		componentWrapperVertex.includeMember(vertex_one);
		componentWrapperVertex.includeMember(vertex_two);
		componentWrapperVertex.includeMember(vertex_three);

		Assert.assertEquals(modelName, componentWrapperVertex.findModelName());
	}

	@Test
	public void building_map_from_only_one_model_should_produce_flat_result() {
		ConnectedComponentInfoRecorder recorder = new ConnectedComponentInfoRecorder();

		SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> expectedMap = new TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>();
		SortedMap<String, SortedMap<String, ConnectedComponentPairCounter>> expected_models_map = new TreeMap<String, SortedMap<String, ConnectedComponentPairCounter>>();

		this.build_same_context_with_changes_on_model(recorder, "model_one",
				expectedMap, expected_models_map);

		Assert.assertTrue(recorder
				.isDataStructureEquals(ConnectedComponentInfoDataStructure
						.make_datastructure_with_both_maps(expectedMap,
								expected_models_map)));
	}

	@Test
	public void building_map_from_two_models_with_the_same_vertices_structures() {

		ConnectedComponentInfoRecorder recorder = new ConnectedComponentInfoRecorder();

		SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> expectedMap = new TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>();
		SortedMap<String, SortedMap<String, ConnectedComponentPairCounter>> expected_models_map = new TreeMap<String, SortedMap<String, ConnectedComponentPairCounter>>();

		this.build_same_context_with_changes_on_model(recorder, "model_one",
				expectedMap, expected_models_map);

		this.build_same_context_with_changes_on_model(recorder, "model_two",
				expectedMap, expected_models_map);

		Assert.assertTrue(recorder
				.isDataStructureEquals(ConnectedComponentInfoDataStructure
						.make_datastructure_with_both_maps(expectedMap,
								expected_models_map)));
	}

	private void build_same_context_with_changes_on_model(
			ConnectedComponentInfoRecorder recorder,
			String model_name,
			SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> expected_species_map,
			SortedMap<String, SortedMap<String, ConnectedComponentPairCounter>> expected_models_map) {

		// creating the components
		ConnectedComponentWrapperVertex scc_one = VertexFactory
				.makeConnectedComponentWrapperVertex();

		ConnectedComponentWrapperVertex scc_two = VertexFactory
				.makeConnectedComponentWrapperVertex();

		ConnectedComponentWrapperVertex scc_three = VertexFactory
				.makeConnectedComponentWrapperVertex();

		// creating the vertices
		Vertex vertex_one = VertexFactory.makeSimpleVertex("species_one",
				OurModel.getDefaultCompartmentId());

		Vertex vertex_two = VertexFactory.makeSimpleVertex("species_two",
				OurModel.getDefaultCompartmentId());

		Vertex vertex_three = VertexFactory.makeSimpleVertex("species_three",
				OurModel.getDefaultCompartmentId());

		Vertex vertex_four = VertexFactory.makeSimpleVertex("species_four",
				OurModel.getDefaultCompartmentId());

		Vertex vertex_five = VertexFactory.makeSimpleVertex("species_five",
				OurModel.getDefaultCompartmentId());

		Vertex vertex_six = VertexFactory.makeSimpleVertex("species_six",
				OurModel.getDefaultCompartmentId());

		// setting up neighborhood relations
		vertex_one.addNeighbour(vertex_two).addNeighbour(vertex_six);
		vertex_two.addNeighbour(vertex_one).addNeighbour(vertex_three);
		vertex_three.addNeighbour(vertex_four).addNeighbour(vertex_six);
		vertex_four.addNeighbour(vertex_five);
		vertex_five.addNeighbour(vertex_three);

		// building the set in order to build the model
		Set<Vertex> vertices = new HashSet<Vertex>();
		vertices.add(vertex_one);
		vertices.add(vertex_two);
		vertices.add(vertex_three);
		vertices.add(vertex_four);
		vertices.add(vertex_five);
		vertices.add(vertex_six);

		// the following statement is needed only to update
		// the vertices respect the container model relation
		@SuppressWarnings("unused")
		OurModel model = OurModel.makeOurModelFrom(vertices, model_name);

		// including the vertices in the respective components
		scc_one.includeMember(vertex_one);
		scc_one.includeMember(vertex_two);

		scc_two.includeMember(vertex_three);
		scc_two.includeMember(vertex_four);
		scc_two.includeMember(vertex_five);

		scc_three.includeMember(vertex_six);

		// building neighborhood relation about connected components
		scc_one.addNeighbour(scc_two);
		scc_two.addNeighbour(scc_three);

		// publishing the informations
		scc_one.publishYourContentOn(recorder);
		scc_two.publishYourContentOn(recorder);
		scc_three.publishYourContentOn(recorder);

		// for this species the suffix doesn't start with "-()-"
		// because the species_name is an empty string.
		String species_identifier_suffix = "-(COMPARTMENT_ID)";

		ConnectedComponentInfoDataStructure.put_tuples_by_species_into(
				expected_species_map,
				"SPECIES_ONE".concat(species_identifier_suffix),
				VertexType.Sources.toString(), 2, model_name);
		ConnectedComponentInfoDataStructure.put_tuples_by_species_into(
				expected_species_map,
				"SPECIES_TWO".concat(species_identifier_suffix),
				VertexType.Sources.toString(), 2, model_name);
		ConnectedComponentInfoDataStructure.put_tuples_by_species_into(
				expected_species_map,
				"SPECIES_THREE".concat(species_identifier_suffix),
				VertexType.Whites.toString(), 3, model_name);
		ConnectedComponentInfoDataStructure.put_tuples_by_species_into(
				expected_species_map,
				"SPECIES_FOUR".concat(species_identifier_suffix),
				VertexType.Whites.toString(), 3, model_name);
		ConnectedComponentInfoDataStructure.put_tuples_by_species_into(
				expected_species_map,
				"SPECIES_FIVE".concat(species_identifier_suffix),
				VertexType.Whites.toString(), 3, model_name);
		ConnectedComponentInfoDataStructure.put_tuples_by_species_into(
				expected_species_map,
				"SPECIES_SIX".concat(species_identifier_suffix),
				VertexType.Sinks.toString(), 1, model_name);

		// this publish refers to scc_one
		ConnectedComponentInfoDataStructure.put_tuples_by_models_into(
				expected_models_map, model_name, VertexType.Sources.toString(),
				2);

		// this publish refers to scc_two
		ConnectedComponentInfoDataStructure.put_tuples_by_models_into(
				expected_models_map, model_name, VertexType.Whites.toString(),
				3);

		// this publish refers to scc_three
		ConnectedComponentInfoDataStructure
				.put_tuples_by_models_into(expected_models_map, model_name,
						VertexType.Sinks.toString(), 1);
	}
}
