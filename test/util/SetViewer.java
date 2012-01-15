package util;

import java.awt.Dimension;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;

public class SetViewer {

	public static interface DoOn {
		void doOnWith(SetViewer viewer);
	}

	public static interface ModelsSelectionListener {
		void selection_changed(Set<String> models);
	}

	public static interface ElementKeyRender extends
			Comparable<ElementKeyRender> {

		Object get_key();

		@Override
		String toString();
	}

	public static class ListboxElementKeyRender implements ElementKeyRender {

		private final Object key;
		private final AdditionalInformationRenderer renderer;

		public static interface AdditionalInformationRenderer {

			String get_representation();

			boolean should_be_rendered();
		}

		public ListboxElementKeyRender(Object key,
				AdditionalInformationRenderer renderer) {
			this.key = key;
			this.renderer = renderer;
		}

		@Override
		public Object get_key() {
			return key;
		}

		@Override
		public String toString() {

			String result = key.toString();

			if (renderer.should_be_rendered() == true) {

				result = result.concat(" (")
						.concat(renderer.get_representation()).concat(")");
			}

			return result;
		}

		@Override
		public int compareTo(ElementKeyRender o) {

			return this.toString().compareTo(o.toString());
		}

	}

	private final JList list_box;
	private final DefaultListModel list_model;
	private final SetViewer next_set_viewer;
	private final SetViewerHookInterface hookInterface;
	private ModelsSelectionListener modelsSelectionListener;

	public void accept_models_selection_listener(
			ModelsSelectionListener modelsSelectionListener) {

		this.modelsSelectionListener = modelsSelectionListener;
	}

	public SetViewer(SetViewer next_set_viewer,
			SetViewerHookInterface hookInterface) {

		this.next_set_viewer = next_set_viewer;

		this.hookInterface = hookInterface;

		list_model = new DefaultListModel();

		list_box = new JList(list_model);
		list_box.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_box.addListSelectionListener(this.hookInterface
				.supply_list_selection_listener(this));
	}

	public Dimension getDefaultMinimumSize() {
		return new Dimension(50, 100);
	}

	public Dimension getDefaultMaximumSize() {
		return new Dimension(100, 100);
	}

	public void belong(JFrame container) {

		JScrollPane scrollPane = new JScrollPane(list_box);
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		container.add(scrollPane);
	}

	public void useBorder(Border border) {

		list_box.setBorder(border);
	}

	protected void clear() {

		if (next_set_viewer != null) {

			next_set_viewer.clear();
		}

		list_model.removeAllElements();
	}

	public void render(Object aMap) {

		this.clear();
		this.hookInterface.render(this, aMap);
	}

	public SetViewer getNext() {
		return next_set_viewer;
	}

	public void add_to_model(final Set<ElementKeyRender> renders) {

		for (ElementKeyRender obj : renders) {

			list_model.addElement(obj);
		}
	}

	public void notify_models_for_selection(Set<String> new_selected_models) {

		this.modelsSelectionListener.selection_changed(new_selected_models);
	}
}
