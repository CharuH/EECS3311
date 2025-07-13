package dailyNutrition;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DailyNutritionDAO {
	private static String url = "jdbc:mysql://localhost:3306/3311_database"; // Replace with correct DB
    private static String user = "root"; // Replace with correct username
    private static String pass = "adminRomeo"; // Replace with correct password

    private Connection connection = null;

	public HashMap<Integer, Double> getNutritionAverage(LocalDate start, LocalDate end, String username) {
		PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
		//ArrayList<Integer> nutrition = new ArrayList<>();
		HashMap<Integer, Double> nutritionTotal = new HashMap<>();
		long numDays = ChronoUnit.DAYS.between(start, end) + 1;
		try {
			//Connect to the database
			connection = DriverManager.getConnection(url, user, pass);

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
	
	public HashMap<Integer, Double> getNutritionMeal(int mealID, String username) {
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
	
	public HashMap<Integer, Double> getNutritionFood(int foodID, double amount) {
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
			connection = DriverManager.getConnection(url, user, pass);
			for (Map.Entry<Integer, Double> entry : nutritionID.entrySet()) {
		    	   Nutrient nutrient = getNutrient(entry.getKey());
		    	   double nutrientValue = entry.getValue();
		    	   if (nutrient.getUnit().equals("mg")) {
		    		   nutrientValue /= 1000;
		    		   nutritionName.put(nutrient, nutrientValue);
		    	   } else if (nutrient.getUnit().equals("µg")) {
		    		   nutrientValue /= 1000000;
		    		   nutritionName.put(nutrient, nutrientValue);
		    	   } else if (nutrient.getUnit().equals("g")) {
		    		   nutritionName.put(nutrient, nutrientValue);
		    	   } 
		    	   
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
	
	public Nutrient getNutrient(int nutrientID) {
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
	
	public ArrayList<Entry<Nutrient, Double>> getRecommendedNutrition(double maxAge, String sex, double weight, ArrayList<Entry<Nutrient, Double>> topNutrients) {
		PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    String searchSQL;
	    ArrayList<Entry<Nutrient, Double>> recNutrition = new ArrayList<>();
	    HashMap<Nutrient, Double> recMap = new HashMap<>();
		try {
			//Connect to the database
			connection = DriverManager.getConnection(url, user, pass);

			// SQL query for reading data and search parameters
			if (maxAge == 0.57 || maxAge == 0.9 || maxAge == 3 || maxAge == 8) {
				searchSQL = "SELECT * FROM dailyrecommended WHERE ageMax = ?";
				preparedStatement = connection.prepareStatement(searchSQL);
				preparedStatement.setDouble(1, maxAge);
			} else {
				searchSQL = "SELECT * FROM dailyrecommended WHERE ageMax = ? AND sex = ?";
				preparedStatement = connection.prepareStatement(searchSQL);
				preparedStatement.setDouble(1, maxAge);
	            preparedStatement.setString(2, sex);
			}
			
			//Execute query
			resultSet = preparedStatement.executeQuery();

			// Process the result
            if (resultSet.next()) {
            	//go through top 10 nutrients + other
               for (Map.Entry<Nutrient, Double> entry: topNutrients) {
            	   Nutrient nutrient = entry.getKey();
            	   double recommendedAmount = 0;
            	   //check if there is corresponding recommended amount for the nutrientID
            	   switch (entry.getKey().getNutrientID()) {
            	   		case 339:
            	   		case 876:
            	   			recommendedAmount = resultSet.getDouble("VitaminD");
            	   			break;
            	   		case 430:
            	   			recommendedAmount = resultSet.getDouble("VitaminK");
            	   			break;
            	   		case 401:
            	   			recommendedAmount = resultSet.getDouble("VitaminC");
            	    		break;
            	   		case 404:
            	   			recommendedAmount = resultSet.getDouble("Thiamin");
            	   			break;
            	   		case 405:
            	   			recommendedAmount = resultSet.getDouble("Riboflavin");
            	   			break;
            	   		case 406:
        	   				recommendedAmount = resultSet.getDouble("Niacin");
        	   				break;
            	   		case 415:
        	   				recommendedAmount = resultSet.getDouble("VitaminB6");
        	   				break;
            	   		case 806:
            	   		case 815:
        	   				recommendedAmount = resultSet.getDouble("Folate");
        	   				break;
            	   		case 418:
            	   		case 874:
        	   				recommendedAmount = resultSet.getDouble("VitaminB12");
        	   				break;
            	   		case 410:
        	   				recommendedAmount = resultSet.getDouble("Pantothenic_Acid");
        	   				break;
            	   		case 416:
        	   				recommendedAmount = resultSet.getDouble("Biotin");
        	   				break;
            	   		case 862:
        	   				recommendedAmount = resultSet.getDouble("Choline");
        	   				break;
            	   		case 301:
        	   				recommendedAmount = resultSet.getDouble("Calcium");
        	   				break;
            	   		case 312:
        	   				recommendedAmount = resultSet.getDouble("Copper");
        	   				break;
            	   		case 303:
        	   				recommendedAmount = resultSet.getDouble("Iron");
        	   				break;
            	   		case 304:
        	   				recommendedAmount = resultSet.getDouble("Magnesium");
        	   				break;
            	   		case 315:
        	   				recommendedAmount = resultSet.getDouble("Manganese");
        	   				break;
            	   		case 305:
        	   				recommendedAmount = resultSet.getDouble("Phosphorus");
        	   				break;
            	   		case 317:
        	   				recommendedAmount = resultSet.getDouble("Selenium");
        	   				break;
            	   		case 309:
        	   				recommendedAmount = resultSet.getDouble("Zinc");
        	   				break;
            	   		case 306:
        	   				recommendedAmount = resultSet.getDouble("Potassium");
        	   				break;
            	   		case 307:
        	   				recommendedAmount = resultSet.getDouble("Sodium");
        	   				break;
            	   		case 205:
        	   				recommendedAmount = resultSet.getDouble("Carbohydrate");
        	   				break;
            	   		case 203:
        	   				recommendedAmount = resultSet.getDouble("Protein") * weight;
        	   				break;
            	   		case 204:
        	   				recommendedAmount = resultSet.getDouble("Fat");
        	   				break;
            	   		case 825:
        	   				recommendedAmount = resultSet.getDouble("Linoleic_Acid_(n-6)");
        	   				break;
            	   		case 605:
            	   		case 606:
        	   				recommendedAmount = resultSet.getDouble("SatTransFats");
        	   				break;
            	   		case 291:
        	   				recommendedAmount = resultSet.getDouble("Fibre");
        	   				break;
            	   		case 255:
        	   				recommendedAmount = resultSet.getDouble("Water");
        	   				break;
            	   		case 269:
        	   				recommendedAmount = resultSet.getDouble("Sugars");
        	   				break;
            	   		case 601:
        	   				recommendedAmount = resultSet.getDouble("Cholesterol");
        	   				break;
            	   }
            	   recMap.put(nutrient, recommendedAmount);   
               }
            }
            recNutrition = new ArrayList<>(recMap.entrySet());
           

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
		return recNutrition;
	}
}

