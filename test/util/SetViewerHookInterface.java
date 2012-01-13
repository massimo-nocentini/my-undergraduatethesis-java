package util;

import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.ConnectedComponentInfoRecorder.ConnectedComponentInfoDataStructure;

public interface SetViewerHookInterface {

	ListSelectionListener supply_list_selection_listener(SetViewer setViewer);

	void render(SetViewer setViewer, Object aMap);

	public class ForSpecies implements SetViewerHookInterface {

		private SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> map = null;

		@Override
		public ListSelectionListener supply_list_selection_listener(
				final SetViewer setViewer) {

			return new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent e) {

					if (e.getValueIsAdjusting()) {
						return;
					}

					final JList sender = (JList) e.getSource();

					Object selectedValue = sender.getSelectedValue();

					if (selectedValue == null) {
						return;
					}

					final String selected_item = selectedValue.toString();

					SortedMap<String, SortedMap<Integer, SortedSet<String>>> components_associated_to_selected_species = map
							.get(selected_item);

					setViewer.getNext().render(
							components_associated_to_selected_species);

					// here we build a set of models relatives to this selection
					Set<String> models_associated_with_current_selection = new TreeSet<String>();
					for (String component_type : components_associated_to_selected_species
							.keySet()) {

						for (Integer cardinality : components_associated_to_selected_species
								.get(component_type).keySet()) {

							// aggiungo l'insieme di modelli da cui e' stato
							// possibile estrarre le informazioni relative alla
							// species selezionata, esplorando ogni tipo di
							// componente ed ogni cardinalita' di queste in
							// quanto la selezione di una sola species non
							// permette di poter raffinare la ricerca dei
							// modelli.
							models_associated_with_current_selection
									.addAll(components_associated_to_selected_species
											.get(component_type).get(
													cardinality));
						}
					}

					setViewer
							.notify_models_for_selection(models_associated_with_current_selection);
				}

			};
		}

		@SuppressWarnings("unchecked")
		@Override
		public void render(SetViewer setViewer, Object data_structure_as_object) {

			if (data_structure_as_object instanceof ConnectedComponentInfoDataStructure) {

				ConnectedComponentInfoDataStructure data_structure = (ConnectedComponentInfoDataStructure) data_structure_as_object;

				map = new TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>();
				data_structure.fill_datas_into(map);

				setViewer.add_to_model(map.keySet());
			}
		}
	}

	public class ForVertexTypes implements SetViewerHookInterface {

		private SortedMap<String, SortedMap<Integer, SortedSet<String>>> map = null;

		@Override
		public ListSelectionListener supply_list_selection_listener(
				final SetViewer setViewer) {

			return new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent e) {

					if (e.getValueIsAdjusting()) {
						return;
					}

					final JList sender = (JList) e.getSource();
					Object selectedValue = sender.getSelectedValue();

					if (selectedValue == null) {
						return;
					}

					final String selected_item = selectedValue.toString();

					SortedMap<Integer, SortedSet<String>> cardinalities_associated_to_selected_component_type = map
							.get(selected_item);

					setViewer
							.getNext()
							.render(cardinalities_associated_to_selected_component_type);

					// here we build a set of models relatives to this selection
					Set<String> models_associated_with_current_selection = new TreeSet<String>();
					for (Integer cardinality : cardinalities_associated_to_selected_component_type
							.keySet()) {

						// aggiungo l'insieme di modelli da cui e' stato
						// possibile estrarre le informazioni relative alla
						// species selezionata, esplorando ogni tipo di
						// componente ed ogni cardinalita' di queste in
						// quanto la selezione di una sola species non
						// permette di poter raffinare la ricerca dei
						// modelli.
						models_associated_with_current_selection
								.addAll(cardinalities_associated_to_selected_component_type
										.get(cardinality));
					}

					setViewer
							.notify_models_for_selection(models_associated_with_current_selection);
				}

			};
		}

		@SuppressWarnings("unchecked")
		@Override
		public void render(SetViewer setViewer, Object aMap) {

			try {
				map = (SortedMap<String, SortedMap<Integer, SortedSet<String>>>) aMap;
			} catch (Exception e) {
				map = null;
				return;
			}

			setViewer.add_to_model(map.keySet());
		}
	}

	public class ForCardinalities implements SetViewerHookInterface {

		private final static String model_count_separator = " (";
		private SortedMap<Integer, SortedSet<String>> map = null;

		@Override
		public ListSelectionListener supply_list_selection_listener(
				final SetViewer setViewer) {

			return new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent e) {

					if (e.getValueIsAdjusting()) {
						return;
					}

					final JList sender = (JList) e.getSource();
					Object selectedValue = sender.getSelectedValue();

					if (selectedValue == null) {
						return;
					}

					// because the method 'render' insert a set of strings that
					// we want to display on the listbox, here the selected item
					// is a string
					String selected_value_as_string = selectedValue.toString();
					selected_value_as_string = selected_value_as_string
							.substring(0, selected_value_as_string
									.indexOf(model_count_separator));

					final Integer selected_item = Integer
							.valueOf(selected_value_as_string);

					SortedSet<String> models_associated_to_selected_cardinality = map
							.get(selected_item);

					setViewer.getNext().render(
							models_associated_to_selected_cardinality);

					// here we build a set of models relatives to this selection
					Set<String> models_associated_with_current_selection = new TreeSet<String>();

					models_associated_with_current_selection
							.addAll(models_associated_to_selected_cardinality);

					setViewer
							.notify_models_for_selection(models_associated_with_current_selection);
				}

			};
		}

		@SuppressWarnings("unchecked")
		@Override
		public void render(SetViewer setViewer, Object aMap) {

			try {
				map = (SortedMap<Integer, SortedSet<String>>) aMap;
			} catch (Exception e) {
				map = null;
				return;
			}

			Set<String> keySet = new TreeSet<String>();
			for (Integer integer : map.keySet()) {
				keySet.add(integer.toString() + model_count_separator
						+ map.get(integer).size() + ")");
			}

			setViewer.add_to_model(keySet);
		}
	}

	public class ForModels implements SetViewerHookInterface {

		private SortedSet<String> map = null;

		@Override
		public ListSelectionListener supply_list_selection_listener(
				final SetViewer setViewer) {

			return new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent e) {

					if (e.getValueIsAdjusting()) {
						return;
					}

					final JList sender = (JList) e.getSource();
					Object selectedValue = sender.getSelectedValue();

					if (selectedValue == null) {
						return;
					}

					final String selected_item = selectedValue.toString();

					// here we don't need to retrieve the associated elements of
					// selected_item because we have no those elements. To be
					// complete and standard with should pass to
					// the other implementors an empty container, for brevity we
					// avoid this technique.
					// setViewer.getNext().render(new TreeSet<Object>());

					// here we build a set of models relatives to this selection
					Set<String> models_associated_with_current_selection = new TreeSet<String>();
					models_associated_with_current_selection.add(selected_item);

					setViewer
							.notify_models_for_selection(models_associated_with_current_selection);
				}

			};
		}

		@SuppressWarnings("unchecked")
		@Override
		public void render(SetViewer setViewer, Object aMap) {

			try {
				map = (SortedSet<String>) aMap;
			} catch (Exception e) {
				map = null;
				return;
			}

			setViewer.add_to_model(map);
		}
	}

}