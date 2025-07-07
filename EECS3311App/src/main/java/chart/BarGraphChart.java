package chart;

import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class BarGraphChart {

	JFreeChart createChart(HashMap<String, Double> data) {
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<String, Double> entry: data.entrySet()) {
			dataSet.addValue(entry.getValue(), null, entry.getKey());
		}
		
		JFreeChart chart = ChartFactory.createBarChart(null, null, null, dataSet);
		return chart;
	}
}
