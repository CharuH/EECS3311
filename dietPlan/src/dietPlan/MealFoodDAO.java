package dietPlan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MealFoodDAO {
	
	    public static MealFood getMealFood(int id, double qty) {
	    	String query = "SELECT * FROM `food name` WHERE FoodID = ?";
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
	    
	    public static String getFoodNameById(int id) {
	        String query = "SELECT FoodDescription FROM `food name` WHERE FoodID = ?";
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

		
	    
	    
	    
	    
	    
	    
	

}


