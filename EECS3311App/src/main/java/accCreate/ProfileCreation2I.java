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

public class ProfileCreation2I extends JPanel {
	private int winWidth;
	private int winHeight;
	
	private String sex = "Male";
	private int year;
	private int month;
	private int day;
	private LocalDate dob;
	private double heightMetric;
	private int heightFt;
	private int heightIn;
	private double weightMetric;
	private double weightImperial;
	
	private boolean error = false;
	
	public ProfileCreation2I(LoginUI main){
		winWidth = main.getSize().width / 4;
		winHeight = main.getSize().height / 4;
		
		setLayout(null);
		setBackground(Color.WHITE);
		
		//Title
		JLabel titleLabel = new JLabel("New Account");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		titleLabel.setBounds(winWidth+115, winHeight-65, 180, 30);
		add(titleLabel);
		
		//sex label
		JLabel sexLabel = new JLabel("Sex");
		sexLabel.setBounds(winWidth+10, winHeight+20, 150, 25); 
		add(sexLabel);
		
		//sex drop-down box
		String[] sexOptions = {"Male", "Female"};
		JComboBox<String> sexBox = new JComboBox<>(sexOptions);
		sexBox.setBackground(Color.white);
		sexBox.setBounds(winWidth+145, winHeight+20, 165, 25);
		sexBox.addActionListener(e -> {
			sex = (String) sexBox.getSelectedItem();
		});
		add(sexBox);
		
		//year label
		JLabel yearLabel = new JLabel("Birth Year (yyyy)");
		yearLabel.setBounds(winWidth+10, winHeight+55, 150, 25); 
		add(yearLabel);
						
		//year field
		JTextField yearField = new JTextField(4);
		yearField.setBounds(winWidth+145, winHeight+55, 165, 25);
		add(yearField);
		yearField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) { // Allow only digits
                    e.consume(); // Ignore non-digit characters
                }
            }
        });
		
		//month label
		JLabel monthLabel = new JLabel("Birth Month (mm)");
		monthLabel.setBounds(winWidth+10, winHeight+90, 150, 25); 
		add(monthLabel);
						
		//month field
		JTextField monthField = new JTextField(2);
		monthField.setBounds(winWidth+145, winHeight+90, 165, 25);
		add(monthField);
		monthField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) { // Allow only digits
                    e.consume(); // Ignore non-digit characters
                }
            }
        });
				
		//day label
		JLabel dayLabel = new JLabel("Birth Day (dd)");
		dayLabel.setBounds(winWidth+10, winHeight+125, 150, 25); 
		add(dayLabel);
		
		//day field
		JTextField dayField = new JTextField(2);
		dayField.setBounds(winWidth+145, winHeight+125, 165, 25);
		add(dayField);
		dayField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) { // Allow only digits
                    e.consume(); // Ignore non-digit characters
                }
            }
        });
		
		//height feet
		JLabel heightFtLabel = new JLabel("Height: (ft)");
		heightFtLabel.setBounds(winWidth+10, winHeight+160, 150, 25);
		add(heightFtLabel);
		
		JTextField heightFtField = new JTextField(4);
		heightFtField.setBounds(winWidth+145, winHeight+160, 165, 25);
		add(heightFtField);
		heightFtField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) { 
                    e.consume(); 
                }
            }
        });
		
		//height inch
		JLabel heightInLabel = new JLabel("Height: (in)");
		heightInLabel.setBounds(winWidth+10, winHeight+195, 150, 25);
		add(heightInLabel);
				
		JTextField heightInField = new JTextField(4);
		heightInField.setBounds(winWidth+145, winHeight+195, 165, 25);
		add(heightInField);
		heightInField.addKeyListener(new KeyAdapter() {
			@Override
		    public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
		        if (!Character.isDigit(c)) { 
		        	e.consume(); 
		        }
		    }
		});
		
		//weight
		JLabel weightLabel = new JLabel("Weight: (lb)");
		weightLabel.setBounds(winWidth+10, winHeight+230, 150, 25);
		add(weightLabel);
		
		JTextField weightField = new JTextField(4);
		weightField.setBounds(winWidth+145, winHeight+230, 165, 25);
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
		nextButton.setBounds(winWidth+255, winHeight+300, 80, 25);
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
				} catch (Exception ex1) {
					error = true;
					ErrorWindow errorDoB = new ErrorWindow("Invalid Date of Birth");
					errorDoB.errorMessage();
				}
				try {
					heightFt = Integer.parseInt(heightFtField.getText());
					heightIn = Integer.parseInt(heightInField.getText());
					if (heightFt <= 0 || heightIn >= 12) {
						throw new Exception();
					}
				} catch (Exception ex2) {
					error = true;
					ErrorWindow errorHeight = new ErrorWindow("Invalid Height");
					errorHeight.errorMessage();
				}
				try {
					weightImperial = Double.parseDouble(weightField.getText());
					if (weightImperial <= 0) {
						throw new Exception();
					}
				} catch (Exception ex3) {
					error = true;
					ErrorWindow errorWeight = new ErrorWindow("Invalid Weight");
					errorWeight.errorMessage();
				}
				if (error == false) {
					weightMetric = weightImperial * 0.453592;
					weightMetric = Math.round(weightMetric * 100.0) / 100.0;
					heightMetric = (heightFt * 30.48) + (heightIn * 2.54);
					main.profile2(sex, dob, weightMetric, heightMetric);
					UserDataRW write = new UserDataRW();
					write.writeUser(main.getUser());
					main.login();
				}
			}
		});
	}
}
