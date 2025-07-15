package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MealSwapControlPanel extends JPanel {
    private final Meal originalMeal;
    private JComboBox<ComboItem> nutrientComboBox;
    private JTextField multiplierField;
    private JButton swapButton;
    private JTextArea resultArea;

    public MealSwapControlPanel(Meal meal) {
        this.originalMeal = meal;

        setLayout(new BorderLayout());

        // --- Input Panel ---
        JPanel inputPanel = new JPanel(new FlowLayout());

        nutrientComboBox = new JComboBox<>();
        populateNutrientDropdown();

        multiplierField = new JTextField("1.10", 5);
        swapButton = new JButton("Find Substitute");

        inputPanel.add(new JLabel("Nutrient:"));
        inputPanel.add(nutrientComboBox);
        inputPanel.add(new JLabel("×"));
        inputPanel.add(multiplierField);
        inputPanel.add(swapButton);

        add(inputPanel, BorderLayout.NORTH);

        // --- Result Display ---
        resultArea = new JTextArea(12, 40);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        swapButton.addActionListener(this::handleSwap);
    }

    private void handleSwap(ActionEvent e) {
        ComboItem selected = (ComboItem) nutrientComboBox.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Select a nutrient.");
            return;
        }

        double multiplier;
        try {
            multiplier = Double.parseDouble(multiplierField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid multiplier.");
            return;
        }

        int nutrientId = selected.getId();
        Meal swapped = MealSwapSearch.suggestSubstitute(originalMeal, nutrientId, multiplier);

        resultArea.setText("");
        if (swapped == null) {
            resultArea.setText("No suitable substitute found.");
        } else {
            resultArea.append("Suggested Meal:\n");
            for (MealFood mf : swapped.getFoods()) {
                resultArea.append(String.format("- %s: %.1fg\n", mf.getName(), mf.getQuantity()));
            }
            resultArea.append(String.format("\nTotal Calories: %.1f kcal", swapped.getCalories()));
        }
    }

    private void populateNutrientDropdown() {
        List<Integer> nutrientIDs = NutritionDAO.getAllNutrientIDs();
        DefaultComboBoxModel<ComboItem> model = new DefaultComboBoxModel<>();
        for (int id : nutrientIDs) {
            String name = NutritionDAO.getNutrientNameByID(id);
            if (name != null && !name.isBlank()) {
                model.addElement(new ComboItem(id, name));
            }
        }
        nutrientComboBox.setModel(model);
    }

    private static class ComboItem {
        private final int id;
        private final String label;

        public ComboItem(int id, String label) {
            this.id = id;
            this.label = label;
        }

        public int getId() {
            return id;
        }

        public String toString() {
            return label;
        }
    }
}
