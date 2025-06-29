package app;

import java.util.HashMap;
import java.util.Map;

public class Nutrition {
	//unused so far, will allow nutritional breakdowns
    private Map<String, Double> nutrients;

    public Nutrition() {
        this.nutrients = new HashMap<>();
    }

    public void setNutrient(String name, double amount) {
        nutrients.put(name, amount);
    }

    public double getNutrient(String name) {
        return nutrients.getOrDefault(name, 0.0);
    }

    public void add(Nutrition other, double factor) {
        for (Map.Entry<String, Double> entry : other.nutrients.entrySet()) {
            String key = entry.getKey();
            double value = entry.getValue() * factor;
            this.nutrients.put(key, this.getNutrient(key) + value);
        }
    }

    public Map<String, Double> getAll() {
        return nutrients;
    }
}