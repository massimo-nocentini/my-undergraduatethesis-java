package util;

import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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

					setViewer.getNext().render(map.get(selected_item));
				}

			};
		}

		@SuppressWarnings("unchecked")
		@Override
		public void render(SetViewer setViewer, Object aMap) {

			try {
				map = (SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>) aMap;
			} catch (Exception e) {
				map = null;
				return;
			}

			setViewer.add_to_model(map.keySet());
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

					setViewer.getNext().render(map.get(selected_item));
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

					setViewer.getNext().render(map.get(selected_item));
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

					// this listener haven't to do nothing

					// final JList sender = (JList) e.getSource();
					// final String selected_item = sender.getSelectedValue()
					// .toString();
					//
					// setViewer.doOnNext(new SetViewer.DoOn() {
					//
					// @Override
					// public void doOnWith(SetViewer viewer) {
					//
					// // first we erase all the previous content
					// viewer.clear();
					//
					// // after we ask to render to correct amount of
					// // information
					// viewer.render(map.get(selected_item));
					// }
					// });

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