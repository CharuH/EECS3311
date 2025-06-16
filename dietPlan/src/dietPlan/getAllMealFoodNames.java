package dietPlan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class getAllMealFoodNames {
	public static List<Food> getAllFoods() {
	    List<Food> foods = new ArrayList<>();
	    String query = "SELECT FoodID, FoodDescription FROM `food name`";
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

}
