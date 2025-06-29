package app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    	Iterator<MealFood> value = foods.iterator();
    	
    	for(MealFood meal:foods) {
    		result+=meal.returnCalories();
    	
    	
    }
    	
    	
    	
    
    	
		return result;
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
    
    
    
    
    
    
}

