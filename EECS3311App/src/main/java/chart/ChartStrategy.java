package chart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.jfree.chart.JFreeChart;

import dailyNutrition.Nutrient;

public interface ChartStrategy {
	public JFreeChart createChart(HashMap<String, Double> data, String title, String yAxis, String xAxis);
	public JFreeChart createChart(ArrayList<Entry<Nutrient, Double>> data, String title, String yAxis, String xAxis);

}