package app;

import java.util.HashMap;
import java.util.Map;

public class Nutrition {
	
    private Map<Integer, Double> nutrients;

    public Nutrition() {
        this.nutrients = new HashMap<>();
    }

    public void setNutrient(int NutrientID, double amount) {
        nutrients.put(NutrientID, amount);
    }

    public double getNutrient(int key) {
        return nutrients.getOrDefault(key, 0.0);
    }

    public void add(FoodNutrition other, double factor) {
        for (Map.Entry<Integer, Double> entry : other.getAll().entrySet()) {
            Integer key = entry.getKey();
            double value = entry.getValue() * factor;
            this.nutrients.put(key, this.getNutrient(key) + value);
        }
    }

    public Map<Integer, Double> getAll() {
        return nutrients;
    }
}