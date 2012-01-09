package util;

import java.awt.Color;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import piping.ConnectedComponentsInfoPipeFilterUnitTest;
import dotInterface.DotFileUtilHandler;

public class ResultViewer extends JFrame {

	/**
	 * auto generated version uid
	 */
	private static final long serialVersionUID = 7289547140990123766L;

	private List<SetViewer> set_viewers;

	public ResultViewer(
			SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> map) {

		build_set_viewers();

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

		// File source_file = checked_standard_models_file_handler();
		File source_file = checked_BioCyc_models_file_handler();

		SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> loaded_map = load_map_from_serialized_data_structure(source_file);

		SwingConsole.run(new ResultViewer(loaded_map), dimension, dimension);
	}

	private static File checked_standard_models_file_handler() {

		File serialized_datastructure_file = ConnectedComponentsInfoPipeFilterUnitTest.serialized_data_structure_for_standard_models_file_handler;

		if (serialized_datastructure_file.exists() == false) {

			ConnectedComponentsInfoPipeFilterUnitTest
					.apply_recording_on(
							DotFileUtilHandler.getSbmlExampleModelsFolder(),
							ConnectedComponentsInfoPipeFilterUnitTest.serialized_data_structure_for_standard_models_file_handler,
							false, "standard-models");
		}

		return serialized_datastructure_file;
	}

	private static File checked_BioCyc_models_file_handler() {

		File serialized_datastructure_file = ConnectedComponentsInfoPipeFilterUnitTest.serialized_data_structure_for_BioCyc_models_file_handler;

		if (serialized_datastructure_file.exists() == false) {

			File BioCyc_folder = new File(DotFileUtilHandler
					.getSbmlExampleModelsFolder().concat("BioCyc15.0"));

			if (BioCyc_folder.exists() == false) {

				String uncompress_command = "tar -C "
						.concat(DotFileUtilHandler
								.getSbmlExampleModelsFolderAsFile()
								.getAbsolutePath()).concat(" -xf ")
						.concat(BioCyc_folder.getAbsolutePath())
						.concat(".tar.gz");

				try {
					Runtime.getRuntime().exec(uncompress_command).waitFor();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			ConnectedComponentsInfoPipeFilterUnitTest.apply_recording_on(
					BioCyc_folder.getAbsolutePath(),
					serialized_datastructure_file, true, "BioCyc-models");
		}

		return serialized_datastructure_file;
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

		ConnectedComponentInfoDataStructure.put_tuples_by_species_into(map,
				species_one, VertexType.Sources.toString(), 3, model_name_one);
		ConnectedComponentInfoDataStructure.put_tuples_by_species_into(map,
				species_one, VertexType.Sources.toString(), 3, model_name_two);
		ConnectedComponentInfoDataStructure
				.put_tuples_by_species_into(map, species_one,
						VertexType.Sources.toString(), 3, model_name_three);

		ConnectedComponentInfoDataStructure.put_tuples_by_species_into(map,
				species_one, VertexType.Sources.toString(), 1, model_name_one);
		ConnectedComponentInfoDataStructure.put_tuples_by_species_into(map,
				species_one, VertexType.Sources.toString(), 1, model_name_two);

		ConnectedComponentInfoDataStructure.put_tuples_by_species_into(map,
				species_one, VertexType.Sinks.toString(), 2, model_name_four);
		ConnectedComponentInfoDataStructure.put_tuples_by_species_into(map,
				species_one, VertexType.Sinks.toString(), 2, model_name_five);

		ConnectedComponentInfoDataStructure.put_tuples_by_species_into(map,
				species_one, VertexType.Whites.toString(), 1, model_name_six);

		// the same settings for the second species.
		ConnectedComponentInfoDataStructure.put_tuples_by_species_into(map,
				species_two, VertexType.Sources.toString(), 1, model_name_one);

		ConnectedComponentInfoDataStructure.put_tuples_by_species_into(map,
				species_two, VertexType.Whites.toString(), 4, model_name_two);
		ConnectedComponentInfoDataStructure.put_tuples_by_species_into(map,
				species_two, VertexType.Whites.toString(), 4, model_name_three);
		ConnectedComponentInfoDataStructure.put_tuples_by_species_into(map,
				species_two, VertexType.Whites.toString(), 4, model_name_four);
		ConnectedComponentInfoDataStructure.put_tuples_by_species_into(map,
				species_two, VertexType.Whites.toString(), 4, model_name_five);

		ConnectedComponentInfoDataStructure.put_tuples_by_species_into(map,
				species_two, VertexType.Sinks.toString(), 1, model_name_six);

		return map;
	}
}
