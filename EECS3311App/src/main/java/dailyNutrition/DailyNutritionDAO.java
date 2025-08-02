package dailyNutrition;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import app.Dbfetch;

public class DailyNutritionDAO {
    private Connection connection = null;

	public HashMap<Integer, Double> getNutritionAverage(LocalDate start, LocalDate end, String username) {
		PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
		//ArrayList<Integer> nutrition = new ArrayList<>();
		HashMap<Integer, Double> nutritionTotal = new HashMap<>();
		long numDays = ChronoUnit.DAYS.between(start, end) + 1;
		try {
			//Connect to the database
			connection = Dbfetch.getConnection();

			// SQL query for reading data
			String searchSQL = "SELECT * FROM meals WHERE meal_date BETWEEN ? AND ? AND username = ?";

			//Create Statement and search parameters and execute query
			preparedStatement = connection.prepareStatement(searchSQL);
			preparedStatement.setDate(1, Date.valueOf(start));
			preparedStatement.setDate(2, Date.valueOf(end));
			preparedStatement.setString(3, username);
			resultSet = preparedStatement.executeQuery();

			// Process the result
            while (resultSet.next()) {
            	//get meal id
               int mealID = resultSet.getInt("id");
               //get nutrient name, units, amount in a meal
               HashMap<Integer, Double> nutritionMeal = getNutritionMeal(mealID, username);
               //add and store nutrients of meal into total
               for (Map.Entry<Integer, Double> entry : nutritionMeal.entrySet()) {
           	    	Integer nutrientID  = entry.getKey();
           	    	Double nutrientValue = entry.getValue();
           	    	nutritionTotal.put(nutrientID, nutritionTotal.getOrDefault(nutrientID, 0.0) + nutrientValue);
               }
            }
            //get average for the number of days
            for (Map.Entry<Integer, Double> entry : nutritionTotal.entrySet()) {
       	    	Integer nutrientID  = entry.getKey();
       	    	Double nutrientValue = entry.getValue();
       	    	nutritionTotal.put(nutrientID, nutrientValue / numDays);
           }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (preparedStatement != null) preparedStatement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//return meals;
		return nutritionTotal;
	}
	
	private HashMap<Integer, Double> getNutritionMeal(int mealID, String username) {
		PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
		HashMap<Integer, Double> nutritionMeal = new HashMap<>();
		try {
			// SQL query for reading data
			String searchSQL = "SELECT * FROM meal_foods WHERE meal_id = ? AND username = ?";

			//Create Statement and search parameters and execute query
			preparedStatement = connection.prepareStatement(searchSQL);
			preparedStatement.setInt(1, mealID);
			preparedStatement.setString(2, username);
			resultSet = preparedStatement.executeQuery();

			// Process the result
            while (resultSet.next()) {
               //get food id and quantity
               int foodID = resultSet.getInt("food_id");
               double amount = resultSet.getDouble("quantity_in_grams");
               //get nutrient name, unit, amount for a food
               HashMap<Integer, Double> nutritionFood = getNutritionFood(foodID, amount);
               //adds and stores nutrient for each food in the meal
               for (Map.Entry<Integer, Double> entry : nutritionFood.entrySet()) {
            	    Integer nutrientID  = entry.getKey();
            	    Double nutrientValue = entry.getValue();
            	    nutritionMeal.put(nutrientID, nutritionMeal.getOrDefault(nutrientID, 0.0) + nutrientValue);
               }
               
            }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (preparedStatement != null) preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return nutritionMeal;
	}
	
	private HashMap<Integer, Double> getNutritionFood(int foodID, double amount) {
		PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
		HashMap<Integer, Double> nutritionFood = new HashMap<>();
		try {
			// SQL query for reading data
			String searchSQL = "SELECT * FROM nutrient_amount WHERE foodID = ?";

			//Create Statement and search parameters and execute query
			preparedStatement = connection.prepareStatement(searchSQL);
			preparedStatement.setInt(1, foodID);
			resultSet = preparedStatement.executeQuery();

			// Process the result
            while (resultSet.next()) {
            	//get nutrient id and value
            	int nutrientID = resultSet.getInt("NutrientID");
            	double nutrientValue = resultSet.getDouble("NutrientValue");
            	//get actual nutrient value based on food amount
            	nutrientValue*= (amount / 100.0);
            	//stores nutrient for the food
            	nutritionFood.put(nutrientID, nutrientValue);
            }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (preparedStatement != null) preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return nutritionFood;
	}
	
	public HashMap<Nutrient, Double> convertUnitsName(HashMap<Integer, Double> nutritionID) {
		HashMap<Nutrient, Double> nutritionName = new HashMap<>();
		try {
			//Connect to the database
			connection = Dbfetch.getConnection();
			for (Map.Entry<Integer, Double> entry : nutritionID.entrySet()) {
		    	   Nutrient nutrient = getNutrient(entry.getKey());
		    	   double nutrientValue = entry.getValue();
		    	   nutrientValue = convertUnits(nutrientValue, nutrient.getUnit());
		    	   nutritionName.put(nutrient, nutrientValue);
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return nutritionName;
	}
	private double convertUnits(double value, String unit) {
		double nutrientValue = value;
		if (unit.equals("mg")) {
 		   nutrientValue /= 1000;
 	   	} else if (unit.equals("µg")) {
 		   nutrientValue /= 1000000;
 	   	} else if (!(unit.equals("g"))) {
 		   nutrientValue = 0;
 	   	} 
		return nutrientValue;

	}
	private Nutrient getNutrient(int nutrientID) {
		PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
		Nutrient nutrient = new Nutrient(nutrientID, null, null);
		try {
			// SQL query for reading data
			String searchSQL = "SELECT * FROM nutrient_name WHERE NutrientID = ?";

			//Create Statement and search parameters and execute query
			preparedStatement = connection.prepareStatement(searchSQL);
			preparedStatement.setInt(1, nutrientID);
			resultSet = preparedStatement.executeQuery();

			// Process the result
            if (resultSet.next()) {
            	//get nutrient name and unit
            	nutrient.setNutrient(resultSet.getString("NutrientName"));
            	nutrient.setUnit(resultSet.getString("NutrientUnit"));
            }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (preparedStatement != null) preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return nutrient;
	}
}