package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



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
	
	public static double getNutrientByFoodID(int foodID, int nutrientID) {
	    String sql = """
	        SELECT na.NutrientValue, nn.NutrientDecimals
	        FROM nutrient_amount na
	        JOIN nutrient_name nn ON na.NutrientID = nn.NutrientID
	        WHERE na.FoodID = ? AND na.NutrientID = ?
	    """;

	    try (Connection conn = Dbfetch.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setInt(1, foodID);
	        stmt.setInt(2, nutrientID);

	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            double value = rs.getDouble("NutrientValue");
	            int decimals = rs.getInt("NutrientDecimals");
	            return value * Math.pow(10, -decimals);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    // If not found or error occurs
	    return -1;
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
	
	public static List<Integer> getAllNutrientIDs() {
	    List<Integer> nutrientIDs = new ArrayList<>();
	    String sql = "SELECT NutrientID FROM nutrient_name";

	    try (Connection conn = Dbfetch.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            nutrientIDs.add(rs.getInt("NutrientID"));
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return nutrientIDs;
	}

}
