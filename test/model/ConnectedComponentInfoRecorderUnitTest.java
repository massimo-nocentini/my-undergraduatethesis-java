package model;

import java.io.ByteArrayOutputStream;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import junit.framework.Assert;
import model.ConnectedComponentInfoRecorder.ConnectedComponentInfoDataStructure;

import org.junit.Test;

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

		recorder.putTuple(species, "sources", 1, "model1");

		TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> expectedMap = new TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>();

		expectedMap.put(species,
				new TreeMap<String, SortedMap<Integer, SortedSet<String>>>());

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		recorder.writeDataStructure(byteArrayOutputStream);

		Assert.assertTrue(recorder
				.isDataStructureEquals(new ConnectedComponentInfoDataStructure(
						expectedMap)));

		Assert.assertFalse(recorder
				.isDataStructureEquals(new ConnectedComponentInfoDataStructure(
						null)));

		Assert.assertFalse(recorder.isDataStructureEquals(null));
	}
}
