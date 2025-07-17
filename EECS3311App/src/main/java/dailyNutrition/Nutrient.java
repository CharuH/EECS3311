package dailyNutrition;

public class Nutrient {

	private int nutrientID;
	private String nutrient;
	private String unit;
	
	public Nutrient(int nutrientID, String nutrient, String unit) {
		this.nutrientID = nutrientID;
		this.nutrient = nutrient;
		this.unit = unit;
	}

	public int getNutrientID() {
		return nutrientID;
	}

	public void setNutrient(int nutrientID) {
		this.nutrientID = nutrientID;
	}
	
	public String getNutrient() {
		return nutrient;
	}

	public void setNutrient(String nutrient) {
		this.nutrient = nutrient;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String toString() {
		return "[" + nutrientID + ": " + nutrient  + ", " + unit + "]";
	}
	
}