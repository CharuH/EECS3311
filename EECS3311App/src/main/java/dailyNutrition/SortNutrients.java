package dailyNutrition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SortNutrients {

	public static ArrayList<Map.Entry<Nutrient, Double>> sortMapByValuesDescending(HashMap<Nutrient, Double> nutrition) {
        // Convert the hashmap to a arraylist
        ArrayList<Map.Entry<Nutrient, Double>> sortedList = new ArrayList<>(nutrition.entrySet());

        // Sort the list using a custom comparator based on the Double value (descending order)
        sortedList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Convert List to ArrayList and return
        return sortedList;
    }
	
	public static ArrayList<Map.Entry<Nutrient, Double>> getTopNutrients(ArrayList<Map.Entry<Nutrient, Double>> nutrition) {
		ArrayList<Map.Entry<Nutrient, Double>> topNutrients = new ArrayList<>();
		HashMap<Nutrient, Double> other = new HashMap<>();
		double sumOther = 0;
		for (int x=0; x<nutrition.size(); x++) {
			Map.Entry<Nutrient, Double> entry = nutrition.get(x);
			if (x < 10) {
				topNutrients.add(entry);
			} else {
				sumOther += entry.getValue();
			}
		}
		other.put(new Nutrient(0, "Other", "g"), sumOther);
		for (Map.Entry<Nutrient, Double> entryOther : other.entrySet()) {
            topNutrients.add(entryOther);
        }
		return topNutrients;
	}
}
