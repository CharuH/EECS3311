package chart;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import dailyNutrition.Nutrient;

public class PieChartStrategy implements ChartStrategy{

	@Override
	public JFreeChart createChart(HashMap<String, Double> data, String title, String yAxis, String xAxis) {
		DefaultPieDataset<String> dataSet = new DefaultPieDataset<>();
		for (Map.Entry<String, Double> entry: data.entrySet()) {
			dataSet.setValue(entry.getKey(), entry.getValue());
		}
		
		JFreeChart chart = ChartFactory.createPieChart(title, dataSet);
		
		@SuppressWarnings("unchecked")
		PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
        StandardPieSectionLabelGenerator generator = new StandardPieSectionLabelGenerator(
                "{0}: {2}", // {0}=label, {1}=absolute value, {2}=percentage
                new DecimalFormat("0"), new DecimalFormat("0.0%"));
        plot.setLabelGenerator(generator);
		return chart;
	}

	@Override
	public JFreeChart createChart(ArrayList<Entry<Nutrient, Double>> data, String title, String yAxis, String xAxis) {
		DefaultPieDataset<String> dataSet = new DefaultPieDataset<>();
		for (Map.Entry<Nutrient, Double> entry: data) {
			dataSet.setValue(entry.getKey().getNutrient(), entry.getValue());
		}
		
		JFreeChart chart = ChartFactory.createPieChart(title, dataSet);
		
		@SuppressWarnings("unchecked")
		PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
        StandardPieSectionLabelGenerator generator = new StandardPieSectionLabelGenerator(
                "{0}: {2}", // {0}=label, {1}=absolute value, {2}=percentage
                new DecimalFormat("0"), new DecimalFormat("0.0%"));
        plot.setLabelGenerator(generator);
		return chart;
	}
	
	
}
