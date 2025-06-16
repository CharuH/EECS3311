package accCreate;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ErrorWindow {

	private String errorMessage;
	
	public ErrorWindow(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public void errorMessage() {
		JFrame errorFrame = new JFrame("Error");
        errorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        errorFrame.setSize(300, 150);
        errorFrame.getContentPane().setBackground(Color.WHITE);

        // Create a label for the error message
        JLabel errorLabel = new JLabel(errorMessage, SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED); // Set the error message to red

        // Add the label to the error window
        errorFrame.add(errorLabel);

        errorFrame.setVisible(true);
	}
}
