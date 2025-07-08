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
		settingsButton.addMouseListener(new MouseAdapter() {
            // When the mouse enters the button, change color
            public void mouseEntered(MouseEvent e) {
                settingsButton.setBackground(new Color(58, 162, 224));
            }
            
            // When the mouse exits the button, reset the color
            public void mouseExited(MouseEvent e) {
                settingsButton.setBackground(Color.white);
            }
        });
		settingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	main.switchToScreen("settings");
            }
        });
		add(settingsButton);
		
		JButton profileButton = new JButton("Profile");
		profileButton.setBounds(25, 65, 100, 25);
		profileButton.setBackground(Color.white);
		profileButton.addMouseListener(new MouseAdapter() {
            // When the mouse enters the button, change color
            public void mouseEntered(MouseEvent e) {
                profileButton.setBackground(new Color(58, 162, 224));
            }
            
            // When the mouse exits the button, reset the color
            public void mouseExited(MouseEvent e) {
                profileButton.setBackground(Color.white); 
            }
        });

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
		logButton.addMouseListener(new MouseAdapter() {
            // When the mouse enters the button, change color
            public void mouseEntered(MouseEvent e) {
                profileButton.setBackground(new Color(58, 162, 224));
            }
            
            // When the mouse exits the button, reset the color
            public void mouseExited(MouseEvent e) {
                profileButton.setBackground(Color.white); 
            }
        });

		logButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	main.switchToScreen("log");
            	
            }
        });
		add(logButton);

		JButton cfgButton = new JButton("CFG");
		cfgButton.setBounds(25, 135, 100, 25);
		cfgButton.setBackground(Color.white);
		cfgButton.addMouseListener(new MouseAdapter() {
            // When the mouse enters the button, change color
            public void mouseEntered(MouseEvent e) {
                profileButton.setBackground(new Color(58, 162, 224));
            }
            
            // When the mouse exits the button, reset the color
            public void mouseExited(MouseEvent e) {
                profileButton.setBackground(Color.white); 
            }
        });

		cfgButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	main.switchToScreen("cfg");
            	
            }
        });
		add(cfgButton);
	}
}
