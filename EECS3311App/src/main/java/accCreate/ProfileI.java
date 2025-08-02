package accCreate;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ProfileI extends ProfileTemplate {
	
	private int tempFtHeight;
	private int tempInHeight;
	private double tempWeight;
	
	private double heightFt;
	private double heightIn;
	private double height;
	private double weight;
	
	
	
	public ProfileI(MainUI main) {
		profileP1(main);
		profileP2(main);
	}

	@Override
	public void profileP2(MainUI main) {
		double totalInch =  currentUser.getHeight() / 2.54;
		tempFtHeight = (int) (totalInch / 12);
		tempInHeight = (int) Math.round(totalInch % 12);
		heightFt = tempFtHeight;
		heightIn = tempInHeight;
		weight = currentUser.getWeight();
		tempWeight = Math.round((weight *  2.205) * 100.0) / 100.0;
		weight = tempWeight;
				
		//height feet
		JLabel heightFtLabel = new JLabel("Height: (ft)");
		heightFtLabel.setBounds(winWidth+215, winHeight+160, 150, 25);
		add(heightFtLabel);
				
		JTextField heightFtField = new JTextField(String.valueOf(tempFtHeight));
		heightFtField.setBounds(winWidth+360, winHeight+160, 165, 25);
		add(heightFtField);
		heightFtField.addKeyListener(new KeyAdapter() {
			@Override
		    public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
		        if (!Character.isDigit(c) ) { 
		        	e.consume(); 
		        }
		    }
		});
		
		//height inch
		JLabel heightInLabel = new JLabel("Height: (in)");
		heightInLabel.setBounds(winWidth+215, winHeight+195, 150, 25);
		add(heightInLabel);
						
		JTextField heightInField = new JTextField(String.valueOf(tempInHeight));
		heightInField.setBounds(winWidth+360, winHeight+195, 165, 25);
		add(heightInField);
		heightInField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
						char c = e.getKeyChar();
				        if (!Character.isDigit(c) && !(c == '.')) { 
				        	e.consume(); 
				        }
				    }
				});
				
		//weight
		JLabel weightLabel = new JLabel("Weight: (lb)");
		weightLabel.setBounds(winWidth+215, winHeight+230, 150, 25);
		add(weightLabel);
				
		JTextField weightField = new JTextField(String.valueOf(tempWeight));
		weightField.setBounds(winWidth+360, winHeight+230, 165, 25);
		add(weightField);
		weightField.addKeyListener(new KeyAdapter() {
			@Override
		    public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
		        if (!Character.isDigit(c) && !(c == '.')) { 
		        	e.consume(); 
		        }
		    }
		});
		
		//save 
		JButton saveButton = new JButton("Save Changes");
		saveButton.setBounds(winWidth+55, winHeight+300, 255, 25);
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
				error = false;
				try {
					password = new String(passwordText.getPassword());
					if (password.length() == 0) {
						throw new Exception();
					}
				} catch (Exception ex0) {
					error = true;
					JOptionPane.showMessageDialog(null, "Invalid Password", "Error", JOptionPane.ERROR_MESSAGE);
				}
				try {
					year = Integer.parseInt(yearField.getText());
					month = Integer.parseInt(monthField.getText());
					day = Integer.parseInt(dayField.getText());
					dob = LocalDate.of(year, month, day);
					LocalDate current = LocalDate.now();
					if (dob.isAfter(current)) {
						throw new Exception();
					}
				} catch (Exception ex1) {
					error = true;
					JOptionPane.showMessageDialog(null, "Invalid Date of Birth", "Error", JOptionPane.ERROR_MESSAGE);
				}
				try {
					heightFt = Integer.parseInt(heightFtField.getText());
					heightIn = Integer.parseInt(heightInField.getText());
					if (heightFt <= 0 || heightIn >= 12) {
						throw new Exception();
					}
				} catch (Exception ex2) {
					error = true;
					JOptionPane.showMessageDialog(null, "Invalid Height", "Error", JOptionPane.ERROR_MESSAGE);
				}
				try {
					weight = Double.parseDouble(weightField.getText());
					if (weight <= 0) {
						throw new Exception();
					}
				} catch (Exception ex3) {
					error = true;
					JOptionPane.showMessageDialog(null, "Invalid Weight", "Error", JOptionPane.ERROR_MESSAGE);
				}
				if (error == false) {
					weight = weight * 0.453592;
					weight = Math.round(weight * 100.0) / 100.0;
					height = (heightFt * 30.48) + (heightIn * 2.54);
					main.profile1(username, password, measurement);
					main.profile2(sex, dob, weight, height);
					UserDataRW write = new UserDataRW();
					write.updateUser(currentUser);	
				}
			}
		});
		add(saveButton);
		
	}
}


