package app;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.Map;

import javax.swing.*;
import org.jfree.chart.*;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public class MealBreakdownTab extends JPanel {

    public MealBreakdownTab(Meal meal) {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Nutrient Breakdown for Meal: " + meal.getType().name());
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        ChartPanel chartPanel = createNutrientChart(meal);
        add(chartPanel, BorderLayout.CENTER);

        JTextArea mealDetails = new JTextArea();
        mealDetails.setEditable(false);
        StringBuilder sb = new StringBuilder("Meal Components:\n");
        for (MealFood mf : meal.getFoods()) {
            sb.append(String.format("- %s: %.1fg\n", mf.getName(), mf.getQuantity()));
        }
        mealDetails.setText(sb.toString());
        mealDetails.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mealDetails, BorderLayout.SOUTH);
    }

    private ChartPanel createNutrientChart(Meal meal) {
        Nutrition total = new Nutrition();
        for (MealFood mf : meal.getFoods()) {
            total.add(mf.getNutrition(), mf.getQuantity());
        }

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Map.Entry<Integer, Double> entry : total.getAll().entrySet()) {
            String nutrientName = NutritionDAO.getNutrientNameByID(entry.getKey());
            double value = entry.getValue();
            if (value > 0) {
                dataset.setValue(nutrientName, value);
            }
        }

        JFreeChart chart = ChartFactory.createPieChart(
            "Nutrient Composition",
            dataset,
            true,
            true,
            false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSimpleLabels(false);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1}"));
        plot.setLabelBackgroundPaint(Color.WHITE);
        plot.setLabelOutlinePaint(null);
        plot.setLabelShadowPaint(null);

        return new ChartPanel(chart);
    }
}
