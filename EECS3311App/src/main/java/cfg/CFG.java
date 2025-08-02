package cfg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import accCreate.MainUI;
import chart.ChartContext;
import chart.PieChartStrategy;

public class CFG extends JPanel {

	public CFG(MainUI main) {
		String username = main.getUser().getUsername();
		CFGDAO cfg = new CFGDAO();
		double[] foodGroupsCFG = sortGroupsCFG(cfg.getAverageMealFG(username));
		
		//add data for table1
		HashMap<String, Double> data1 = new HashMap<>();
		setChartData(data1, foodGroupsCFG);
		
		//add data for table2
		HashMap<String, Double> data2 = new HashMap<>();
		setChartData(data2, new double[] {5.0, 2.5, 2.5, 0.0});
		
		//setup frame
		setPreferredSize(new Dimension(750, getHeight()));
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		setBackground(Color.WHITE);
		
		//set chart context
		ChartContext context1 = new ChartContext();
		ChartContext context2 = new ChartContext();

        //set chart type
        context1.setChartType(new PieChartStrategy()); 
        context2.setChartType(new PieChartStrategy());

        //get chart
        JFreeChart chart1 = context1.getChart(data1, "Average Meal", null, null);
        JFreeChart chart2 = context2.getChart(data2, "CFG Recommended Meal", null, null);
        
        //create chart
        ChartPanel chartPanel1 = new ChartPanel(chart1);
        ChartPanel chartPanel2 = new ChartPanel(chart2);  
        setChart(chartPanel1, gbc, 0, 0);
        setChart(chartPanel2, gbc, 1, 0);
	}
	
	private double[] sortGroupsCFG(double[] fg) {
		//get food groups of each meal 
		double FruitVeg = 0;
		double Protein = 0;
		double Grain = 0;
		double Mixed = 0; 
		for (int x=1; x<=25; x++) {
			if (x == 9 || x == 11) {
				FruitVeg += fg[x-1];
			} else if (x == 8 || x == 20) {
				Grain += fg[x-1];
			} else if (x == 1 || x == 5 || x == 7 || x == 10 || x == 12 || x == 13 || x == 15 || x == 16 || x == 17) {
				Protein += fg[x-1];
			} else {
				Mixed += fg[x-1];
			}
		}
		double[] fgCFG = {FruitVeg, Protein, Grain, Mixed};
		return fgCFG;
	}
	
	private void setChart(ChartPanel panel, GridBagConstraints gbc, int x, int y) {
		Dimension preferredSize = new Dimension(350, 250);  
		panel.setPreferredSize(preferredSize);
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = 1;  
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; 
		add(panel, gbc);
	}
	
	private void setChartData(HashMap<String, Double> chartData, double[] data) {
		chartData.put("Fruits & Vegetables", data[0]);
		chartData.put("Protein", data[1]);
		chartData.put("Grain", data[2]);
		chartData.put("Mixed/Other", data[3]);
	}
	
}
