package app;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.Map.Entry;

public class NutritionCalculator {

	public static Nutrition calculateNutrienceInMeals(List<Meal> meals) {
		
		Nutrition nutrition = new Nutrition();
		
		try (Connection conn = Dbfetch.getConnection()) {
			for (Meal meal : meals) {
				String foodQuery = """
					SELECT food_id, quantity_in_grams
					FROM meal_foods
					WHERE meal_id = ?
					""";
				PreparedStatement foodStmt = conn.prepareStatement(foodQuery);
				
				foodStmt.setInt(1, meal.getID());
				
				ResultSet foodRS = foodStmt.executeQuery();
				while (foodRS.next()) {
				    
					int foodID = foodRS.getInt("food_id");
					double portion = foodRS.getDouble("quantity_in_grams");
					
					String nutrientQuery = """
							SELECT NutrientID, NutrientValue
							FROM nutrient_amount
							WHERE FoodID = ?
							""";
					PreparedStatement nutrientStmt = conn.prepareStatement(nutrientQuery);
					nutrientStmt.setInt(1, foodID);
					ResultSet nutrientRS = nutrientStmt.executeQuery();
					
					while (nutrientRS.next()) {
						int nutrientID = nutrientRS.getInt("NutrientID");
						double nutrientValue = nutrientRS.getDouble("nutrientValue");
						
						double actualValue = nutrientValue * (double) (portion / 100.0);
						
						nutrition.setNutrient(nutrientID, nutrition.getNutrient(nutrientID) + actualValue);
					}
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nutrition;
	}
	
	public static Nutrition calculateChangeCumulative(Nutrition old_meals_nutrition, Nutrition new_meals_nutrition) {		
		Nutrition cumulative = new Nutrition();
		for (Map.Entry<Integer, Double> entry : new_meals_nutrition.getAll().entrySet()) {
			int nutrientID = entry.getKey();
			double newValue = entry.getValue();
			double oldValue = old_meals_nutrition.getNutrient(nutrientID);
			cumulative.setNutrient(nutrientID, newValue-oldValue);
		}
			
		return cumulative;
	}


	public static Nutrition calculateChangeAverage(Nutrition cumulative, LocalDate start, LocalDate end) {
		
		int numMeals = 0;//Getting the number of meals
		try(Connection conn = Dbfetch.getConnection()) {
			String getNumMealsQuery = "SELECT COUNT(*) FROM meals WHERE meal_date BETWEEN ? AND ?";
			
			PreparedStatement stmt = conn.prepareStatement(getNumMealsQuery);
			stmt.setDate(1, Date.valueOf(start));
			stmt.setDate(2, Date.valueOf(end));
	
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				numMeals += rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Nutrition average = new Nutrition();
		for (Map.Entry<Integer, Double> entry : cumulative.getAll().entrySet()) {
			int nutrientID = entry.getKey();
			double delta = entry.getValue() / numMeals;
			average.setNutrient(nutrientID, delta);
		}
			
		return average;
	}
	
	public static Map<Integer, Nutrition> calculateNutritionPerMeal(List<Meal> meals) {
		Map<Integer, Nutrition> perMeal = new HashMap<Integer, Nutrition>();
		String query = """
				SELECT nutrient_amount.NutrientID, SUM(nutrient_amount.NutrientValue) 
				FROM meal_foods
				JOIN nutrient_amount ON meal_foods.food_id = nutrient_amount.FoodID
				WHERE meal_foods.meal_id = ?
				GROUP BY nutrient_amount.NutrientID
				""";
		
		try (Connection conn = Dbfetch.getConnection()) {	
			PreparedStatement stmt = conn.prepareStatement(query);
			
			for (Meal meal : meals) {
				stmt.setInt(1, meal.getID());
				
				ResultSet rs = stmt.executeQuery();
				Nutrition nutrition = new Nutrition();
				while (rs.next()) {
					int nutrientID = rs.getInt("NutrientID");
					double value = rs.getDouble("SUM(nutrient_amount.NutrientValue)");
					nutrition.setNutrient(nutrientID, value);
				}
				perMeal.put(meal.getID(), nutrition);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return perMeal;
	}
	
	
	public static Map<Integer, Nutrition> calculateChangePerMeal(Map<Integer, Nutrition> old_nutrition_per_plate, Map<Integer, Nutrition> new_nutrition_per_plate) {
		Map<Integer, Nutrition> perMeal = new HashMap<Integer, Nutrition>();

		for (Entry<Integer, Nutrition> nutrition : old_nutrition_per_plate.entrySet()) {
			Integer mealID = nutrition.getKey();
			Nutrition change = new Nutrition();
			
			for (Entry<Integer, Double> nut : nutrition.getValue().getAll().entrySet()) {
				Integer nutrientID = nut.getKey();
				Double oldNutrientValue = nut.getValue();
				
				Nutrition newNutrition = new_nutrition_per_plate.get(mealID);
				Double newNutrientValue = newNutrition.getNutrient(nutrientID);
				
				change.setNutrient(nutrientID, (newNutrientValue - oldNutrientValue));
			}
			perMeal.put(mealID, change);
		}
		return perMeal;
	}
	
}














