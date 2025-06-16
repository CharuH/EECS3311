package accCreate;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;


public class Login extends JPanel{
	private int winWidth;
	private int winHeight;
	private String username;
	private String password;
	
	public Login(LoginUI main) {
		winWidth = main.getSize().width / 4;
		winHeight = main.getSize().height / 4;
		
		setLayout(null);
		setBackground(Color.WHITE);
		
		//Title
		JLabel titleLabel = new JLabel("NutriSci");
		titleLabel.setForeground(new Color(42, 215, 23));
		titleLabel.setFont(new Font("Georgia", Font.BOLD, 28));
		titleLabel.setBounds(winWidth+125, winHeight-65, 180, 30);
		add(titleLabel);
		
		//username
		JLabel userLabel = new JLabel("Username");
		userLabel.setBounds(winWidth+55, winHeight+20, 80, 25); //x, y, width, height
		add(userLabel);
		
		JTextField userText = new JTextField();
		userText.setBounds(winWidth+145, winHeight+20, 165, 25);
		add(userText);

		//password
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(winWidth+55, winHeight+55, 80, 25);
		add(passwordLabel);
		
		JPasswordField passwordText = new JPasswordField();
		passwordText.setBounds(winWidth+145, winHeight+55, 165, 25);
		add(passwordText);
		
		//login
		JButton loginButton = new JButton("Login");
		loginButton.setBounds(winWidth+55, winHeight+100, 255, 25);
		loginButton.setBackground(new Color(58, 162, 224));
		loginButton.setContentAreaFilled(false);
		add(loginButton);
		loginButton.addMouseListener(new MouseAdapter() {
            // When the mouse enters the button, change color
            public void mouseEntered(MouseEvent e) {
                loginButton.setContentAreaFilled(true);
            }
            
            // When the mouse exits the button, reset the color
            public void mouseExited(MouseEvent e) {
                loginButton.setContentAreaFilled(false); 
            }
        });
		loginButton.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
				username = userText.getText();
				password = new String(passwordText.getPassword());
				UserDataRW read = new UserDataRW();
				if (read.searchUser(username, password)) {
					UserData data = read.getUser(username, password);
					main.profile1(username, password, data.getUnits());
					main.profile2(data.getSex(), data.getDob(), data.getWeight(), data.getHeight());
					main.login();
				} else {
					ErrorWindow error = new ErrorWindow("Incorrect Username or Password");
					error.errorMessage();
				}
			}
		});
		
		//new account
		JButton newAccountButton = new JButton("New Account");
		newAccountButton.setBounds(winWidth+55, winHeight+150, 255, 25);
		newAccountButton.setForeground(Color.BLUE);
		newAccountButton.setContentAreaFilled(false);
		newAccountButton.setBorderPainted(false);
		add(newAccountButton);
		newAccountButton.addActionListener(e -> main.switchToScreen("createProfile1"));
	}
}
