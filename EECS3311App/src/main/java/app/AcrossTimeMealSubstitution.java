package app;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AcrossTimeMealSubstitution {
	Nutrition cumulative = new Nutrition();
	Nutrition average = new Nutrition();
	Map<Integer, Nutrition> perMeal = new HashMap<Integer, Nutrition>();
	
	//USE CASE 5
	public AcrossTimeMealSubstitution(int foodID_new, int foodID_old, LocalDate start, LocalDate end, String username) {
		
		List<Meal> old_meals = MealDAO.getMealsByDates(start, end, username);
		Nutrition old_meals_nutrition = NutritionCalculator.calculateNutrienceInMeals(old_meals);
		Map<Integer, Nutrition> old_nutrition_per_plate = NutritionCalculator.calculateNutritionPerMeal(old_meals);
		
		MealDAO.swapMeals(foodID_old, foodID_new, start, end, username);
		
		List<Meal> new_meals = MealDAO.getMealsByDates(start, end, username);
		Nutrition new_meals_nutrition = NutritionCalculator.calculateNutrienceInMeals(new_meals);
		Map<Integer, Nutrition> new_nutrition_per_plate = NutritionCalculator.calculateNutritionPerMeal(new_meals);
		
		this.cumulative = NutritionCalculator.calculateChangeCumulative(old_meals_nutrition, new_meals_nutrition);
		this.average = NutritionCalculator.calculateChangeAverage(cumulative, start, end);
		this.perMeal = NutritionCalculator.calculateChangePerMeal(old_nutrition_per_plate, new_nutrition_per_plate);
	}
	
}
