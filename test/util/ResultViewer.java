package util;

import java.awt.Color;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.ConnectedComponentInfoRecorder.ConnectedComponentInfoDataStructure;
import model.VertexType;
import piping.ConnectedComponentsInfoPipeFilterUnitTest;
import util.SetViewer.ModelsSelectionListener;
import dotInterface.DotFileUtilHandler;

public class ResultViewer extends JFrame {

	/**
	 * auto generated version uid
	 */
	private static final long serialVersionUID = 7289547140990123766L;

	private List<SetViewer> set_viewers;

	private final ModelsSelectionListener modelsSelectionListener;

	private final StatisticaInformationUpdater statisticaInformationUpdater;

	private interface StatisticaInformationUpdater {
		void update_by_requested_models(Set<String> models);
	}

	public ResultViewer(ConnectedComponentInfoDataStructure data_structure) {

		setLayout(new FlowLayout());

		Border border = BorderFactory
				.createMatteBorder(1, 1, 2, 2, Color.BLACK);

		final JTable summary_table = build_summary_table(data_structure, border);

		modelsSelectionListener = new ModelsSelectionListener() {

			@Override
			public void selection_changed(Set<String> models) {

				summary_table.clearSelection();

				int columnIndex = 0;
				for (String model : models) {
					for (int rowIndex = 0; rowIndex < summary_table.getModel()
							.getRowCount(); rowIndex = rowIndex + 1) {

						if (model.equals(summary_table.getModel()
								.getValueAt(rowIndex, columnIndex).toString())) {

							summary_table.addRowSelectionInterval(rowIndex,
									rowIndex);
						}
					}
				}
			}
		};

		statisticaInformationUpdater = build_statistical_list_box(
				data_structure, border);

		build_set_viewers(border);

		set_viewers.iterator().next().render(data_structure);

	}

	private StatisticaInformationUpdater build_statistical_list_box(
			final ConnectedComponentInfoDataStructure data_structure,
			Border border) {

		final DefaultListModel list_model = new DefaultListModel();
		update_list_model(
				data_structure
						.build_statistical_info_grouping_by_component_type_combination(),
				list_model);

		JList list_box = new JList(list_model);

		list_box.setBorder(border);
		list_box.setSize(150, 100);

		JScrollPane scrollPane = new JScrollPane(list_box);
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		this.add(scrollPane);

		return new StatisticaInformationUpdater() {

			@Override
			public void update_by_requested_models(Set<String> models) {
				list_model.clear();
				update_list_model(
						data_structure
								.build_statistical_info_grouping_by_component_type_combination(models),
						list_model);
			}
		};
	}

	private void update_list_model(
			Collection<String> build_statistical_info_grouping_by_component_type_combination,
			final DefaultListModel list_model) {

		for (String item : build_statistical_info_grouping_by_component_type_combination) {
			list_model.addElement(item);
		}
	}

	private JTable build_summary_table(
			final ConnectedComponentInfoDataStructure data_structure,
			Border border) {

		final JTable summary_table = new JTable(
				data_structure.build_rows_data(),
				data_structure.build_columns_data());

		summary_table.setBorder(border);

		summary_table.setFillsViewportHeight(true);

		summary_table.setRowSelectionAllowed(true);
		summary_table
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		summary_table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent e) {

						if (e.getValueIsAdjusting()) {
							return;
						}

						Set<String> selected_models = new TreeSet<String>();

						for (int row_index : summary_table.getSelectedRows()) {

							if (row_index == summary_table.getRowCount() - 1) {
								// this check is necessary in order to skip the
								// average row
								continue;
							}

							String model = summary_table.getModel()
									.getValueAt(row_index, 0).toString();

							selected_models.add(model);
						}

						statisticaInformationUpdater
								.update_by_requested_models(selected_models);
					}
				});

		add(new JScrollPane(summary_table));

		return summary_table;
	}

	private void build_set_viewers(Border border) {

		SetViewer models_set_viewer = new SetViewer(null,
				new SetViewerHookInterface.ForModels());

		SetViewer cardinalities_set_viewer = new SetViewer(models_set_viewer,
				new SetViewerHookInterface.ForCardinalities());

		SetViewer vertex_types_set_viewer = new SetViewer(
				cardinalities_set_viewer,
				new SetViewerHookInterface.ForVertexTypes());

		SetViewer species_set_viewer = new SetViewer(vertex_types_set_viewer,
				new SetViewerHookInterface.ForSpecies());

		species_set_viewer
				.accept_models_selection_listener(modelsSelectionListener);
		vertex_types_set_viewer
				.accept_models_selection_listener(modelsSelectionListener);
		cardinalities_set_viewer
				.accept_models_selection_listener(modelsSelectionListener);
		models_set_viewer
				.accept_models_selection_listener(modelsSelectionListener);

		set_viewers = new LinkedList<SetViewer>();
		set_viewers.addAll(Arrays.<SetViewer> asList(new SetViewer[] {
				species_set_viewer, vertex_types_set_viewer,
				cardinalities_set_viewer, models_set_viewer }));

		for (SetViewer set_viewer : this.set_viewers) {
			set_viewer.belong(this);
			set_viewer.useBorder(border);
		}

	}

	public static void main(String[] args) throws InterruptedException {

		int dimension = 500;

		File source_file = checked_BioCyc_models_file_handler();

		ConnectedComponentInfoDataStructure data_structure = load_serialized_data_structure(source_file);

		SwingConsole
				.run(new ResultViewer(data_structure), dimension, dimension);
	}

	private static File checked_standard_models_file_handler() {

		File serialized_datastructure_file = ConnectedComponentsInfoPipeFilterUnitTest.serialized_data_structure_for_standard_models_file_handler;

		if (serialized_datastructure_file.exists() == false) {

			ConnectedComponentsInfoPipeFilterUnitTest.apply_recording_on(
					DotFileUtilHandler.getSbmlExampleModelsFolder(),
					serialized_datastructure_file, false, "standard-models");
		}

		return serialized_datastructure_file;
	}

	private static File checked_first_BioCyc_model_file_handler() {

		File serialized_datastructure_file = ConnectedComponentsInfoPipeFilterUnitTest.serialized_data_structure_for_first_BioCyc_model_file_handler;

		if (serialized_datastructure_file.exists() == false) {

			ConnectedComponentsInfoPipeFilterUnitTest.apply_recording_on(
					DotFileUtilHandler.getSbmlExampleModelsFolder().concat(
							"BioCyc15.0/ACYPI/"),
					serialized_datastructure_file, false, "standard-models");
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

	private static ConnectedComponentInfoDataStructure load_serialized_data_structure(
			File source_file) {

		ConnectedComponentInfoDataStructure data_structure = null;
		try {
			InputStream input_stream = new FileInputStream(source_file);
			data_structure = new ConnectedComponentInfoDataStructure(
					input_stream);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return data_structure;
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
