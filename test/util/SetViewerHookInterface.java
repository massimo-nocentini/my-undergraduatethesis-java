package util;

import java.util.SortedMap;
import java.util.SortedSet;

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

					final Integer selected_item = (Integer) selectedValue;

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

			setViewer.add_to_model(map.keySet());
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