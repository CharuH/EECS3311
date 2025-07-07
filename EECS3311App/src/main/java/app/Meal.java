package app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Meal {
    private int id;
    private LocalDate date;
    private MealType type;
    private List<MealFood> foods;
    

    public Meal() {
        this.foods = new ArrayList<>();
        
    }
    
    public Meal(int id,LocalDate date,MealType type) {
        this.foods = new ArrayList<>();
        this.date=date;
        this.id=id;
        this.type=type;
        
    }
    
    public void AddCourse(MealFood course) {
    	foods.add(course);
    }
    public void AddDate(LocalDate date) {
    	this.date=date;
    }
    
    
    
    public double getCalories() {
    	double result=0.0;
    	
    	
    	for(MealFood meal:foods) {
    		result+=meal.returnCalories();
    	
    	
    }

    	
		return result;
    }
    
    public Nutrition getNutrition() {
        Nutrition totalNutrition = new Nutrition();
        for (MealFood food : foods) {
            totalNutrition.add(food.getNutrition(), food.getQuantity());
        }
        return totalNutrition;
    }
    
    public List<MealFood> getFoods(){
    	return foods;
    }
    
    public MealType getType() {
    	return type;
    }

	public LocalDate getDate() {
		
		return date;
	}

	public void addFood(MealFood mealFood) {
		foods.add(mealFood);
		
	}
	
	public void setType(MealType type) {
	    this.type = type;
	}

	public void setDate(LocalDate date) {
	    this.date = date;
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("Meal ID: ").append(id).append("\n");
	    sb.append("Date: ").append(date).append("\n");
	    sb.append("Type: ").append(type).append("\n");
	    sb.append("Foods:\n");

	    for (MealFood food : foods) {
	        sb.append("  - ").append(food.toString()).append("\n");
	    }

	    sb.append("Total Nutrition:\n");
	    sb.append(getNutrition().toString()); 

	    return sb.toString();
	}
    
    
    
    
    
    
}

