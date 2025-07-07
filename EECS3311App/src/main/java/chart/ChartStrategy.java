package chart;

import java.util.HashMap;

import org.jfree.chart.JFreeChart;

public interface ChartStrategy {
	public JFreeChart createChart(HashMap<String, Double> data);

}
