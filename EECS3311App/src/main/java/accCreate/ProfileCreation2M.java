package accCreate;

import java.awt.Color;
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
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ProfileCreation2M extends ProfileCreation2Template {
	private double height;
	private double weight;
	
	public ProfileCreation2M(LoginUI main){
		profileP1(main);
		profileP2(main);
	}

	@Override
	public void profileP2(LoginUI main) {
		//height
		JLabel heightLabel = new JLabel("Height: (cm)");
		heightLabel.setBounds(winWidth+10, winHeight+160, 150, 25);
		add(heightLabel);
				
		JTextField heightField = new JTextField(4);
		heightField.setBounds(winWidth+145, winHeight+160, 165, 25);
		add(heightField);
		heightField.addKeyListener(new KeyAdapter() {
			@Override
		    public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!Character.isDigit(c) && !(c == '.')) { 
					e.consume(); 
		        }
			}
		});
				
		//weight
		JLabel weightLabel = new JLabel("Weight: (kg)");
		weightLabel.setBounds(winWidth+10, winHeight+195, 150, 25);
		add(weightLabel);
				
		JTextField weightField = new JTextField(4);
		weightField.setBounds(winWidth+145, winHeight+195, 165, 25);
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
				
		//next
		JButton nextButton = new JButton("Next");
		nextButton.setBounds(winWidth+255, winHeight+265, 80, 25);
		add(nextButton);
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
				error = false;
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
					ErrorWindow errorDoB = new ErrorWindow("Invalid Date of Birth");
					errorDoB.errorMessage();
				}
				try {
					height = Double.parseDouble(heightField.getText());
					if (height <= 0) {
						throw new Exception();
					}
				} catch (Exception ex2) {
					error = true;
					ErrorWindow errorHeight = new ErrorWindow("Invalid Height");
					errorHeight.errorMessage();
				}
				try {
					weight = Double.parseDouble(weightField.getText());
					if (weight <= 0) {
						throw new Exception();
					}
				} catch (Exception ex3) {
					error = true;
					ErrorWindow errorWeight = new ErrorWindow("Invalid Weight");
					errorWeight.errorMessage();
				}
				if (error == false) {
					main.profile2(sex, dob, weight, height);
					UserDataRW write = new UserDataRW();
					write.writeUser(main.getUser());
					main.login();
				}
			}
		});
		
	}

}
