package app;

import java.sql.*;
import java.util.*;

public class NutritionCalculator {

	public static Nutrition calculateNutrienceInMeals(List<Meal> meals) {
		
		Nutrition nutrition = new Nutrition();
		
		try (Connection conn = Dbfetch.getConnection()) {
			for (Meal meal : meals) {
				String query = """
					SELECT NutrientID, NutrientValue FROM nutrient_amount
					WHERE FoodID IN
					(
						SELECT food_id FROM meal_foods WHERE meal_id = ?
					)
					""";
				PreparedStatement stmt = conn.prepareStatement(query);
				
				stmt.setInt(1, meal.getID()); // add getter in github
				
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
				    int nutrientID = rs.getInt("NutrientID");
				    double nutrientValue = rs.getDouble("NutrientValue");
				    
				    if (nutrition.getAll().containsKey(nutrientID)) {
					    nutrition.setNutrient(nutrientID, nutrition.getNutrient(nutrientID) + nutrientValue);
				    } else {
					    nutrition.setNutrient(nutrientID, nutrientValue);
	    	
				    }
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nutrition;
	}
}