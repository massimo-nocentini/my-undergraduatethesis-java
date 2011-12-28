package util;

import java.awt.Color;
import java.awt.FlowLayout;
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

public class ResultViewer extends JFrame {

	/**
	 * auto generated version uid
	 */
	private static final long serialVersionUID = 7289547140990123766L;

	private List<SetViewer> set_viewers;

	// public ResultViewer(InputStream inputStream) {
	public ResultViewer() {

		SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> map = new TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>();

		this.fill_test_map(map);

		ConnectedComponentInfoDataStructure data_structure = new ConnectedComponentInfoDataStructure(
				map);

		this.build_set_viewers(data_structure);

		setLayout(new FlowLayout());

		Border border = BorderFactory
				.createMatteBorder(1, 1, 2, 2, Color.BLACK);

		for (SetViewer set_viewer : this.set_viewers) {
			set_viewer.belong(this);
			set_viewer.useBorder(border);
		}

		set_viewers.iterator().next().render(map);

	}

	private void fill_test_map(
			SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> map) {

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

	}

	private void build_set_viewers(
			ConnectedComponentInfoDataStructure data_structure) {

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

		// probably here we can build a FileInputStream to use for deserializing
		// the data structure.
		InputStream some_input_stream;

		int dimension = 500;
		SwingConsole.run(new ResultViewer(), dimension, dimension);

	}
}
