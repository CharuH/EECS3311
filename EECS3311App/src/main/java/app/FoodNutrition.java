package app;

import java.util.HashMap;
import java.util.Map;

public class FoodNutrition {
	private Map<Integer, Double> nutrients;
	public int FoodID;
	

    

    

    public FoodNutrition(int foodID) {
    	this.nutrients = new HashMap<>();
        this.FoodID=FoodID;
	}



	public double getNutrient(Integer key) {
        return nutrients.getOrDefault(key, 0.0);
    }
    
    public int getFoodID() {
    	return FoodID;
    }

    

    public Map<Integer, Double> getAll() {
        return nutrients;
    }

	public void setNutrient(Integer NutrientID, double nutrientValue) {
		nutrients.put(NutrientID, nutrientValue);
		
	}



	

	
	

}
