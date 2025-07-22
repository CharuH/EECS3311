package app;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class MealSwapControlPanel extends JPanel {
    private final Meal originalMeal;
    private JComboBox<ComboItem> nutrientComboBox;
    private JTextField multiplierField;
    private JButton swapButton, compareButton, applyButton, bulkCompareButton;
    private JTextArea resultArea;
    private Meal swappedMeal;
    private String username;

    public MealSwapControlPanel(Meal meal, String username) {
        this.username = username;
        this.originalMeal = meal;
        setLayout(new BorderLayout());

        //Input Panel
        JPanel inputPanel = new JPanel(new FlowLayout());

        nutrientComboBox = new JComboBox<>();
        populateNutrientDropdown();

        multiplierField = new JTextField("1.10", 5);
        swapButton = new JButton("Find Substitute");
        compareButton = new JButton("Compare Meals");
        compareButton.setEnabled(false);
        applyButton = new JButton("Apply Swap");
        applyButton.setEnabled(false);
        bulkCompareButton = new JButton("Compare Bulk Swap");
        bulkCompareButton.setEnabled(false);

        inputPanel.add(applyButton);
        inputPanel.add(new JLabel("Nutrient:"));
        inputPanel.add(nutrientComboBox);
        inputPanel.add(new JLabel("×"));
        inputPanel.add(multiplierField);
        inputPanel.add(swapButton);
        inputPanel.add(compareButton);
        inputPanel.add(bulkCompareButton);
        add(inputPanel, BorderLayout.NORTH);

        //Result Display
        resultArea = new JTextArea(12, 40);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        //Actions
        swapButton.addActionListener(this::handleSwap);
        compareButton.addActionListener(this::showComparison);
        applyButton.addActionListener(this::applySwapToDatabase);
        bulkCompareButton.addActionListener(this::handleBulkCompare);
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
            bulkCompareButton.setEnabled(true);
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
                MealDAO.deleteMealFoods(mealId, username);
                MealDAO.insertMealFoods(swappedMeal, username);
                JOptionPane.showMessageDialog(this, "Meal successfully updated.");
                applyButton.setEnabled(false);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating meal.");
            }
        }
    }

    private void handleBulkCompare(ActionEvent e) {
        if (swappedMeal == null || originalMeal == null) {
            JOptionPane.showMessageDialog(this, "Swap must be available.");
            return;
        }

        JDateChooser startPicker = new JDateChooser();
        JDateChooser endPicker = new JDateChooser();
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Start Date:")); panel.add(startPicker);
        panel.add(new JLabel("End Date:")); panel.add(endPicker);

        int ok = JOptionPane.showConfirmDialog(this, panel, "Select Date Range", JOptionPane.OK_CANCEL_OPTION);
        if (ok != JOptionPane.OK_OPTION) return;

        if (startPicker.getDate() == null || endPicker.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Invalid dates.");
            return;
        }

        LocalDate start = startPicker.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endPicker.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        List<Meal> mealsInRange = MealDAO.getMealsByDates(start, end, username);
        List<Meal> matching = mealsInRange.stream()
            .filter(m -> mealsStructurallyEqual(m, originalMeal))
            .toList();

        if (matching.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No identical meals found.");
            return;
        }

        Nutrition originalTotal = new Nutrition();
        Nutrition swappedTotal = new Nutrition();
        for (Meal ignored : matching) {
            for (MealFood mf : originalMeal.getFoods()) {
                originalTotal.add(mf.getNutrition(), mf.getQuantity());
            }
            for (MealFood mf : swappedMeal.getFoods()) {
                swappedTotal.add(mf.getNutrition(), mf.getQuantity());
            }
        }

        CumulativeComparisonDialog.showCumulativeComparisonDialog(
            originalTotal, swappedTotal, matching.size(),
            () -> {
                for (Meal m : matching) {
                    try {
                        MealDAO.deleteMealFoods(m.getID(), username);
                        Meal newMeal = swappedMeal.copy();
                        newMeal.setID(m.getID());
                        MealDAO.insertMealFoods(newMeal, username);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                JOptionPane.showMessageDialog(this, "Bulk swap complete.");
            }
        );
    }

    private boolean mealsStructurallyEqual(Meal m1, Meal m2) {
        List<MealFood> f1 = m1.getFoods();
        List<MealFood> f2 = m2.getFoods();
        List<Double> q1 = m1.getQuantities();
        List<Double> q2 = m2.getQuantities();

        if (f1.size() != f2.size()) return false;
        for (int i = 0; i < f1.size(); i++) {
            if (f1.get(i).getFoodId() != f2.get(i).getFoodId()) return false;
            if (Math.abs(q1.get(i) - q2.get(i)) > 0.01) return false;
        }
        return true;
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

        public int getId() { return id; }

        public String toString() { return label; }
    }
}

