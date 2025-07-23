package accCreate;

import java.awt.BorderLayout;
import java.awt.Component;
import java.time.LocalDate;

import javax.swing.JFrame;
import javax.swing.JPanel;

import app.Log;
import app.MealSwapViewerPanel;
import cfg.CFG;
import dailyNutrition.DailyNutrition;

public class MainUI extends JFrame {
	
	private Component currentPanel;
	private SidePanel sidePanel;
	private Settings settings;
	private ProfileM profileM;
	private ProfileI profileI;
	private UserData currentUser;
	private MealSwapViewerPanel mealSwapViewer;
	private CFG cfg;
	private Log log;
	private DailyNutrition nutrition;

	
	public MainUI(UserData currentUser) {
		setTitle("Nutrition Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout());
        this.currentUser = currentUser;
        
        log = new Log(this);
	currentPanel = log;
        add(log, BorderLayout.EAST);

        sidePanel = new SidePanel(this);
        add(sidePanel, BorderLayout.WEST);
        
	}
	
	// Method to switch screens
    public void switchToScreen(String screenName) {
    	remove(currentPanel);
        if (screenName.equals("settings")) {
        	settings = new Settings(this);
        	currentPanel = settings;
        	add(settings, BorderLayout.EAST);
        } else if (screenName.equals("profileM")) {
        	profileM = new ProfileM(this);
        	currentPanel = profileM;
        	add(profileM, BorderLayout.EAST);
        } else if (screenName.equals("profileI")) {
        	profileI = new ProfileI(this);
        	currentPanel = profileI;
        	add(profileI, BorderLayout.EAST);
        } else if (screenName.equals("log")) {
        	log = new Log(this);
        	currentPanel = log;
        	add(log, BorderLayout.EAST);
        } else if (screenName.equals("cfg")) {
        	cfg = new CFG(this);
        	currentPanel = cfg;
        	add(cfg, BorderLayout.EAST);
        }  else if (screenName.equals("nutrition")) {
        	nutrition = new DailyNutrition(this);
        	currentPanel = nutrition;
        	add(nutrition, BorderLayout.EAST);
        }  
        revalidate();
        repaint();
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
}
