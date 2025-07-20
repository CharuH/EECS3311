package dailyNutrition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SortNutrients {

	public static ArrayList<Entry<Nutrient, Double>> sortMapByValuesDescending(HashMap<Nutrient, Double> nutrition) {
        //convert the HashMap to an ArrayList
        ArrayList<Entry<Nutrient, Double>> sortedList = new ArrayList<>(nutrition.entrySet());
        //sort the list in descending order
        sortedList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        return sortedList;
    }
	
	public static ArrayList<Entry<Nutrient, Double>> getTopNutrients(ArrayList<Entry<Nutrient, Double>> nutrition) {
		ArrayList<Entry<Nutrient, Double>> topNutrients = new ArrayList<>();
		HashMap<Nutrient, Double> other = new HashMap<>();
		double sumOther = 0;
		//go through each nutrient
		for (int x=0; x<nutrition.size(); x++) {
			Map.Entry<Nutrient, Double> entry = nutrition.get(x);
			//if top 10, add amount on its own
			if (x < 10) {
				topNutrients.add(entry);
			//else, add amount to other
			} else {
				sumOther += entry.getValue();
			}
		}
		other.put(new Nutrient(0, "Other", "g"), sumOther);
		//add HashMap entries to ArrayList
		for (Entry<Nutrient, Double> entryOther : other.entrySet()) {
            topNutrients.add(entryOther);
        }
		return topNutrients;
	}
}
