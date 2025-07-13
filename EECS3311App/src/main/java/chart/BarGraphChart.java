package chart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import dailyNutrition.Nutrient;

public class BarGraphChart implements ChartStrategy {

	@Override
	public JFreeChart createChart(HashMap<String, Double> data, String title, String yAxis, String xAxis) {
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<String, Double> entry: data.entrySet()) {
			dataSet.addValue(entry.getValue(), "", entry.getKey());
		}
		
		JFreeChart chart = ChartFactory.createBarChart(title, xAxis, yAxis, dataSet);
		return chart;
	}

	@Override
	public JFreeChart createChart(ArrayList<Entry<Nutrient, Double>> data, String title, String yAxis, String xAxis) {
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<Nutrient, Double> entry: data) {
			dataSet.addValue(entry.getValue(), "Nutrients", entry.getKey().getNutrient());
		}
		
		JFreeChart chart = ChartFactory.createBarChart(title, xAxis, yAxis, dataSet);
		return chart;
	}
}
