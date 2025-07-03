package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class NutritionDAO {
	public FoodNutrition getNutritionByFoodID(int foodID) {
        FoodNutrition foodNutrition = new FoodNutrition(foodID);
        

        String sql = "SELECT nutrient_code, nutrient_value FROM food_nutrition_table WHERE food_id = ?";

        try (Connection conn = Dbfetch.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, foodID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int nutrientCode = rs.getInt("nutrient_code");
                double nutrientValue = rs.getDouble("nutrient_value");
                Integer wrapperCode = nutrientCode;//can get messy
                foodNutrition.setNutrient(wrapperCode, nutrientValue);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return foodNutrition;
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
