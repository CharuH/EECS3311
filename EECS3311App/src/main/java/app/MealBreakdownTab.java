package app;
import java.util.Map;

import javax.swing.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public class MealBreakdownTab extends JPanel {

    public MealBreakdownTab(Meal meal) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Nutrient Breakdown for Meal: " + meal.getType().name()));

        ChartPanel chartPanel = createNutrientChart(meal);
        add(chartPanel);
    }

    private ChartPanel createNutrientChart(Meal meal) {
        // Aggregate nutrition
        Nutrition total = new Nutrition();
        for (MealFood mf : meal.getFoods()) {
            total.add(mf.getNutrition(), mf.getQuantity());
        }

        // Prepare dataset
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Map.Entry<Integer, Double> entry : total.getAll().entrySet()) {
            String nutrientName = NutritionDAO.getNutrientNameByID(entry.getKey()); // You’d define this helper
            double value = entry.getValue();
            if (value > 0) {
                dataset.setValue(nutrientName, value);
            }
        }

        // Create pie chart
        JFreeChart chart = ChartFactory.createPieChart(
            "Nutrient Composition",
            dataset,
            true,
            true,
            false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSimpleLabels(true);

        return new ChartPanel(chart);
    }
}
