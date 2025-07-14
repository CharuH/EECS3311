package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MealSubstitutionTester {
    

    

    
    

    
    
    public static void main(String args[]) {
    	
    	List<MealFood> foods= new ArrayList<MealFood>();
    	MealFood food1 =MealFoodDAO.getMealFood(133, 100);
    	MealFood food2 =MealFoodDAO.getMealFood(1223, 80);
    	MealFood food3 =MealFoodDAO.getMealFood(1142, 100);
    	foods.add(food1);
    	foods.add(food2);
    	foods.add(food3);
    	LocalDate date = LocalDate.now();
    	Meal meal= new Meal(100,date,MealType.BREAKFAST,foods );
    	
    	
    	
    	Meal testMeal=MealSwapSearch.suggestSubstitute(meal, NutrientConstants.PROTEIN, 1.5);
    	System.out.println(meal.toString());
    	System.out.println(testMeal.toString());
    	System.out.println("Meal1    TestMeal");
    	System.out.println("Calories:    "+ meal.getNutrition().getNutrient(NutrientConstants.CALORIES)+"    "+testMeal.getNutrition().getNutrient(NutrientConstants.CALORIES));
    	System.out.println("Fat:    "+ meal.getNutrition().getNutrient(NutrientConstants.FAT)+"    "+testMeal.getNutrition().getNutrient(NutrientConstants.FAT));
    	System.out.println("Carbs:    "+ meal.getNutrition().getNutrient(NutrientConstants.CARBS)+"    "+testMeal.getNutrition().getNutrient(NutrientConstants.CARBS));
    	System.out.println("Protein:    "+ meal.getNutrition().getNutrient(NutrientConstants.PROTEIN)+"    "+testMeal.getNutrition().getNutrient(NutrientConstants.PROTEIN));
    	System.out.println("Fiber:    "+ meal.getNutrition().getNutrient(NutrientConstants.FIBER)+"    "+testMeal.getNutrition().getNutrient(NutrientConstants.FIBER));
    	
    	
    	
    	
    	
    	
    }
}