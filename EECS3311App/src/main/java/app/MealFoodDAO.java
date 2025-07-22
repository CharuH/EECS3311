package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MealFoodDAO {
		
	    public static MealFood getMealFood(int id, double qty) {
	    	String query = "SELECT * FROM `food_name` WHERE FoodID = ?";
	        try (Connection conn = Dbfetch.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(query)) {

	            stmt.setInt(1, id);
	            ResultSet rs = stmt.executeQuery();

	            if (rs.next()) {
	                String name = rs.getString("FoodDescription");
	                
	                return new MealFood(id, qty,name);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	    //returns string name of Mealfood given ID
	    public static String getFoodNameById(int id) {
	        String query = "SELECT FoodDescription FROM `food_name` WHERE FoodID = ?";
	        try (Connection conn = Dbfetch.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(query)) {

	            stmt.setInt(1, id);
	            ResultSet rs = stmt.executeQuery();

	            if (rs.next()) {
	                return rs.getString("FoodDescription");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return null; 
	    }
	    
	    public static List<Food> getAllFoods() {
		    List<Food> foods = new ArrayList<>();
		    String query = "SELECT FoodID, FoodDescription FROM `food_name`";
		    try (Connection conn = Dbfetch.getConnection();
		         PreparedStatement stmt = conn.prepareStatement(query);
		         ResultSet rs = stmt.executeQuery()) {

		        while (rs.next()) {
		            foods.add(new Food(rs.getInt("FoodID"), rs.getString("FoodDescription")));
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return foods;
		}
	    
	    public static int getFoodGroupID(int foodID) {
	        String sql = "SELECT FoodGroupID FROM food_name WHERE FoodID = ?";
	        
	        try (Connection conn = Dbfetch.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setInt(1, foodID);
	            ResultSet rs = stmt.executeQuery();
	            
	            if (rs.next()) {
	                return rs.getInt("FoodGroupID");
	            }
	            
	        } catch (SQLException e) {
	            e.printStackTrace(); 
	        }

	        return -1;  
	    }
	    
	    
	    
	    
	    
	    
	    
	    
	    
		

		
	    
	    
	    
	    
	    
	    
	

}


