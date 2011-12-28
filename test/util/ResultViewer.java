package util;

import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class ResultViewer extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7289547140990123766L;

	JLabel label;

	public ResultViewer() {
		super("Hello swing");

		label = new JLabel("A label");

		add(label);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 300);
		setVisible(true);
	}

	static ResultViewer resultViewer;

	public static void main(String[] args) throws InterruptedException {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				resultViewer = new ResultViewer();
			}
		});

		TimeUnit.SECONDS.sleep(1);

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				resultViewer.label
						.setText("This text is different from the previous one");
			}
		});

	}
}
