package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MealSwapSearch {
    /**
     * Finds substitute meals based on nutritional adjustment.
     * 
     * @param originalMeal the current meal
     * @param nutrientId the nutrient to adjust (e.g., protein)
     * @param multiplier the amount to increase or decrease (e.g., 1.1 for +10%)
     * @return a list of suggested meals with similar composition but adjusted nutrition
     * 
     * 
     * 
     */
	
	
	public static List<MealFood> findSimilarFoods(int foodID, double quantity) {
	    List<MealFood> similarFoods = new ArrayList<>();

	    int foodGroupID = MealFoodDAO.getFoodGroupID(foodID);
	    if (foodGroupID == -1) {
	        return similarFoods;
	    }

	    String sql = "SELECT FoodID FROM food_name WHERE FoodGroupID = ? AND FoodID != ? LIMIT 30";

	    try (Connection conn = Dbfetch.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setInt(1, foodGroupID);
	        stmt.setInt(2, foodID);

	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            int similarFoodID = rs.getInt("FoodID");
	            
	            
	            
	            MealFood mf = MealFoodDAO.getMealFood(similarFoodID, quantity);
	            

	            similarFoods.add(mf);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace(); 
	    }

	    return similarFoods;
	}
    
    public List<Meal> suggestSubstitutes(Meal originalMeal, int nutrientId, double multiplier) {
        Nutrition originalNutrition = originalMeal.getNutrition();
        double targetAmount = originalNutrition.getNutrient(nutrientId) * multiplier;

       
        List<Meal> candidates = new ArrayList<>();
        int c=0;
        
        // Try replacing one item at a time and calcing  nutrition
        for (int i = 0; i < originalMeal.getFoods().size(); i++) {
            Food originalFood = originalMeal.getFoods().get(i);
            double originalQty = originalMeal.getQuantities().get(i);

            List<MealFood> substitutes = findSimilarFoods(originalFood.foodId, originalQty);
            for (MealFood substitute : substitutes) {
            	
                Meal modified = originalMeal.copy(); 
                modified.replaceFoodAt(i, substitute, originalQty); // Swap food, same qty
                Nutrition modifiedNutrition = modified.getNutrition();
                if (modifiedNutrition == null) continue;
                
                double newAmount = modifiedNutrition.getNutrient(nutrientId);
                double diff = Math.abs(newAmount - targetAmount);

                
                if (newAmount >= targetAmount * 0.9 && newAmount <= targetAmount * 1.1) {
                	candidates.add(modified);
                	System.out.println("added!");
                	
                }
                System.out.println("Trying substitute: " + substitute.getName());
            	System.out.println("Target: " + targetAmount + " Actual: " + newAmount);
            	System.out.println(c);
            	c++;
            }
        }

        // Step 3: Return sorted by closest match
        System.out.println("Ranking Candidates: /n");
        candidates.sort(Comparator.comparingDouble(
            m -> Math.abs(m.getNutrition().getNutrient(nutrientId) - targetAmount)
        ));
        System.out.println("Returning candidates");
        return candidates;
    }
}