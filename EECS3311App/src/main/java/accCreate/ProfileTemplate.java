package accCreate;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public abstract class ProfileTemplate extends JPanel {
	protected int winWidth;
	protected int winHeight;
	
	protected UserData currentUser;
	protected String tempPassword;
	protected String tempSex;
	protected int tempYear;
	protected int tempMonth;
	protected int tempDay;
	
	protected String username;
	protected String password;
	protected String measurement;
	protected String sex;
	protected int year;
	protected int month;
	protected int day;
	protected LocalDate dob;
	
	protected JPasswordField passwordText;
	protected JTextField yearField;
	protected JTextField monthField;
	protected JTextField dayField;

	protected boolean error = false;
	
	
	public void profileP1(MainUI main) {
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
		
		passwordText = new JPasswordField(tempPassword);
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
		yearField = new JTextField(String.valueOf(tempYear));
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
		monthField = new JTextField(String.valueOf(tempMonth));
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
		dayField = new JTextField(String.valueOf(tempDay));
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
	}
	
	public abstract void profileP2(MainUI main);
}
