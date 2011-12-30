package util;

import java.awt.Dimension;
import java.util.Collection;

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

	private final JList list_box;
	private final DefaultListModel list_model;
	private final SetViewer next_set_viewer;
	private final SetViewerHookInterface hookInterface;

	public SetViewer(SetViewer next_set_viewer,
			SetViewerHookInterface hookInterface) {

		this.next_set_viewer = next_set_viewer;

		this.hookInterface = hookInterface;

		list_model = new DefaultListModel();

		list_box = new JList(list_model);
		list_box.setMaximumSize(new Dimension(100, 100));
		list_box.setMinimumSize(new Dimension(50, 100));
		list_box.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_box.addListSelectionListener(this.hookInterface
				.supply_list_selection_listener(this));
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

	public void add_to_model(final Collection<?> objects) {

		for (Object obj : objects) {
			if (obj == null) {
				continue;
			}

			list_model.addElement(obj);
		}
	}
}
