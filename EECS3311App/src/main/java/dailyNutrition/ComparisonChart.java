package dailyNutrition;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JTable;

public class ComparisonChart {

	public static JTable getComparisonTable(ArrayList<Entry<Nutrient, Double>>  avgNutrition, ArrayList<Entry<Nutrient, Double>>  recNutrition) {
		String[] columnNames = {"Nutrient", "Average (g)", "Recommended (g)", "Difference (g)"};
		String[][] data = new String[11][4];
		
		//set nutrient column and average columns
		int row = 0;
		for (Map.Entry<Nutrient, Double> entry: avgNutrition) {
			data[row][0] = entry.getKey().getNutrient();
			data[row][1] = Double.toString(entry.getValue());
			row++;
		}
		
		//set recommended and difference columns
		for(Map.Entry<Nutrient, Double> entry: recNutrition) {
			for (int r=0; r<11; r++) {
				//match recommended amount corresponding nutrient
				if (entry.getKey().getNutrient().equals(data[r][0])) {
					if (entry.getValue() == 0.0) {
						//if no data
						data[r][2] = "No Data";
						data[r][3] = "N/A";
					} else {
						data[r][2] = Double.toString(entry.getValue());
						double difference = Double.parseDouble(data[r][1]) - entry.getValue(); 
						data[r][3] = Double.toString(difference);
					}
				}
			}
		}
		JTable table = new JTable(data, columnNames);
		return table;
	}
}
