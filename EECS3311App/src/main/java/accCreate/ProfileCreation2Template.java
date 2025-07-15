package accCreate;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class ProfileCreation2Template extends JPanel {
	protected int winWidth;
	protected int winHeight;
	
	protected String sex = "Male";
	protected int year;
	protected int month;
	protected int day;
	protected LocalDate dob;
	
	protected JTextField yearField;
	protected JTextField monthField;
	protected JTextField dayField;
	
	protected boolean error = false;
	
	
	public void profileP1(LoginUI main) {
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
		yearField = new JTextField(4);
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
		monthField = new JTextField(2);
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
		dayField = new JTextField(2);
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
	}
	
	public abstract void profileP2(LoginUI main);
}
