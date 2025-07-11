package accCreate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
        errorFrame.setLayout(new BorderLayout());

        // Create a label for the error message
        JLabel errorLabel = new JLabel(errorMessage, SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED); // Set the error message to red

        // Add the label to the error window
        errorFrame.add(errorLabel, BorderLayout.CENTER);
        
        JButton okButton = new JButton("OK");
        okButton.setBackground(new Color(58, 162, 224));
		okButton.setContentAreaFilled(false);
		okButton.addMouseListener(new MouseAdapter() {
            // When the mouse enters the button, change color
            public void mouseEntered(MouseEvent e) {
                okButton.setContentAreaFilled(true);
            }
            
            // When the mouse exits the button, reset the color
            public void mouseExited(MouseEvent e) {
                okButton.setContentAreaFilled(false); 
            }
        });
        okButton.addActionListener(ev -> errorFrame.dispose());
        okButton.setSize(80, 25);
        
        JPanel button = new JPanel();
        button.setBackground(Color.WHITE);
        button.add(okButton);
        errorFrame.add(button, BorderLayout.SOUTH);

        errorFrame.setVisible(true);
	}
}
