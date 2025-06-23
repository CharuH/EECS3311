package dietPlan;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.sql.Date;


public class MealDAO {

    // Save a meal, return ID
    public static int saveMeal(Meal meal) {
        String insertMealSQL = "INSERT INTO meals (meal_date, meal_type) VALUES (?, ?)";
        try (Connection conn = Dbfetch.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertMealSQL, Statement.RETURN_GENERATED_KEYS)) {

        	stmt.setDate(1, Date.valueOf(meal.getDate()));
            stmt.setString(2, meal.getType().name());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                int mealId = keys.getInt(1);

                
                saveMealFoods(mealId, meal.getFoods());
                return mealId;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; 
    }

    // Save MealFoods for a given meal
    private static void saveMealFoods(int mealId, List<MealFood> foods) {
        String insertSQL = "INSERT INTO meal_foods (meal_id, food_id, quantity_in_grams) VALUES (?, ?, ?)";
        try (Connection conn = Dbfetch.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertSQL)) {

            for (MealFood mf : foods) {
                stmt.setInt(1, mealId);
                stmt.setInt(2, mf.getFoodId());
                stmt.setDouble(3, mf.getQuantity());
                stmt.addBatch();
            }
            stmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Retrieve meals for a specific date
    public static List<Meal> getMealsByDate(LocalDate date) {
        List<Meal> meals = new ArrayList<>();
        String query = "SELECT * FROM meals WHERE meal_date = ?";

        try (Connection conn = Dbfetch.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                MealType type = MealType.valueOf(rs.getString("meal_type"));
                
                Meal meal = new Meal(id, date, type);

                List<MealFood> foods = getMealFoodsByMealId(id);
                foods.forEach(meal::AddCourse);

                meals.add(meal);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return meals;
    }

    // Retrieve foods for a given meal
    public static List<MealFood> getMealFoodsByMealId(int mealId) {
        List<MealFood> mealFoods = new ArrayList<>();
        String query = "SELECT food_id, quantity_in_grams FROM meal_foods WHERE meal_id = ?";

        try (Connection conn = Dbfetch.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, mealId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int foodId = rs.getInt("food_id");
                double quantity = rs.getDouble("quantity_in_grams");
                mealFoods.add(new MealFood(foodId, quantity,MealFoodDAO.getFoodNameById(foodId)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mealFoods;
    }
}