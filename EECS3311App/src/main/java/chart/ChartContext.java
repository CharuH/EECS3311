package chart;

import java.util.HashMap;

import org.jfree.chart.JFreeChart;

public class ChartContext {
	private ChartStrategy chartType;
	
	public void setChartType(ChartStrategy chartType) {
		this.chartType = chartType;
	}
	
	public JFreeChart getChart(HashMap<String, Double> data) {
		return chartType.createChart(data);
	}
	

}
