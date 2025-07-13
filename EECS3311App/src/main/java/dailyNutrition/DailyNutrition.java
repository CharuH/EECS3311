package dailyNutrition;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import com.toedter.calendar.JDateChooser;

import accCreate.MainUI;
import chart.BarGraphChart;
import chart.ChartContext;
import chart.PieChartStrategy;

public class DailyNutrition extends JPanel {

	GridBagConstraints gbc; 
	boolean chartsAdded = false;
	
	public DailyNutrition(MainUI main) {
		setPreferredSize(new Dimension(750, getHeight()));
		setLayout(new GridBagLayout());
		setRange(main);
	}
	
	public void setRange(MainUI main) {
		gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 5, 5, 5); // spacing between components
		setBackground(Color.WHITE);

		//Instructions
		JLabel instructions = new JLabel("Select Start and End Date to Visualize Daily Nutrient Intake", SwingConstants.CENTER);
		gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 5; 
        gbc.fill = GridBagConstraints.BOTH;
		add(instructions, gbc );
		
		//Start Date
		JLabel dateStartLabel = new JLabel("Start Date");
		gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; 
        gbc.fill = GridBagConstraints.BOTH;
		add(dateStartLabel, gbc);
		
		JDateChooser dateStart = new JDateChooser();
		gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1; 
        gbc.fill = GridBagConstraints.BOTH;
		add(dateStart, gbc);
		
		//End Date
		JLabel dateEndLabel = new JLabel("End Date");
		gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1; 
        gbc.fill = GridBagConstraints.BOTH;
		add(dateEndLabel, gbc);
		
		JDateChooser dateEnd = new JDateChooser();
		gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 1; 
        gbc.fill = GridBagConstraints.BOTH;
		add(dateEnd, gbc);
		
		//Calculate Button
		JButton calcButton = new JButton("Calculate");
		gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.gridwidth = 1; 
        gbc.fill = GridBagConstraints.BOTH;
        calcButton.setBackground(new Color(58, 162, 224));
		calcButton.setContentAreaFilled(false);
		calcButton.addMouseListener(new MouseAdapter() {
            // When the mouse enters the button, change color
            public void mouseEntered(MouseEvent e) {
                calcButton.setContentAreaFilled(true);
            }
            
            // When the mouse exits the button, reset the color
            public void mouseExited(MouseEvent e) {
                calcButton.setContentAreaFilled(false); 
            }
        });
		calcButton.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
				//get start date
				Date startTemp = dateStart.getDate();
				LocalDate Start = startTemp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				//get end date
				Date endTemp = dateEnd.getDate();
				LocalDate End = endTemp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				String username = main.getUser().getUsername();
				DailyNutritionDAO nutritionDAO = new DailyNutritionDAO();
				//get average nutrition from start to end date
				HashMap<Integer, Double> avgNutrition = nutritionDAO.getNutritionAverage(Start, End, username);
				System.out.println(avgNutrition);
				System.out.println("SizeBefore: " + avgNutrition.size());
				//get average nutrition from start to end date (with names and units)
				HashMap<Nutrient, Double> avgNutritionNamed = nutritionDAO.convertUnitsName(avgNutrition);
				System.out.println(avgNutritionNamed);
				System.out.println("SizeAfter: " + avgNutritionNamed.size());
				//sort average nutrition in descending order
				ArrayList<Map.Entry<Nutrient, Double>> nutritionDescending = SortNutrients.sortMapByValuesDescending(avgNutritionNamed);
				System.out.println(nutritionDescending);
				System.out.println("SizeList: " + nutritionDescending.size());
				//get top 10 nutrients + other 
				ArrayList<Entry<Nutrient, Double>> topList = SortNutrients.getTopNutrients(nutritionDescending);
				for (int x=0; x< topList.size(); x++) {
					System.out.println((x+1) + ": " + topList.get(x));
				}
				System.out.println("SizeList: " + topList.size());
				//get matching recommended nutrient amounts corresponding to top 10 nutrients
				ArrayList<Entry<Nutrient, Double>> recList = dailyRecommendedNutrition.getRecommendedNutrition(topList, main.getUser());
				for (int x=0; x< recList.size(); x++) {
					System.out.println((x+1) + ": " + recList.get(x));
				}
				System.out.println("SizeList: " + recList.size());
				//check if charts added before
				if (chartsAdded) {
					//refresh panel
					refresh(main);
				} 
				//add charts
				addCharts(topList, recList);
			}
		});
		add(calcButton, gbc);
	}

	
	public void addCharts(ArrayList<Entry<Nutrient, Double>>  avgNutrition, ArrayList<Entry<Nutrient, Double>>  recNutrition) {
		//set chart context
		ChartContext context1 = new ChartContext();

        //set chart type
        context1.setChartType(new BarGraphChart()); 

      
        //get chart
        JFreeChart chart = context1.getChart(avgNutrition, "Top 10 Daily Average Nutrition", "Grams", "Nutrients");
     
        //create chart
        ChartPanel chartPanel = new ChartPanel(chart);
        Dimension preferredSize = new Dimension(350, 250);  
        chartPanel.setPreferredSize(preferredSize);
        
        //add chart 
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 5;  
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; 
        add(chartPanel, gbc);
        
        //add comparison table
        JTable compareTable = ComparisonChart.getComparisonTable(avgNutrition, recNutrition);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 5;  
        gbc.fill = GridBagConstraints.BOTH;
        add(new JScrollPane(compareTable), gbc);
        chartsAdded = true;
        revalidate();
        repaint();
	}
	
	public void refresh(MainUI main) {
		removeAll();
		setRange(main);
		
	}
	
}
