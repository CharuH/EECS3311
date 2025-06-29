package accCreate;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Settings extends JPanel {
	
	private UserData currentUser;
	private String measurement;
	private String tempMeasurement;
	private double height;
	private double weight;

	public Settings(MainUI main) {
		setPreferredSize(new Dimension(750, getHeight()));
		setLayout(null);
		setBackground(Color.WHITE);
		
		currentUser = main.getUser();
		tempMeasurement = currentUser.getUnits();
		measurement = tempMeasurement;
		
		//Title
		JLabel titleLabel = new JLabel("Settings");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		titleLabel.setBounds(360, 85, 180, 30);
		add(titleLabel);
				
				
		//measurement label
		JLabel measureLabel = new JLabel("Preferred Units");
		measureLabel.setBounds(255, 150, 150, 25); 
		add(measureLabel);
							
		//measurement drop-down box
		String[] measureOptions = {"Metric", "Imperial"};
		JComboBox<String> measureBox = new JComboBox<>(measureOptions);
		measureBox.setSelectedItem(tempMeasurement);
		measureBox.setBounds(355, 150, 165, 25);
		measureBox.setBackground(Color.white);
		measureBox.addActionListener(e -> {
			measurement = (String) measureBox.getSelectedItem();
		});
		add(measureBox);
		
		//save 
		JButton saveButton = new JButton("Save Changes");
		saveButton.setBounds(255, 450, 255, 25);
		saveButton.setBackground(new Color(58, 162, 224));
		saveButton.setContentAreaFilled(false);
		add(saveButton);
		saveButton.addMouseListener(new MouseAdapter() {
            // When the mouse enters the button, change color
            public void mouseEntered(MouseEvent e) {
                saveButton.setContentAreaFilled(true);
            }
            
            // When the mouse exits the button, reset the color
            public void mouseExited(MouseEvent e) {
                saveButton.setContentAreaFilled(false); 
            }
        });
		saveButton.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
				main.profile1(currentUser.getUsername(), currentUser.getPassword() , measurement);
				UserDataRW write = new UserDataRW();
				write.updateUser(currentUser);	
			}
		});
		add(saveButton);
	}
}
