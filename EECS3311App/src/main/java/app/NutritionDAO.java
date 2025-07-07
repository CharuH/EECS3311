package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class NutritionDAO {
	public static FoodNutrition getNutritionByFoodID(int foodID) {
	    FoodNutrition foodNutrition = new FoodNutrition(foodID);

	    String sql = """
	    		SELECT nutrient_amount.NutrientID, nutrient_amount.NutrientValue, nutrient_name.NutrientDecimals
	    		FROM nutrient_amount
	    		JOIN nutrient_name ON nutrient_amount.NutrientID = nutrient_name.NutrientID
	    		WHERE nutrient_amount.foodid = ?

	    """;

	    try (Connection conn = Dbfetch.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setInt(1, foodID);
	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            int nutrientCode = rs.getInt("NutrientID");
	            double nutrientValue = rs.getDouble("NutrientValue");
	            int nutrientDecimal = rs.getInt("NutrientDecimals");

	            double scaledValue = nutrientValue * Math.pow(10, -nutrientDecimal);

	            foodNutrition.setNutrient(nutrientCode, scaledValue);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return foodNutrition;
	}
	
	public static String getNutrientNameByID(int nutrientID) {
        String nutrientName = null;

        String sql = "SELECT NutrientName FROM nutrient_name WHERE NutrientID = ?";

        try (Connection conn = Dbfetch.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nutrientID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nutrientName = rs.getString("NutrientName");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nutrientName != null ? nutrientName : "Unknown Nutrient";
    }
	
	public static int getKcalbyFoodID(int id) {
		String query = "SELECT `Nutrient_Value` FROM `nutrients` WHERE Food_id = ? AND Nutrient_code = 208";
        try (Connection conn = Dbfetch.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return (int) rs.getInt("Nutrient_Value"); 
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; 
    }

}
