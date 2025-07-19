package app;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.sql.Date;


public class MealDAO {

    // Save a meal, return ID
    public static int saveMeal(Meal meal, String username) {
        String insertMealSQL = "INSERT INTO meals (meal_date, meal_type, username) VALUES (?, ?, ?)";
        try (Connection conn = Dbfetch.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertMealSQL, Statement.RETURN_GENERATED_KEYS)) {

        	stmt.setDate(1, Date.valueOf(meal.getDate()));
            stmt.setString(2, meal.getType().name());
            stmt.setString(3, username);
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                int mealId = keys.getInt(1);

                
                saveMealFoods(mealId, meal.getFoods(), username);
                return mealId;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; 
    }

    // Save MealFoods for a given meal
    private static void saveMealFoods(int mealId, List<MealFood> foods, String username) {
        String insertSQL = "INSERT INTO meal_foods (meal_id, food_id, quantity_in_grams, username) VALUES (?, ?, ?, ?)";
        try (Connection conn = Dbfetch.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertSQL)) {

            for (MealFood mf : foods) {
                stmt.setInt(1, mealId);
                stmt.setInt(2, mf.getFoodId());
                stmt.setDouble(3, mf.getQuantity());
                stmt.setString(4, username);
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


	public static Meal getMealById(int mealId) {
		
		    String query = "SELECT * FROM meals WHERE id = ?";

		    try (Connection conn = Dbfetch.getConnection();
		         PreparedStatement stmt = conn.prepareStatement(query)) {

		        stmt.setInt(1, mealId);
		        ResultSet rs = stmt.executeQuery();

		        if (rs.next()) {
		            LocalDate mealDate = rs.getDate("meal_date").toLocalDate();
		            MealType type = MealType.valueOf(rs.getString("meal_type"));

		            Meal meal = new Meal(mealId, mealDate, type);

		            List<MealFood> foods = getMealFoodsByMealId(mealId);
		            foods.forEach(meal::AddCourse);

		            return meal;
		        }

		    } catch (SQLException e) {
		        e.printStackTrace(); 
		    }

		    return null; // Not found or failed
		
	}

    //Retrieve food between two dates
    public static List<Meal> getMealsByDates(LocalDate start, LocalDate end, String username) {
        List<Meal> meals = new ArrayList<>();
        String query = "SELECT * FROM meals WHERE meal_date BETWEEN ? AND ? AND username = ?";

        try (Connection conn = Dbfetch.getConnection()){
        	PreparedStatement stmt = conn.prepareStatement(query);
        	
        	stmt.setDate(1, Date.valueOf(start));
        	stmt.setDate(2, Date.valueOf(end));
        	stmt.setString(3,  username);
        	
        	ResultSet rs = stmt.executeQuery();
        	while (rs.next()) {
        		int id = rs.getInt("id");
        		
        		Date meal_date = Date.valueOf(rs.getString("meal_date"));
        		LocalDate date = meal_date.toLocalDate();
				
        		MealType type = MealType.valueOf(rs.getString("meal_type"));
        		
        		List<MealFood> foods = getMealFoodsByMealId(id);
        		Meal meal = new Meal(id, date, type, foods);
	        
        		meals.add(meal);
        	}
        	
        } catch (SQLException e) {
        		e.printStackTrace();
        }
		return meals;
    }
    
    //Swaps meals between two dates
    public static void swapMeals(int foodID_old, int foodID_new, LocalDate start, LocalDate end, String username) {
    	
    	try (Connection conn = Dbfetch.getConnection()) {
    		String query = """
    				UPDATE meal_foods
    				JOIN meals ON meal_foods.meal_id = meals.id
    				SET meal_foods.food_id = ?
    				WHERE meal_foods.food_id = ?
    				AND meal_foods.username = ?
    				AND meals.meal_date BETWEEN ? AND ?
    				""";
    		
    		PreparedStatement stmt = conn.prepareStatement(query);
    		
    		stmt.setInt(1, foodID_new);
    		stmt.setInt(2, foodID_old);
    		stmt.setString(3, username);
    		stmt.setDate(4, Date.valueOf(start));
    		stmt.setDate(5, Date.valueOf(end));
    		
    		stmt.executeUpdate();
    		
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }
}

