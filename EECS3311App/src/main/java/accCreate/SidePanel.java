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
		
		JButton settingsButton = new JButton("Settings");
		settingsButton.setBounds(25, 30, 100, 25);
		settingsButton.setBackground(Color.white);
		buttonHover(settingsButton);
		buttonClicked(settingsButton, "settings", main);
		add(settingsButton);
		
		JButton profileButton = new JButton("Profile");
		profileButton.setBounds(25, 65, 100, 25);
		profileButton.setBackground(Color.white);
		buttonHover(profileButton);
		profileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	UserData data = main.getUser();
            	if (data.getUnits().equals("Metric")) {
            		main.switchToScreen("profileM");
            	} else {
            		main.switchToScreen("profileI");
            	}
            }
        });
		add(profileButton);

		JButton logButton = new JButton("Log");
		logButton.setBounds(25, 100, 100, 25);
		logButton.setBackground(Color.white);
		buttonHover(logButton);
		buttonClicked(logButton, "log", main);
		add(logButton);

		JButton cfgButton = new JButton("CFG");
		cfgButton.setBounds(25, 135, 100, 25);
		cfgButton.setBackground(Color.white);
		buttonHover(cfgButton);
		buttonClicked(cfgButton, "cfg", main);
		add(cfgButton);

		JButton nutriButton = new JButton("Nutrition");
		nutriButton.setBounds(25, 170, 100, 25);
		nutriButton.setBackground(Color.white);
		buttonHover(nutriButton);
		buttonClicked(nutriButton, "nutrition", main);
		add(nutriButton);

		JButton logoutButton = new JButton("Logout");
		logoutButton.setBounds(25, 205, 100, 25);
		logoutButton.setBackground(Color.white);
		buttonHover(logoutButton);
		buttonClicked(logoutButton, "logout", main);
		add(logoutButton);
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
}
