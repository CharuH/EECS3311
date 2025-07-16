package app;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

public class MealComparisonTab extends JPanel {

    public MealComparisonTab(Meal original, Meal modified) {
        setLayout(new BorderLayout());

        // Top: Title
        JLabel title = new JLabel("Meal Comparison", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        // Center: Nutrient comparison
        JPanel comparisonPanel = new JPanel(new GridLayout(0, 3, 10, 5));
        Map<Integer, Double> orig = original.getNutrition().getAll();
        Map<Integer, Double> mod = modified.getNutrition().getAll();
        comparisonPanel.setLayout(new GridLayout(0, 4, 10, 5));
        comparisonPanel.add(new JLabel("Nutrient (Unit)", JLabel.LEFT));
        comparisonPanel.add(new JLabel("Original", JLabel.CENTER));
        comparisonPanel.add(new JLabel("Modified", JLabel.CENTER));
        comparisonPanel.add(new JLabel("Change", JLabel.CENTER));

        // Sort nutrients alphabetically
        Map<String, Integer> nameToId = new TreeMap<>();
        for (Integer id : orig.keySet()) {
            double val1 = orig.getOrDefault(id, 0.0);
            double val2 = mod.getOrDefault(id, 0.0);
            if (val1 == 0.0 && val2 == 0.0) continue;

            String name = NutritionDAO.getNutrientNameByID(id);
            if (name != null && !name.isBlank()) {
                nameToId.put(name, id);
            }
        }

        // Display rows
        for (Map.Entry<String, Integer> entry : nameToId.entrySet()) {
            String name = entry.getKey();
            int id = entry.getValue();

            double val1 = orig.getOrDefault(id, 0.0);
            double val2 = mod.getOrDefault(id, 0.0);

            String unit = NutritionDAO.getNutrientUnitByID(id); // You need to implement this
            String label = name + (unit != null && !unit.isBlank() ? " (" + unit + ")" : "");

            JLabel nameLabel = new JLabel(label);
            JLabel val1Label = new JLabel(String.format("%.1f", val1));
            JLabel val2Label = new JLabel(String.format("%.1f", val2));
            JLabel diffLabel;

            if (val1 == 0) {
                diffLabel = new JLabel("▲ ∞%");
                diffLabel.setForeground(Color.RED);
            } else {
                double percentDiff = ((val2 - val1) / val1) * 100;
                diffLabel = new JLabel(String.format("%+.1f%%", percentDiff));
                diffLabel.setForeground(percentDiff > 0 ? Color.RED : Color.GREEN);
            }

            comparisonPanel.add(nameLabel);
            comparisonPanel.add(val1Label);
            comparisonPanel.add(val2Label);
            comparisonPanel.add(diffLabel);
        }

        add(new JScrollPane(comparisonPanel), BorderLayout.CENTER);

        // Bottom: Meal foods
        JTextArea foodArea = new JTextArea(6, 50);
        foodArea.setEditable(false);
        foodArea.setText("--- Original Meal ---\n");
        for (MealFood f : original.getFoods()) {
            foodArea.append(f.getName() + " - " + f.getQuantity() + "g\n");
        }

        foodArea.append("\n--- Modified Meal ---\n");
        for (MealFood f : modified.getFoods()) {
            foodArea.append(f.getName() + " - " + f.getQuantity() + "g\n");
        }

        add(new JScrollPane(foodArea), BorderLayout.SOUTH);
    }
}
