package chart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.jfree.chart.JFreeChart;

import dailyNutrition.Nutrient;

public class ChartContext {
	private ChartStrategy chartType;
	
	public void setChartType(ChartStrategy chartType) {
		this.chartType = chartType;
	}
	
	public JFreeChart getChart(HashMap<String, Double> data, String title, String yAxis, String xAxis) {
		return chartType.createChart(data, title, yAxis, xAxis);
	}
	
	public JFreeChart getChart(ArrayList<Entry<Nutrient, Double>> data, String title, String yAxis, String xAxis) {
		return chartType.createChart(data, title, yAxis, xAxis);
	}
	

}