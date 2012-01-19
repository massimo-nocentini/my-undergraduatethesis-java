package util;

import java.awt.Dimension;
import java.util.Map;
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
import util.SetViewer.ListboxElementKeyRender.AdditionalInformationRenderer;

public interface SetViewerHookInterface {

	ListSelectionListener supply_list_selection_listener(SetViewer setViewer);

	void render(SetViewer setViewer, Object aMap);

	Dimension get_maximum_size(SetViewer setViewer);

	Dimension get_minimum_size(SetViewer setViewer);

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

				Map<String, IntegerCounter> distribution_map = new TreeMap<String, IntegerCounter>();

				Set<ElementKeyRender> renders = new TreeSet<SetViewer.ElementKeyRender>();

				for (final String species : map.keySet()) {

					AdditionalInformationRenderer renderer = new AdditionalInformationRenderer() {

						@Override
						public boolean should_be_rendered() {
							return true;
						}

						@Override
						public String get_representation() {

							return ((Integer) map.get(species).size())
									.toString();

						}
					};

					renders.add(new ListboxElementKeyRender(species, renderer));
				}

				// for (String species : map.keySet()) {
				//
				// distribution_map.put(species, new IntegerCounter());
				//
				// for (String component_type : map.get(species).keySet()) {
				//
				// for (Integer cardinality : map.get(species)
				// .get(component_type).keySet()) {
				//
				// int size = map.get(species).get(component_type)
				// .get(cardinality).size();
				//
				// distribution_map.get(species).increment(size);
				//
				// }
				// }
				// }

				// final int components_among_all_models = data_structure
				// .compute_number_of_components_among_all_models();

				// int max_presence = Integer.MIN_VALUE;
				// for (final Entry<String, IntegerCounter> entry :
				// distribution_map
				// .entrySet()) {
				//
				// Integer presence = entry.getValue().getCount();
				//
				// if (presence > max_presence) {
				// max_presence = presence;
				// }
				// }
				//
				// // final Set<Double> frequencies = new TreeSet<Double>();
				// final int copy_of_max_presence = max_presence;
				//
				// for (final Entry<String, IntegerCounter> entry :
				// distribution_map
				// .entrySet()) {
				//
				// AdditionalInformationRenderer renderer = new
				// AdditionalInformationRenderer() {
				//
				// @Override
				// public boolean should_be_rendered() {
				// return true;
				// }
				//
				// @Override
				// public String get_representation() {
				//
				// DecimalFormat formatter = new DecimalFormat(
				// "#.####");
				//
				// double frequency = entry.getValue().getCount()
				// * (entry.getValue().getCount() / (double)
				// copy_of_max_presence);
				//
				// // frequencies.add(frequency);
				//
				// return "weighed-freq: "
				// + formatter.format(frequency);
				// }
				// };
				//
				// renders.add(new ListboxElementKeyRender(entry.getKey(),
				// renderer));
				// }

				setViewer.add_to_model(renders);
			}
		}

		@Override
		public Dimension get_maximum_size(SetViewer setViewer) {
			return new Dimension(100, 500);
		}

		@Override
		public Dimension get_minimum_size(SetViewer setViewer) {
			return new Dimension(100, 500);
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

			Set<ElementKeyRender> renders = new TreeSet<SetViewer.ElementKeyRender>();
			for (final String vertex_type : map.keySet()) {

				AdditionalInformationRenderer renderer = new AdditionalInformationRenderer() {

					@Override
					public boolean should_be_rendered() {
						return true;
					}

					@Override
					public String get_representation() {
						return ((Integer) map.get(vertex_type).size())
								.toString();
					}
				};

				renders.add(new ListboxElementKeyRender(vertex_type, renderer));

			}

			setViewer.add_to_model(renders);
		}

		@Override
		public Dimension get_maximum_size(SetViewer setViewer) {
			return setViewer.getDefaultMaximumSize();
		}

		@Override
		public Dimension get_minimum_size(SetViewer setViewer) {
			return setViewer.getDefaultMinimumSize();
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

			Set<ElementKeyRender> renders = new TreeSet<SetViewer.ElementKeyRender>();
			for (final Integer cardinality : map.keySet()) {

				AdditionalInformationRenderer renderer = new AdditionalInformationRenderer() {

					@Override
					public boolean should_be_rendered() {
						return true;
					}

					@Override
					public String get_representation() {
						return ((Integer) map.get(cardinality).size())
								.toString();
					}
				};

				renders.add(new ListboxElementKeyRender(cardinality, renderer));
			}

			setViewer.add_to_model(renders);
		}

		@Override
		public Dimension get_maximum_size(SetViewer setViewer) {
			return setViewer.getDefaultMaximumSize();
		}

		@Override
		public Dimension get_minimum_size(SetViewer setViewer) {
			return setViewer.getDefaultMinimumSize();
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

			Set<ElementKeyRender> renders = new TreeSet<SetViewer.ElementKeyRender>();
			for (String model : map) {

				AdditionalInformationRenderer renderer = new AdditionalInformationRenderer() {

					@Override
					public boolean should_be_rendered() {
						return false;
					}

					@Override
					public String get_representation() {
						throw new RuntimeException(
								"This method should not be called due to the should_be_rendered will");
					}
				};

				renders.add(new ListboxElementKeyRender(model, renderer));
			}

			setViewer.add_to_model(renders);
		}

		@Override
		public Dimension get_maximum_size(SetViewer setViewer) {
			return setViewer.getDefaultMaximumSize();
		}

		@Override
		public Dimension get_minimum_size(SetViewer setViewer) {
			return setViewer.getDefaultMinimumSize();
		}
	}

}