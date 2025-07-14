package cfg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import app.Dbfetch;

public class CFGDAO {
    private Connection connection = null;
    
    public double[] getAverageMealFG(String username) {
    	double[] FoodGroups = new double[25];
    	try {
    		//Connect to the database
			connection = Dbfetch.getConnection();
	    	int maxMealID = getMaxMealID();
	    	//get total amount for each food group
	    	for (int x=1; x <= maxMealID; x++) {
	    		double[] temp = getMealFG(x, username);
	    		for (int y=0; y < 25; y++) {
	    			FoodGroups[y] += temp[y];
	    		}
	    	}
	    	//get average amount for each food group
	    	for (int y=0; y < 25; y++) {
	    		FoodGroups[y] /= maxMealID;
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
    	
    	return FoodGroups;
    }
    
    public int getMaxMealID() {
    	PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
    	int maxID = 0;
		try {
			// SQL query for reading data
			String searchSQL = "SELECT MAX(meal_id) AS maxID FROM meal_foods";

			//Create Statement and search parameters and execute query
			preparedStatement = connection.prepareStatement(searchSQL);
			resultSet = preparedStatement.executeQuery();

			//get max mealID
            if (resultSet.next()) {
               maxID = resultSet.getInt("maxID");
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
		return maxID;
	}
    
	public double[] getMealFG(int mealID, String username) {
		PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
		double[] foodGroups = new double[25];
		try {
			// SQL query for reading data
			String searchSQL = "SELECT * FROM meal_foods WHERE meal_id = ? AND username = ?";

			//Create Statement and search parameters and execute query
			preparedStatement = connection.prepareStatement(searchSQL);
			preparedStatement.setInt(1, mealID);
			preparedStatement.setString(2, username);
			resultSet = preparedStatement.executeQuery();

			//gets food groups for foods in corresponding mealID 
            while (resultSet.next()) {
            	int food = resultSet.getInt("food_id");
            	//System.out.println("meal " + mealID + ":" + food);
            	double food_amount = resultSet.getDouble("quantity_in_grams");
            	//get food group for corresponding food id
            	int foodGroup = getFoodFG(food);
            	if (foodGroup != 0) {
            		foodGroups[foodGroup-1] += food_amount;
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
		return foodGroups;
	}
	
	public int getFoodFG(int foodID) {
		PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
		int foodGroup = 0;
		try {
			// SQL query for reading data
			String searchSQL = "SELECT * FROM food_name WHERE FoodID = ?";

			//Create Statement and search parameters and execute query
			preparedStatement = connection.prepareStatement(searchSQL);
			preparedStatement.setInt(1, foodID);
			resultSet = preparedStatement.executeQuery();

			//gets food group for corresponding food id
            if (resultSet.next()) {
               foodGroup = resultSet.getInt("FoodGroupID");
               //System.out.println("Food Group: " + foodGroup);
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
		return foodGroup;
	}
}
