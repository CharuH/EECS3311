package accCreate;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ProfileCreation1 extends JPanel {
	private int winWidth;
	private int winHeight;
	
	private String username;
	private String password;
	private String repassword;
	private String measurement = "Metric";
		
	public ProfileCreation1(LoginUI main) {
		winWidth = main.getSize().width / 4;
		winHeight = main.getSize().height / 4;
		
		setLayout(null);
		setBackground(Color.WHITE);
		
		//Title
		JLabel titleLabel = new JLabel("New Account");
		//titleLabel.setForeground(new Color(42, 215, 23));
		titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		titleLabel.setBounds(winWidth+115, winHeight-65, 180, 30);
		add(titleLabel);
		
		JLabel userLabel = new JLabel("Enter a Username");
		userLabel.setBounds(winWidth+10, winHeight+20, 150, 25); //x, y, width, height
		add(userLabel);
		
		JTextField userText = new JTextField(20);
		userText.setBounds(winWidth+145, winHeight+20, 165, 25);
		add(userText);
		
		JLabel passwordLabel = new JLabel("Enter a Password");
		passwordLabel.setBounds(winWidth+10, winHeight+55, 150, 25);
		add(passwordLabel);
		
		JPasswordField passwordText = new JPasswordField();
		passwordText.setBounds(winWidth+145, winHeight+55, 165, 25);
		add(passwordText);
		
		JLabel repasswordLabel = new JLabel("Re-enter Password");
		repasswordLabel.setBounds(winWidth+10, winHeight+90, 150, 25);
		add(repasswordLabel);
		
		JPasswordField repasswordText = new JPasswordField();
		repasswordText.setBounds(winWidth+145, winHeight+90, 165, 25);
		add(repasswordText);

		//measurement label
		JLabel measureLabel = new JLabel("Preferred Units");
		measureLabel.setBounds(winWidth+10, winHeight+125, 150, 25); 
		add(measureLabel);
			
		//measurement drop-down box
		String[] measureOptions = {"Metric", "Imperial"};
		JComboBox<String> measureBox = new JComboBox<>(measureOptions);
		measureBox.setBounds(winWidth+145, winHeight+125, 165, 25);
		measureBox.setBackground(Color.white);
		measureBox.addActionListener(e -> {
			measurement = (String) measureBox.getSelectedItem();
		});
		add(measureBox);
		
		JButton nextButton = new JButton("Next");
		nextButton.setBounds(winWidth+255, winHeight+195, 80, 25);
		nextButton.setBackground(new Color(58, 162, 224));
		nextButton.setContentAreaFilled(false);
		add(nextButton);
		nextButton.addMouseListener(new MouseAdapter() {
            // When the mouse enters the button, change color
            public void mouseEntered(MouseEvent e) {
                nextButton.setContentAreaFilled(true);
            }
            
            // When the mouse exits the button, reset the color
            public void mouseExited(MouseEvent e) {
                nextButton.setContentAreaFilled(false); 
            }
        });
		nextButton.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
				username = userText.getText();
				password = new String(passwordText.getPassword());
				repassword =  new String(repasswordText.getPassword());
				UserDataRW check = new UserDataRW();
				if (!check.searchUsername(username) && username.length() > 0) {
					if (password.equals(repassword) && password.length() > 0) {
						main.profile1(username, password, measurement);
						if (measurement.equals("Metric")) {
							main.switchToScreen("createProfile2M");
						} else {
							main.switchToScreen("createProfile2I");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Invalid or Non-matching Password", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Username Already Taken", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		add(nextButton);
	}
}
