package app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class applySwapToDateRange {	
	public static void applySwapToDateRange(Meal original, Meal swapped,LocalDate start, LocalDate end, String username) {
		List<Meal> mealsInRange = MealDAO.getMealsByDates(start, end, username);
		List<Meal> matchingMeals = new ArrayList<>();

		for (Meal candidate : mealsInRange) {
			if (mealsStructurallyEqual(candidate, original)) {
				matchingMeals.add(candidate);
			}
		}

		if (matchingMeals.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No matching meals found in the selected range.");
			return;
		}

		// Calculate cumulative change
		Nutrition originalTotal = new Nutrition();
		Nutrition swappedTotal = new Nutrition();

		for (int i = 0; i < matchingMeals.size(); i++) {
			originalTotal.add(original.getNutrition(), 1.0);
			swappedTotal.add(swapped.getNutrition(), 1.0);
		}

		CumulativeComparisonDialog.showCumulativeComparisonDialog(
			    originalTotal, swappedTotal, matchingMeals.size(),
			    () -> {
			        
			        for (Meal m : matchingMeals) {
			            MealDAO.deleteMealFoods(m.getID(), username);
			            Meal newMeal = swapped.copy();  
			            newMeal.setID(m.getID());
			            MealDAO.insertMealFoods(newMeal, username);
			        }
			        JOptionPane.showMessageDialog(null, "Meals updated!");
			    }
			);

		
		int response = JOptionPane.showConfirmDialog(null,
		"Replace all matching meals (" + matchingMeals.size() + ")?", "Confirm Swap", JOptionPane.YES_NO_OPTION);

		if (response == JOptionPane.YES_OPTION) {
			for (Meal match : matchingMeals) {
				MealDAO.deleteMealFoods(match.getID(), username);
				Meal swappedClone = swapped.copy(); 
				MealDAO.insertMealFoods(swappedClone, username);
			}	
			JOptionPane.showMessageDialog(null, "All matching meals have been replaced.");
		}
}

	private static boolean areMealsEqual(Meal candidate, Meal original) {
		//if(original.getType()!=candidate.getType())return false; 
		if (original.getFoods().size() != candidate.getFoods().size()) return false;
		for (int i = 0; i < original.getFoods().size(); i++) {
	        MealFood fa = original.getFoods().get(i);
	        MealFood fb = candidate.getFoods().get(i);

	        if (fa.getFoodId() != fb.getFoodId()) return false;
	        if (Math.abs(fa.getQuantity() - fb.getQuantity()) > 0.01) return false;
	    }
		return true;
	}
	
	private static boolean mealsStructurallyEqual(Meal m1, Meal m2) {
	    if (m1.getFoods().size() != m2.getFoods().size()) return false;

	    for (int i = 0; i < m1.getFoods().size(); i++) {
	        MealFood f1 = m1.getFoods().get(i);
	        MealFood f2 = m2.getFoods().get(i);
	        if (f1.getFoodId() != f2.getFoodId()) return false;

	        double q1 = m1.getQuantities().get(i);
	        double q2 = m2.getQuantities().get(i);
	        if (Math.abs(q1 - q2) > 0.01) return false; 
	    }
	    return true;
	}

	

}
