package util;

import java.awt.Color;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.border.Border;

import model.ConnectedComponentInfoRecorder.ConnectedComponentInfoDataStructure;
import model.VertexType;
import dotInterface.DotFileUtilHandler;

public class ResultViewer extends JFrame {

	/**
	 * auto generated version uid
	 */
	private static final long serialVersionUID = 7289547140990123766L;

	private List<SetViewer> set_viewers;

	public ResultViewer(
			SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> map) {

		this.build_set_viewers();

		setLayout(new FlowLayout());

		Border border = BorderFactory
				.createMatteBorder(1, 1, 2, 2, Color.BLACK);

		for (SetViewer set_viewer : this.set_viewers) {
			set_viewer.belong(this);
			set_viewer.useBorder(border);
		}

		set_viewers.iterator().next().render(map);

	}

	private void build_set_viewers() {

		SetViewer models_set_viewer = new SetViewer(null,
				new SetViewerHookInterface.ForModels());

		SetViewer cardinalities_set_viewer = new SetViewer(models_set_viewer,
				new SetViewerHookInterface.ForCardinalities());

		SetViewer vertex_types_set_viewer = new SetViewer(
				cardinalities_set_viewer,
				new SetViewerHookInterface.ForVertexTypes());

		SetViewer species_set_viewer = new SetViewer(vertex_types_set_viewer,
				new SetViewerHookInterface.ForSpecies());

		set_viewers = new LinkedList<SetViewer>();
		set_viewers.addAll(Arrays.<SetViewer> asList(new SetViewer[] {
				species_set_viewer, vertex_types_set_viewer,
				cardinalities_set_viewer, models_set_viewer }));

	}

	public static void main(String[] args) throws InterruptedException {

		int dimension = 500;

		File source_file = new File(
				DotFileUtilHandler
						.dotOutputFolderPathName()
						.concat("massive-connected-components-info-serialized-for-standard-models.datastructure"));

		SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> loaded_map = load_map_from_serialized_data_structure(source_file);

		// modify this assignment to render your maps.
		// SortedMap<String, SortedMap<String, SortedMap<Integer,
		// SortedSet<String>>>> test_map = build_test_map();

		SwingConsole.run(new ResultViewer(loaded_map), dimension, dimension);
	}

	private static SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> load_map_from_serialized_data_structure(
			File source_file) {

		SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> map = null;
		try {
			InputStream input_stream = new FileInputStream(source_file);
			ConnectedComponentInfoDataStructure data_structure = new ConnectedComponentInfoDataStructure(
					input_stream);

			map = new TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>();
			data_structure.fill_datas_into(map);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return map;
	}

	private static SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> build_test_map() {

		SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> map = new TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>();

		String species_one = "species_one";
		String species_two = "species_two";
		String model_name_one = "model_one";
		String model_name_two = "model_two";
		String model_name_three = "model_three";
		String model_name_four = "model_four";
		String model_name_five = "model_five";
		String model_name_six = "model_six";

		ConnectedComponentInfoDataStructure.putIntoMap(map, species_one,
				VertexType.Sources.toString(), 3, model_name_one);
		ConnectedComponentInfoDataStructure.putIntoMap(map, species_one,
				VertexType.Sources.toString(), 3, model_name_two);
		ConnectedComponentInfoDataStructure.putIntoMap(map, species_one,
				VertexType.Sources.toString(), 3, model_name_three);

		ConnectedComponentInfoDataStructure.putIntoMap(map, species_one,
				VertexType.Sources.toString(), 1, model_name_one);
		ConnectedComponentInfoDataStructure.putIntoMap(map, species_one,
				VertexType.Sources.toString(), 1, model_name_two);

		ConnectedComponentInfoDataStructure.putIntoMap(map, species_one,
				VertexType.Sinks.toString(), 2, model_name_four);
		ConnectedComponentInfoDataStructure.putIntoMap(map, species_one,
				VertexType.Sinks.toString(), 2, model_name_five);

		ConnectedComponentInfoDataStructure.putIntoMap(map, species_one,
				VertexType.Whites.toString(), 1, model_name_six);

		// the same settings for the second species.
		ConnectedComponentInfoDataStructure.putIntoMap(map, species_two,
				VertexType.Sources.toString(), 1, model_name_one);

		ConnectedComponentInfoDataStructure.putIntoMap(map, species_two,
				VertexType.Whites.toString(), 4, model_name_two);
		ConnectedComponentInfoDataStructure.putIntoMap(map, species_two,
				VertexType.Whites.toString(), 4, model_name_three);
		ConnectedComponentInfoDataStructure.putIntoMap(map, species_two,
				VertexType.Whites.toString(), 4, model_name_four);
		ConnectedComponentInfoDataStructure.putIntoMap(map, species_two,
				VertexType.Whites.toString(), 4, model_name_five);

		ConnectedComponentInfoDataStructure.putIntoMap(map, species_two,
				VertexType.Sinks.toString(), 1, model_name_six);

		return map;
	}
}
