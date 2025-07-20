package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MealSwapControlPanel extends JPanel {
    private final Meal originalMeal;
    private JComboBox<ComboItem> nutrientComboBox;
    private JTextField multiplierField;
    private JButton swapButton, compareButton, applyButton;
    private JTextArea resultArea;
    private Meal swappedMeal;
    private String username;
    

    public MealSwapControlPanel(Meal meal, String username) {
    	this.username=username;
        this.originalMeal = meal;
        setLayout(new BorderLayout());

        // --- Input Panel ---
        JPanel inputPanel = new JPanel(new FlowLayout());

        nutrientComboBox = new JComboBox<>();
        populateNutrientDropdown();

        multiplierField = new JTextField("1.10", 5);
        swapButton = new JButton("Find Substitute");
        compareButton = new JButton("Compare Meals");
        compareButton.setEnabled(false); // Disabled initially
        
        
        applyButton = new JButton("Apply Swap");
        applyButton.setEnabled(false);
        inputPanel.add(applyButton);
        

        inputPanel.add(new JLabel("Nutrient:"));
        inputPanel.add(nutrientComboBox);
        inputPanel.add(new JLabel("×"));
        inputPanel.add(multiplierField);
        inputPanel.add(swapButton);
        inputPanel.add(compareButton);

        add(inputPanel, BorderLayout.NORTH);

        // --- Result Display ---
        resultArea = new JTextArea(12, 40);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // --- Actions ---
        swapButton.addActionListener(this::handleSwap);
        compareButton.addActionListener(this::showComparison);
        
        applyButton.addActionListener(this::applySwapToDatabase);
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
        swappedMeal = MealSwapSearch.suggestSubstitute(originalMeal, nutrientId, multiplier);

        resultArea.setText("");
        if (swappedMeal == null) {
            resultArea.setText("No suitable substitute found.");
            compareButton.setEnabled(false);
        } else {
            resultArea.append("Suggested Meal:\n");
            for (MealFood mf : swappedMeal.getFoods()) {
                resultArea.append(String.format("- %s: %.1fg\n", mf.getName(), mf.getQuantity()));
                
            }
            resultArea.append(String.format("\nTotal Calories: %.1f kcal", swappedMeal.getCalories()));
            compareButton.setEnabled(true);
            applyButton.setEnabled(true);
        }
    }

    private void showComparison(ActionEvent e) {
        if (originalMeal == null || swappedMeal == null) {
            JOptionPane.showMessageDialog(this, "Both meals must be available.");
            return;
        }

        JFrame frame = new JFrame("Meal Comparison");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(new MealComparisonTab(originalMeal, swappedMeal));
        frame.pack();
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
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
    
    private void applySwapToDatabase(ActionEvent e) {
        if (swappedMeal == null) {
            JOptionPane.showMessageDialog(this, "No substituted meal to apply.");
            return;
        }

        int mealId = originalMeal.getID();
        swappedMeal.setID(mealId);
        

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to replace this meal?",
            "Confirm Swap",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                MealDAO.deleteMealFoods(mealId,username);
                MealDAO.insertMealFoods(swappedMeal,username);
                JOptionPane.showMessageDialog(this, "Meal successfully updated.");
                applyButton.setEnabled(false); // Optional: disable after use
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating meal.");
            }
        }
    }
}
