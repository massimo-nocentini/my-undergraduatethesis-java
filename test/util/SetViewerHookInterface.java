package util;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.ConnectedComponentInfoRecorder.ConnectedComponentInfoDataStructure;
import util.SetViewer.ElementKeyRender;
import util.SetViewer.ListboxElementKeyRender;

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

					if ((selectedValue instanceof ElementKeyRender) == false) {
						return;
					}

					final ElementKeyRender selected_item = (ElementKeyRender) selectedValue;

					SortedMap<String, SortedMap<Integer, SortedSet<String>>> components_associated_to_selected_species = map
							.get(selected_item.get_key());

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

		@Override
		public void render(SetViewer setViewer, Object data_structure_as_object) {

			if (data_structure_as_object instanceof ConnectedComponentInfoDataStructure) {

				ConnectedComponentInfoDataStructure data_structure = (ConnectedComponentInfoDataStructure) data_structure_as_object;

				map = new TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>();
				data_structure.fill_datas_into(map);

				Set<ElementKeyRender> renders = new HashSet<SetViewer.ElementKeyRender>();
				for (String species : map.keySet()) {
					renders.add(new ListboxElementKeyRender(species, map.get(
							species).size()));
				}

				setViewer.add_to_model(renders);
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

					if ((selectedValue instanceof ElementKeyRender) == false) {
						return;
					}

					final ElementKeyRender selected_item = (ElementKeyRender) selectedValue;

					SortedMap<Integer, SortedSet<String>> cardinalities_associated_to_selected_component_type = map
							.get(selected_item.get_key());

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

			Set<ElementKeyRender> renders = new HashSet<SetViewer.ElementKeyRender>();
			for (String vertex_type : map.keySet()) {
				renders.add(new ListboxElementKeyRender(vertex_type, map.get(
						vertex_type).size()));
			}

			setViewer.add_to_model(renders);
		}
	}

	public class ForCardinalities implements SetViewerHookInterface {

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

					if ((selectedValue instanceof ElementKeyRender) == false) {
						return;
					}

					final ElementKeyRender selected_item = (ElementKeyRender) selectedValue;

					SortedSet<String> models_associated_to_selected_cardinality = map
							.get(selected_item.get_key());

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

			Set<ElementKeyRender> renders = new HashSet<SetViewer.ElementKeyRender>();
			for (Integer cardinality : map.keySet()) {
				renders.add(new ListboxElementKeyRender(cardinality, map.get(
						cardinality).size()));
			}

			setViewer.add_to_model(renders);
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

					if ((selectedValue instanceof ElementKeyRender) == false) {
						return;
					}

					final ElementKeyRender selected_item = (ElementKeyRender) selectedValue;

					// here we don't need to retrieve the associated elements of
					// selected_item because we have no those elements. To be
					// complete and standard with should pass to
					// the other implementors an empty container, for brevity we
					// avoid this technique.
					// setViewer.getNext().render(new TreeSet<Object>());

					// here we build a set of models relatives to this selection
					Set<String> models_associated_with_current_selection = new TreeSet<String>();
					models_associated_with_current_selection.add(selected_item
							.get_key().toString());

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

			Set<ElementKeyRender> renders = new HashSet<SetViewer.ElementKeyRender>();
			for (String model : map) {
				renders.add(new ListboxElementKeyRender(model, 1));
			}

			setViewer.add_to_model(renders);
		}
	}

}