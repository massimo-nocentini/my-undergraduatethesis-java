package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import junit.framework.Assert;
import model.ConnectedComponentInfoRecorder.ConnectedComponentInfoDataStructure;

import org.junit.Test;

import dotInterface.DotFileUtilHandler;

public class ConnectedComponentInfoRecorderUnitTest {

	@Test
	public void just_created_recorder_should_have_empty_map() {

		ConnectedComponentInfoRecorder recorder = new ConnectedComponentInfoRecorder();

		String species = "my_species";

		TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> someMap = new TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>();

		someMap.put(species,
				new TreeMap<String, SortedMap<Integer, SortedSet<String>>>());

		Assert.assertTrue(recorder
				.isDataStructureEquals(new ConnectedComponentInfoDataStructure()));

		Assert.assertFalse(recorder
				.isDataStructureEquals(new ConnectedComponentInfoDataStructure(
						someMap)));

		Assert.assertFalse(recorder.isDataStructureEquals(null));
	}

	@Test
	public void putting_a_species_into_new_recorder_should_contains_only_the_putted_species() {

		ConnectedComponentInfoRecorder recorder = new ConnectedComponentInfoRecorder();

		String species = "my_species";
		String componentType = "sources";
		int cardinality = 1;
		String modelName = "model1";

		recorder.putTuple(species, componentType, cardinality, modelName);

		TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> expectedMap = new TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>();

		ConnectedComponentInfoDataStructure.putIntoMap(expectedMap, species,
				componentType, cardinality, modelName);

		TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> unexpectedMap = new TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>();

		ConnectedComponentInfoDataStructure.putIntoMap(unexpectedMap, species
				+ "hello", componentType + "hello", cardinality + 2, modelName);

		Assert.assertTrue(recorder
				.isDataStructureEquals(new ConnectedComponentInfoDataStructure(
						expectedMap)));

		Assert.assertFalse(recorder
				.isDataStructureEquals(new ConnectedComponentInfoDataStructure(
						unexpectedMap)));

		Assert.assertFalse(recorder.isDataStructureEquals(null));
	}

	@Test
	public void serializing_the_map_should_produce_an_non_empty_file() {

		ConnectedComponentInfoRecorder recorder = new ConnectedComponentInfoRecorder();

		String species = "my_species";
		String componentType = "sources";
		int cardinality = 1;
		String modelName = "model1";

		recorder.putTuple(species, componentType, cardinality, modelName);

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

			recorder.writeDataStructure(fileOutputStream);

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
		String componentType = "sources";
		int cardinality = 1;
		String modelName = "model1";

		recorder.putTuple(species, componentType, cardinality, modelName);

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

			recorder.writeDataStructure(fileOutputStream);

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

		String species = "my_species";
		String componentType = "sources";
		int cardinality = 1;
		String modelName = "model1";

		// setting up the connected component to have one member and one
		// neighbor in order to be a source (otherwise we cannot distinguish if
		// it is a source or a sink)
		componentWrapperVertex.includeMember(VertexFactory.makeSimpleVertex(
				species, OurModel.getDefaultCompartmentId()));

		componentWrapperVertex.addNeighbour(VertexFactory
				.makeConnectedComponentWrapperVertex());

		componentWrapperVertex.publishYourContentOn(recorder);

		TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> expectedMap = new TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>();

		ConnectedComponentInfoDataStructure.putIntoMap(expectedMap, species,
				componentType, cardinality, modelName);

		Assert.assertTrue(recorder
				.isDataStructureEquals(new ConnectedComponentInfoDataStructure(
						expectedMap)));
	}
}
