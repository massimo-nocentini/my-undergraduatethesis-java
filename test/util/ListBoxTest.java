package util;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ListBoxTest extends JFrame {

	private final String[] flavors = { "Chocolate", "Strawberry",
			"Vanilla Fudge Swirl", "Mint Chip", "Mocha Almond Fudge",
			"Rum Raisin", "Praline Cream" };

	private final DefaultListModel l_items = new DefaultListModel();
	private final JList jList = new JList(l_items);
	private final JTextArea textArea = new JTextArea(flavors.length, 20);
	private final JButton button = new JButton("Add Item");
	private int count = 0;

	private final ActionListener action_listener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			l_items.addElement("Hello");

			if (count < flavors.length) {
				l_items.add(0, flavors[count]);
				count = count + 1;
			} else {
				button.setEnabled(false);
			}
		}
	};

	private final ListSelectionListener list_selection_listener = new ListSelectionListener() {

		@Override
		public void valueChanged(ListSelectionEvent e) {

			if (e.getValueIsAdjusting()) {
				return;
			}

			textArea.setText("");

			for (Object item : jList.getSelectedValues()) {
				textArea.append(item.toString() + "\n");
			}
		}
	};

	public ListBoxTest() {

		textArea.setEditable(true);
		setLayout(new FlowLayout());
		Border border = BorderFactory
				.createMatteBorder(1, 1, 2, 2, Color.BLACK);

		jList.setBorder(border);
		textArea.setBorder(border);

		for (int i = 0; i < 4; i = i + 1) {
			this.l_items.addElement(flavors[count]);
			count = count + 1;
		}

		add(textArea);
		add(new JScrollPane(jList));
		add(button);

		jList.addListSelectionListener(list_selection_listener);
		button.addActionListener(action_listener);
	}

	public static void main(String[] args) {
		int dimension = 500;
		SwingConsole.run(new ListBoxTest(), dimension, dimension);
	}

	public static class SwingConsole {
		public static void run(final JFrame f, final int width, final int height) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {

					f.setTitle(f.getClass().getSimpleName());
					f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					f.setSize(width, height);
					f.setVisible(true);
				}
			});
		}
	}
}
