package app;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import com.mysql.cj.exceptions.DataConversionException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import accCreate.MainUI;
import accCreate.UserData;

public class AcrossTimeMealSubstitution extends JPanel {
	private MainUI mainUI;
	private JPanel nutrientPanel = new JPanel();
	private Nutrition cumulative = new Nutrition();
	private Nutrition average = new Nutrition();
	private Map<Integer, Nutrition> perMeal = new HashMap<Integer, Nutrition>();//Integer: MealID, Nutrition: nutrition of meal
	
	
	//USE CASE 5
	public AcrossTimeMealSubstitution(MainUI mainUI) {
        this.mainUI = mainUI;
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Across-Time Meal Substitution", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));


        JLabel oldFoodLabel = new JLabel("Food you want to replace:");
        JTextField oldFoodField = new JTextField();
        
        JLabel newFoodLabel = new JLabel("Food you want to replace it with:");
        JTextField newFoodField = new JTextField();
        
        oldFoodField.setMaximumSize(new Dimension(Integer.MAX_VALUE, oldFoodField.getPreferredSize().height));
        newFoodField.setMaximumSize(new Dimension(Integer.MAX_VALUE, newFoodField.getPreferredSize().height));
        
        JLabel startDateLabel = new JLabel("Start Date (YYYY-MM-DD):");
        JTextField startDateField = new JTextField();

        JLabel endDateLabel = new JLabel("End Date (YYYY-MM-DD):");
        JTextField endDateField = new JTextField();

        startDateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, startDateField.getPreferredSize().height));
        endDateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, endDateField.getPreferredSize().height));


        formPanel.add(oldFoodLabel);
        formPanel.add(oldFoodField);
        formPanel.add(newFoodLabel);
        formPanel.add(newFoodField);
        formPanel.add(new JLabel());
        formPanel.add(startDateLabel);
        formPanel.add(startDateField);
        formPanel.add(endDateLabel);
        formPanel.add(endDateField);
        
        
        JButton cumulativeButton = new JButton("Cumulative");
    	cumulativeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    	formPanel.add(cumulativeButton);
    	cumulativeButton.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
        		displayCumulative(nutrientPanel);
        	}
        });
    	cumulativeButton.setEnabled(false);
    	
    	JButton averageButton = new JButton("Average");
    	averageButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    	formPanel.add(averageButton);
    	averageButton.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
        		displayAverage(nutrientPanel);
        	}
        });
    	averageButton.setEnabled(false);
    	
    	//PER MEAL
    	JLabel mealPickerLabel = new JLabel("Select a Meal ID to see its Nutrition:");
    	mealPickerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    	JComboBox<Integer> mealIdComboBox = new JComboBox<>();
    	mealIdComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, mealIdComboBox.getPreferredSize().height));
    	mealIdComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
    	JButton displayPerMealButton = new JButton("Show Nutrition for Meal");
    	displayPerMealButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    	displayPerMealButton.setEnabled(false);  // Enable after data is loaded
    	formPanel.add(mealPickerLabel);
    	formPanel.add(mealIdComboBox);
    	formPanel.add(displayPerMealButton);
    	displayPerMealButton.addActionListener(new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	        Integer mealId = (Integer) mealIdComboBox.getSelectedItem();
    	        if (mealId  != null) {
    	            nutrientPanel.removeAll();
    	            Nutrition nutrition = perMeal.get(mealId);
    	            nutrientPanel.add(new JLabel("Nutrition for Meal ID: " + mealId));
    	            
    	            String nutrientName = "";
    	    	    for (Map.Entry<Integer, Double> entry : nutrition.getAll().entrySet()) {
    	    	    	try (Connection conn = Dbfetch.getConnection()) {
    	    	    		String query = "SELECT NutrientName FROM nutrient_name WHERE NutrientID = ?";
    	    	    		PreparedStatement stmt = conn.prepareStatement(query);
    	    	    		stmt.setInt(1, entry.getKey());
    	    	    		
    	    	    		ResultSet rs = stmt.executeQuery();
    	    	    		if (rs.next()) {
    	    	    			nutrientName = rs.getString(1);
    	    	    		}
    	    	    	} catch (SQLException SQLe) {
    	    	    		SQLe.printStackTrace();
    	    	    	}
    	    	        JLabel label = new JLabel(nutrientName + ": " + entry.getValue());
    	    	        nutrientPanel.add(label);
    	    	    }
    	    	    
    	            nutrientPanel.revalidate();
    	            nutrientPanel.repaint();
    	        }
    	    }
    	});
        
    	//SUBMIT BUTTON
    	JButton submitButton = new JButton("Submit");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(new ActionListener() {
            @SuppressWarnings("deprecation")
			@Override
            public void actionPerformed(ActionEvent e) {
            	try {
            		String oldFood = oldFoodField.getText().trim();
                	String newFood = newFoodField.getText().trim();
                	String startDateString = startDateField.getText().trim();
                	String endDateString = endDateField.getText().trim();
                	LocalDate startDate; LocalDate endDate;

                	try {
                		startDate = LocalDate.parse(startDateString);
                		endDate = LocalDate.parse(endDateString);
                	} catch (IllegalArgumentException  d) {
                		JOptionPane.showMessageDialog(AcrossTimeMealSubstitution.this, "Make sure the dates are in the right form");
                		return;
                	}
                
                	
                	if (oldFood.isEmpty() || newFood.isEmpty() || startDateString.isEmpty() || endDateString.isEmpty()) {
                        JOptionPane.showMessageDialog(AcrossTimeMealSubstitution.this, "Please fill the form fully");
                        return;
                	}
                	
                	
                	Integer oldFoodID = null;
                	Integer newFoodID = null;
                	try (Connection conn = Dbfetch.getConnection()){
                		String query = """
                				SELECT FoodID from food_name
                				WHERE TRIM(LOWER(FoodDescription)) = TRIM(LOWER(?))
                				""";
                		
                		PreparedStatement oldStmt = conn.prepareStatement(query);
                		oldStmt.setString(1, oldFood);
                		ResultSet rsOLD = oldStmt.executeQuery();
                		if (rsOLD.next()) {
                			oldFoodID = rsOLD.getInt(1);
                		}
                		
                		PreparedStatement newStmt = conn.prepareStatement(query);
                		newStmt.setString(1, newFood);
                		ResultSet rsNEW = newStmt.executeQuery();
                		if (rsNEW.next()) {
                			newFoodID = rsNEW.getInt(1);
                		}

                	} catch (SQLException sqlE) {
                		sqlE.printStackTrace();
                	}
                	
                	if (oldFoodID == null || newFoodID == null) {
                		JOptionPane.showMessageDialog(AcrossTimeMealSubstitution.this, "At lease one of the food items do not exist");
                        return;
                	}

                	String username = UserData.getInstance().getUsername();
                	AcrossTimeMealSubstitutionMethod(newFoodID, oldFoodID, startDate, endDate, username);
                	
                	cumulativeButton.setEnabled(true);
                	averageButton.setEnabled(true);
                	
                	mealIdComboBox.removeAllItems();
                	for (Integer mealId : perMeal.keySet()) {
                	    mealIdComboBox.addItem(mealId);
                	}
                	displayPerMealButton.setEnabled(true);
                	
                	
            	} catch (Exception ex) {
            		ex.printStackTrace();
            		 JOptionPane.showMessageDialog(AcrossTimeMealSubstitution.this, "An unexpected error occurred:\n" + ex.getMessage());
            	}
            }
        });
        
        formPanel.add(submitButton);
        
        nutrientPanel.setLayout(new BoxLayout(nutrientPanel, BoxLayout.Y_AXIS));
        contentPanel.add(formPanel);
        
        JScrollPane scrollPane = new JScrollPane(nutrientPanel);
        scrollPane.setPreferredSize(new Dimension(300, 200));
        contentPanel.add(scrollPane);
        nutrientPanel.setLayout(new BoxLayout(nutrientPanel, BoxLayout.Y_AXIS));

        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(contentPanel, BorderLayout.CENTER);
	}
	
	public void AcrossTimeMealSubstitutionMethod(int foodID_new, int foodID_old, LocalDate start, LocalDate end, String username) {
		System.out.println(username);
		
		List<Meal> old_meals = MealDAO.getMealsByDates(start, end, username);
		Nutrition old_meals_nutrition = NutritionCalculator.calculateNutrienceInMeals(old_meals);
		Map<Integer, Nutrition> old_nutrition_per_plate = NutritionCalculator.calculateNutritionPerMeal(old_meals);
		
		MealDAO.swapMeals(foodID_old, foodID_new, start, end, username);
		
		List<Meal> new_meals = MealDAO.getMealsByDates(start, end, username);
		Nutrition new_meals_nutrition = NutritionCalculator.calculateNutrienceInMeals(new_meals);
		Map<Integer, Nutrition> new_nutrition_per_plate = NutritionCalculator.calculateNutritionPerMeal(new_meals);
		
		this.cumulative = NutritionCalculator.calculateChangeCumulative(old_meals_nutrition, new_meals_nutrition);
		this.average = NutritionCalculator.calculateChangeAverage(cumulative, start, end);
		this.perMeal = NutritionCalculator.calculateChangePerMeal(old_nutrition_per_plate, new_nutrition_per_plate);
	}
	
	public Nutrition getCumulative() {
		return this.cumulative;
	}
	
	public Nutrition getAverage() {
		return this.average;
	}
	
	public Map<Integer, Nutrition> getPerMeal() {
		return this.perMeal;
	}
	
	private void displayCumulative(JPanel contentPanel) {
	    nutrientPanel.removeAll();  // Clear previous labels

	    Map<Integer, Double> cumulativeNutrients = this.getCumulative().getAll();
	    nutrientPanel.add(new JLabel("Cumulative:"));
	    
	    String nutrientName = "";
	    for (Map.Entry<Integer, Double> entry : cumulativeNutrients.entrySet()) {
	    	try (Connection conn = Dbfetch.getConnection()) {
	    		String query = "SELECT NutrientName FROM nutrient_name WHERE NutrientID = ?";
	    		PreparedStatement stmt = conn.prepareStatement(query);
	    		stmt.setInt(1, entry.getKey());
	    		
	    		ResultSet rs = stmt.executeQuery();
	    		if (rs.next()) {
	    			nutrientName = rs.getString(1);
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
	    	}
	        JLabel label = new JLabel(nutrientName + ": " + entry.getValue());
	        nutrientPanel.add(label);
	    }

	    nutrientPanel.revalidate();
	    nutrientPanel.repaint();
	}

	
	private void displayAverage(JPanel contentPanel) {
	    nutrientPanel.removeAll();  // Clear previous labels
	    
	    Map<Integer, Double> averageNutrients = this.getAverage().getAll();
	    nutrientPanel.add(new JLabel("Average:"));
	    
	    String nutrientName = "";
	    for (Map.Entry<Integer, Double> entry : averageNutrients.entrySet()) {
	    	try (Connection conn = Dbfetch.getConnection()) {
	    		String query = "SELECT NutrientName FROM nutrient_name WHERE NutrientID = ?";
	    		PreparedStatement stmt = conn.prepareStatement(query);
	    		stmt.setInt(1, entry.getKey());
	    		
	    		ResultSet rs = stmt.executeQuery();
	    		if (rs.next()) {
	    			nutrientName = rs.getString(1);
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
	    	}
	        JLabel label = new JLabel(nutrientName + ": " + entry.getValue());
	        nutrientPanel.add(label);
	    }

	    nutrientPanel.revalidate();
	    nutrientPanel.repaint();
	}

}
