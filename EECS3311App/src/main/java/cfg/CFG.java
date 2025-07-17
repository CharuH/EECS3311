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
		//get food groups of each meal 
		double FruitVeg = 0;
		double Protein = 0;
		double Grain = 0;
		double Mixed = 0;
		String username = main.getUser().getUsername();
		CFGDAO cfg = new CFGDAO();
		double[] fg = cfg.getAverageMealFG(username); 
		for (int x=1; x<=25; x++) {
			//System.out.println("Food Group " + (x) + ": " + fg[x-1]);
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
		
		//add data for table1
		HashMap<String, Double> data1 = new HashMap<>();
		data1.put("Fruits & Vegetables", FruitVeg);
		data1.put("Protein", Protein);
		data1.put("Grain", Grain);
		data1.put("Mixed/Other", Mixed);
		
		//add data for table2
		HashMap<String, Double> data2 = new HashMap<>();
		data2.put("Fruits & Vegetables", 5.0);
		data2.put("Protein", 2.5);
		data2.put("Grain", 2.5);
		data2.put("Mixed/Other", 0.0);
		
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
        Dimension preferredSize = new Dimension(350, 250);  
        chartPanel1.setPreferredSize(preferredSize);
        chartPanel2.setPreferredSize(preferredSize);
  
        //add chart 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;  
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; 
        add(chartPanel1, gbc);

        //add chart 2
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;  
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; 
        add(chartPanel2, gbc);

       
	}
	
}
