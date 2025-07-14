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
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ProfileM extends JPanel {

	private int winWidth;
	private int winHeight;
	
	private UserData currentUser;
	private String tempPassword;
	private String tempSex;
	private int tempYear;
	private int tempMonth;
	private int tempDay;
	private double tempHeight;
	private double tempWeight;
	
	private String username;
	private String password;
	private String measurement;
	private String sex;
	private int year;
	private int month;
	private int day;
	private LocalDate dob;
	private double height;
	private double weight;
	
	private boolean error = false;
	
	public ProfileM(MainUI main) {
		winWidth = 200;
		winHeight = 150;
		setPreferredSize(new Dimension(750, getHeight()));
		setLayout(null);
		setBackground(Color.WHITE);
		
		currentUser = main.getUser();
		username = currentUser.getUsername();
		tempPassword = currentUser.getPassword();
		password = tempPassword;
		measurement = currentUser.getUnits();
		tempSex = currentUser.getSex();
		sex = tempSex;
		tempYear = currentUser.getDob().getYear();
		year = tempYear;
		tempMonth = currentUser.getDob().getMonthValue();
		month = tempMonth;
		tempDay = currentUser.getDob().getDayOfMonth();
		day = tempDay;
		tempHeight = currentUser.getHeight();
		height = tempHeight;
		tempWeight = currentUser.getWeight();
		weight = tempWeight;
		
		//Title
		JLabel titleLabel = new JLabel("Profile");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		titleLabel.setBounds(360, 85, 180, 30);
		add(titleLabel);
		
		//username
		JLabel userLabel = new JLabel("Username");
		userLabel.setBounds(winWidth-140, winHeight+80, 80, 25); //x, y, width, height
		add(userLabel);
		
		JTextField userText = new JTextField(username);
		userText.setEditable(false);
		userText.setBounds(winWidth+5, winHeight+80, 165, 25);
		add(userText);

		//password
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(winWidth-140, winHeight+115, 80, 25);
		add(passwordLabel);
		
		JPasswordField passwordText = new JPasswordField(tempPassword);
		passwordText.setBounds(winWidth+5, winHeight+115, 165, 25);
		add(passwordText);
		
		//sex label
		JLabel sexLabel = new JLabel("Sex");
		sexLabel.setBounds(winWidth+215, winHeight+20, 150, 25); 
		add(sexLabel);
				
		//sex drop-down box
		String[] sexOptions = {"Male", "Female"};
		JComboBox<String> sexBox = new JComboBox<>(sexOptions);
		sexBox.setSelectedItem(tempSex);
		sexBox.setBackground(Color.white);
		sexBox.setBounds(winWidth+360, winHeight+20, 165, 25);
		sexBox.addActionListener(e -> {
					sex = (String) sexBox.getSelectedItem();
		});
		add(sexBox);
				
		//year label
		JLabel yearLabel = new JLabel("Birth Year (yy)");
		yearLabel.setBounds(winWidth+215, winHeight+55, 150, 25); 
		add(yearLabel);
								
		//year field
		JTextField yearField = new JTextField(String.valueOf(tempYear));
		//yearField.setText(sex);
		yearField.setBounds(winWidth+360, winHeight+55, 165, 25);
		add(yearField);
		yearField.addKeyListener(new KeyAdapter() {
		        @Override
		        public void keyTyped(KeyEvent e) {
		        	char c = e.getKeyChar();
		            if (!Character.isDigit(c)) { 
		            	e.consume(); // Ignore non-digit characters
		            }
		        }
		});
				
		//month label
		JLabel monthLabel = new JLabel("Birth Month (mm)");
		monthLabel.setBounds(winWidth+215, winHeight+90, 150, 25); 
		add(monthLabel);
								
		//month field
		JTextField monthField = new JTextField(String.valueOf(tempMonth));
		monthField.setBounds(winWidth+360, winHeight+90, 165, 25);
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
		JLabel dayLabel = new JLabel("Birth Day: (dd)");
		dayLabel.setBounds(winWidth+215, winHeight+125, 150, 25); 
		add(dayLabel);
				
		//day field
		JTextField dayField = new JTextField(String.valueOf(tempDay));
		dayField.setBounds(winWidth+360, winHeight+125, 165, 25);
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
				
		//height
		JLabel heightLabel = new JLabel("Height: (cm)");
		heightLabel.setBounds(winWidth+215, winHeight+160, 150, 25);
		add(heightLabel);
				
		JTextField heightField = new JTextField(String.valueOf(tempHeight));
		heightField.setBounds(winWidth+360, winHeight+160, 165, 25);
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
		weightLabel.setBounds(winWidth+215, winHeight+195, 150, 25);
		add(weightLabel);
				
		JTextField weightField = new JTextField(String.valueOf(tempWeight));
		weightField.setBounds(winWidth+360, winHeight+195, 165, 25);
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
					ErrorWindow errorDoB = new ErrorWindow("Invalid Password");
					errorDoB.errorMessage();
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
