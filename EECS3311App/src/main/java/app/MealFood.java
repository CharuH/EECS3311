package app;

public class MealFood extends Food{
           // Foreign key to a food item
    private double quantityInGrams;
    

    public MealFood(int foodId, double quantityInGrams,String name) {
    	super(foodId,name);
        
        this.quantityInGrams = quantityInGrams;
        
    }
    
    
    public double getQuantity() {
    	return quantityInGrams;
    }
    
    public double returnCalories() {
    	double Calpergram=CalorieDAO.getKcalbyFoodID(this.foodId);
    	//calories per 100grams
		return (Calpergram/100)*quantityInGrams;
    	
    }

    
}