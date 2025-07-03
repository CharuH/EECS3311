package dietPlan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import app.FoodNutrition;

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
}
