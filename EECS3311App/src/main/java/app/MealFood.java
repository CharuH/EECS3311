package app;

import java.util.Map;

public class MealFood extends Food{
           // Foreign key to a food item
    private double quantityInGrams;
    private FoodNutrition cachedNutrition;
    
    

    public MealFood(int foodId, double quantityInGrams,String name) {
    	super(foodId,name);
        
        this.quantityInGrams = quantityInGrams;
        
    }
    
    
    public double getQuantity() {
    	return quantityInGrams;
    }
    
    public double returnCalories() {
    	double Calpergram=NutritionDAO.getKcalbyFoodID(this.foodId);
    	//calories per 100grams
		return (Calpergram/100)*quantityInGrams;
    	
    }
    
    public FoodNutrition getNutrition(){
    	if (cachedNutrition == null) {
            cachedNutrition = NutritionDAO.getNutritionByFoodID(foodId);
        }
        return cachedNutrition;
    }
    
    public MealFood copy() {
        MealFood clone = new MealFood(this.foodId, this.quantityInGrams, this.name);
        clone.cachedNutrition = this.cachedNutrition;
        return clone;
    }
    
    
    
    

    
}