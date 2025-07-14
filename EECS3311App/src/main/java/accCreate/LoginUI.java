package accCreate;

import java.awt.CardLayout;
import java.time.LocalDate;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class LoginUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel cards;
    private UserData currentUser = UserData.getInstance();
    private Login login;
    private ProfileCreation1 createProfile1;
    private ProfileCreation2M createProfile2M;
    private ProfileCreation2I createProfile2I;
    
    public LoginUI() {
        setTitle("Nutrition Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        
        // Create instances of the screens and add them to the card layout
        login = new Login(this); // allow screen switching
        createProfile1 = new ProfileCreation1(this);
        createProfile2M = new ProfileCreation2M(this);
        createProfile2I = new ProfileCreation2I(this);
        
        // Add screens to the card panel
        cards.add(login, "login");
        cards.add(createProfile1, "createProfile1");
        cards.add(createProfile2M, "createProfile2M");
        cards.add(createProfile2I, "createProfile2I");
       
        // Add card panel to the main frame
        add(cards);
        
    }

    // Method to switch screens
    public void switchToScreen(String screenName) {
        cardLayout.show(cards, screenName); 
    }
    
    public void profile1(String username, String password, String units) {
    	currentUser.setUsername(username);
    	currentUser.setPassword(password);
    	currentUser.setUnits(units);
    }
    
    public void profile2(String sex, LocalDate dob, double weight, double height) {
    	currentUser.setSex(sex);
    	currentUser.setDob(dob);
    	currentUser.setWeight(weight);
    	currentUser.setHeight(height);
    }
    
    public UserData getUser() {
    	return currentUser;
    }
    
    public void login() {
    	dispose();
    	new MainUI(currentUser).setVisible(true);
    	
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }
}