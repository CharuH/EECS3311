package accCreate;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

public class SidePanel extends JPanel {
	
	public SidePanel(MainUI main) {
		setLayout(null);
		setBackground(Color.gray);
		setPreferredSize(new Dimension(150, getHeight()));
		
		setButton(new JButton("Settings"), 30, "settings", main);
		setProfileButton(new JButton("Profile"), 65, main);
		setButton(new JButton("Log"), 100, "log", main);
		setButton(new JButton("CFG"), 135, "cfg", main);
		setButton(new JButton("Nutrition"), 170, "nutrition", main);
		setButton(new JButton("Logout"), 205, "logout", main);
	}
	
	private void buttonHover(JButton button) {
		button.addMouseListener(new MouseAdapter() {
            // When the mouse enters the button, change color
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(58, 162, 224));
            }
            
            // When the mouse exits the button, reset the color
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.white); 
            }
        });
	}
	
	private void buttonClicked(JButton button, String screen, MainUI main) {
		button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	main.switchToScreen(screen);
            }
        });
	}
	
	private void buttonClickedProfile(JButton button, MainUI main) {
		button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	UserData data = main.getUser();
            	if (data.getUnits().equals("Metric")) {
            		main.switchToScreen("profileM");
            	} else {
            		main.switchToScreen("profileI");
            	}
            }
        });
	}
	
	private void setButton(JButton button, int yValue, String screen, MainUI main) {
		button.setBounds(25, yValue, 100, 25);
		button.setBackground(Color.white);
		buttonHover(button);
		buttonClicked(button, screen, main);
		add(button);
	}
	
	private void setProfileButton(JButton button, int yValue, MainUI main) {
		button.setBounds(25, yValue, 100, 25);
		button.setBackground(Color.white);
		buttonHover(button);
		buttonClickedProfile(button, main);
		add(button);
	}
}
