package drawitgui2;

import java.awt.BorderLayout;

import javax.swing.*;

public class GUI extends JFrame {
	
	private DrawItCanvas canvas = new DrawItCanvas();
	private JTextField messageField = new JTextField();
	private JPanel canvasPanel = new JPanel();
	private JTextArea textArea = new JTextArea();
	private JScrollPane scrollPane = new JScrollPane(textArea);
	private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, canvasPanel, scrollPane);
	
	private GUI() {
		super("DrawIt");
		canvas.textArea = textArea;
		messageField.setEditable(false);
		canvasPanel.setLayout(new BorderLayout());
		canvasPanel.add(canvas, BorderLayout.CENTER);
		canvasPanel.add(messageField, BorderLayout.SOUTH);
		getContentPane().add(splitPane);
		canvas.messageField = messageField;
		
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new GUI().setVisible(true);
		});
	}
	
}
