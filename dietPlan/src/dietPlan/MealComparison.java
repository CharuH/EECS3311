package dietPlan;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;

public class MealComparisonPanel extends JPanel {
    private List<Food> allFoods;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private List<MealItem> originalMeal;
    private List<MealItem> modifiedMeal;
    private String currentView = "meal"; // "meal" or "nutrients"

    // Colors for visual indicators
    private static final Color ORIGINAL_COLOR = new Color(239, 68, 68); // Red
    private static final Color MODIFIED_COLOR = new Color(34, 197, 94); // Green
    private static final Color REPLACED_COLOR = new Color(220, 252, 231); // Light green
    private static final Color IMPROVEMENT_COLOR = new Color(34, 197, 94); // Green
    private static final Color DECLINE_COLOR = new Color(239, 68, 68); // Red

    public MealComparisonPanel(List<Food> allFoods) {
        this.allFoods = allFoods;
        initializeData();
        setupUI();
    }

    private void initializeData() {
        // Sample data - replace with actual meal data from your system
        originalMeal = new ArrayList<>();
        originalMeal.add(new MealItem("White Toast (2 slices)", 160, 2.4, 6.0, 30.0, 2.0, false));
        originalMeal.add(new MealItem("Butter (1 tbsp)", 100, 0, 0.1, 0, 11.0, false));
        originalMeal.add(new MealItem("Orange Juice (1 cup)", 110, 0.2, 2.0, 26.0, 0.2, false));
        originalMeal.add(new MealItem("Scrambled Eggs (2 eggs)", 180, 0, 12.0, 2.0, 14.0, false));

        modifiedMeal = new ArrayList<>();
        modifiedMeal.add(new MealItem("Whole Grain Toast (2 slices)", 140, 6.0, 8.0, 26.0, 2.0, true));
        modifiedMeal.add(new MealItem("Avocado Spread (1/2 avocado)", 120, 5.0, 2.0, 6.0, 11.0, true));
        modifiedMeal.add(new MealItem("Fresh Orange (1 medium)", 60, 3.0, 1.2, 15.0, 0.2, true));
        modifiedMeal.add(new MealItem("Scrambled Eggs (2 eggs)", 180, 0, 12.0, 2.0, 14.0, false));
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // View toggle buttons
        JPanel togglePanel = createTogglePanel();
        add(togglePanel, BorderLayout.CENTER);

        // Main content with CardLayout
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);

        mainContentPanel.add(createMealComparisonView(), "meal");
        mainContentPanel.add(createNutrientAnalysisView(), "nutrients");

        add(mainContentPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("NutriSci: SwEATch to better!", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(37, 99, 235)); // Blue

        JLabel subtitleLabel = new JLabel("Compare your meal before and after smart food swaps", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        return headerPanel;
    }

    private JPanel createTogglePanel() {
        JPanel togglePanel = new JPanel(new FlowLayout());
        togglePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton mealViewButton = new JButton("Meal Comparison");
        JButton nutrientViewButton = new JButton("Nutrient Analysis");

        // Style buttons
        styleToggleButton(mealViewButton, true);
        styleToggleButton(nutrientViewButton, false);

        mealViewButton.addActionListener(e -> {
            currentView = "meal";
            cardLayout.show(mainContentPanel, "meal");
            styleToggleButton(mealViewButton, true);
            styleToggleButton(nutrientViewButton, false);
        });

        nutrientViewButton.addActionListener(e -> {
            currentView = "nutrients";
            cardLayout.show(mainContentPanel, "nutrients");
            styleToggleButton(nutrientViewButton, true);
            styleToggleButton(mealViewButton, false);
        });

        togglePanel.add(mealViewButton);
        togglePanel.add(nutrientViewButton);

        return togglePanel;
    }

    private void styleToggleButton(JButton button, boolean active) {
        if (active) {
            button.setBackground(new Color(37, 99, 235)); // Blue
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(Color.GRAY);
        }
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
    }

    private JPanel createMealComparisonView() {
        JPanel mealPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mealPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Original meal panel
        JPanel originalPanel = createMealPanel("Original Breakfast", originalMeal, ORIGINAL_COLOR, false);

        // Modified meal panel
        JPanel modifiedPanel = createMealPanel("Breakfast (Optimized)", modifiedMeal, MODIFIED_COLOR, true);

        mealPanel.add(originalPanel);
        mealPanel.add(modifiedPanel);

        return mealPanel;
    }

    private JPanel createMealPanel(String title, List<MealItem> meals, Color titleColor, boolean showReplacements) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createRaisedBorder());
        panel.setBackground(Color.WHITE);

        // Title with colored indicator
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);

        JLabel colorIndicator = new JLabel("●");
        colorIndicator.setForeground(titleColor);
        colorIndicator.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        titlePanel.add(colorIndicator);
        titlePanel.add(titleLabel);

        // Meals list
        JPanel mealsPanel = new JPanel();
        mealsPanel.setLayout(new BoxLayout(mealsPanel, BoxLayout.Y_AXIS));
        mealsPanel.setBackground(Color.WHITE);

        double totalCalories = 0, totalFiber = 0, totalProtein = 0;

        for (MealItem meal : meals) {
            JPanel mealItemPanel = createMealItemPanel(meal, showReplacements);
            mealsPanel.add(mealItemPanel);
            mealsPanel.add(Box.createVerticalStrut(5));

            totalCalories += meal.calories;
            totalFiber += meal.fiber;
            totalProtein += meal.protein;
        }

        // Totals
        JPanel totalsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalsPanel.setBackground(Color.WHITE);
        totalsPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JLabel totalsLabel = new JLabel(String.format("Total: %.0f kcal • %.1fg fiber • %.1fg protein",
                totalCalories, totalFiber, totalProtein));
        totalsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        totalsPanel.add(totalsLabel);

        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(mealsPanel, BorderLayout.CENTER);
        panel.add(totalsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMealItemPanel(MealItem meal, boolean showReplacements) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        if (showReplacements && meal.isReplaced) {
            itemPanel.setBackground(REPLACED_COLOR);
            itemPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(MODIFIED_COLOR, 2),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
        } else {
            itemPanel.setBackground(new Color(249, 250, 251)); // Light gray
        }

        JLabel nameLabel = new JLabel(meal.name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        if (showReplacements && meal.isReplaced) {
            nameLabel.setForeground(new Color(21, 128, 61)); // Dark green
        }

        JLabel detailsLabel = new JLabel(String.format("%.0f kcal • %.1fg fiber • %.1fg protein",
                meal.calories, meal.fiber, meal.protein));
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        detailsLabel.setForeground(Color.GRAY);

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(nameLabel, BorderLayout.NORTH);
        textPanel.add(detailsLabel, BorderLayout.SOUTH);

        itemPanel.add(textPanel, BorderLayout.CENTER);

        if (showReplacements && meal.isReplaced) {
            JLabel checkLabel = new JLabel("✓");
            checkLabel.setForeground(MODIFIED_COLOR);
            checkLabel.setFont(new Font("Arial", Font.BOLD, 14));
            itemPanel.add(checkLabel, BorderLayout.EAST);

            // Add tooltip functionality
            itemPanel.setToolTipText(getTooltipText(meal));
        }

        return itemPanel;
    }

    private String getTooltipText(MealItem meal) {
        // Find corresponding original meal item for comparison
        // This is simplified - you'd implement proper matching logic
        return "Nutritional improvements: Higher fiber, better nutrients";
    }

    private JPanel createNutrientAnalysisView() {
        JPanel nutrientPanel = new JPanel(new BorderLayout());
        nutrientPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Nutritional Changes", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Calculate totals
        NutrientTotals originalTotals = calculateTotals(originalMeal);
        NutrientTotals modifiedTotals = calculateTotals(modifiedMeal);

        // Create nutrient comparison grid
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 10, 10));

        addNutrientCard(gridPanel, "Calories", originalTotals.calories, modifiedTotals.calories, "kcal", "reduce");
        addNutrientCard(gridPanel, "Fiber", originalTotals.fiber, modifiedTotals.fiber, "g", "increase");
        addNutrientCard(gridPanel, "Protein", originalTotals.protein, modifiedTotals.protein, "g", "maintain");
        addNutrientCard(gridPanel, "Carbohydrates", originalTotals.carbs, modifiedTotals.carbs, "g", "reduce");
        addNutrientCard(gridPanel, "Fat", originalTotals.fat, modifiedTotals.fat, "g", "maintain");

        // Summary panel
        JPanel summaryPanel = createSummaryPanel(originalTotals, modifiedTotals);

        nutrientPanel.add(titleLabel, BorderLayout.NORTH);
        nutrientPanel.add(gridPanel, BorderLayout.CENTER);
        nutrientPanel.add(summaryPanel, BorderLayout.SOUTH);

        return nutrientPanel;
    }

    private void addNutrientCard(JPanel parent, String name, double original, double modified, String unit, String goal) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createRaisedBorder(), name, TitledBorder.LEFT, TitledBorder.TOP));
        card.setBackground(Color.WHITE);

        JPanel content = new JPanel(new GridLayout(4, 1));
        content.setBackground(Color.WHITE);

        content.add(new JLabel(String.format("Before: %.1f %s", original, unit)));
        content.add(new JLabel(String.format("After: %.1f %s", modified, unit)));

        double change = modified - original;
        double changePercent = (change / original) * 100;

        JLabel changeLabel = new JLabel(String.format("Change: %+.1f %s", change, unit));
        JLabel percentLabel = new JLabel(String.format("(%.1f%%)", changePercent));

        // Color based on goal
        Color changeColor = getChangeColor(change, goal);
        changeLabel.setForeground(changeColor);
        percentLabel.setForeground(changeColor);

        content.add(changeLabel);
        content.add(percentLabel);

        card.add(content, BorderLayout.CENTER);
        parent.add(card);
    }

    private Color getChangeColor(double change, String goal) {
        if (Math.abs(change) < 0.1) return Color.GRAY;

        switch (goal) {
            case "increase":
                return change > 0 ? IMPROVEMENT_COLOR : DECLINE_COLOR;
            case "reduce":
                return change < 0 ? IMPROVEMENT_COLOR : DECLINE_COLOR;
            default:
                return Color.BLUE;
        }
    }

    private JPanel createSummaryPanel(NutrientTotals original, NutrientTotals modified) {
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Swap Summary"));
        summaryPanel.setBackground(new Color(239, 246, 255)); // Light blue

        JPanel content = new JPanel(new GridLayout(3, 1));
        content.setBackground(new Color(239, 246, 255));

        double calorieChange = original.calories - modified.calories;
        double fiberChange = modified.fiber - original.fiber;
        double proteinChange = modified.protein - original.protein;

        content.add(new JLabel(String.format("✓ Reduced calories by %.0f kcal", calorieChange)));
        content.add(new JLabel(String.format("✓ Increased fiber by %.1fg", fiberChange)));
        content.add(new JLabel(String.format("✓ Maintained protein levels (+%.1fg)", proteinChange)));

        summaryPanel.add(content, BorderLayout.CENTER);
        return summaryPanel;
    }

    private NutrientTotals calculateTotals(List<MealItem> meals) {
        NutrientTotals totals = new NutrientTotals();
        for (MealItem meal : meals) {
            totals.calories += meal.calories;
            totals.fiber += meal.fiber;
            totals.protein += meal.protein;
            totals.carbs += meal.carbs;
            totals.fat += meal.fat;
        }
        return totals;
    }

    // Helper classes
    private static class MealItem {
        String name;
        double calories, fiber, protein, carbs, fat;
        boolean isReplaced;

        MealItem(String name, double calories, double fiber, double protein, double carbs, double fat, boolean isReplaced) {
            this.name = name;
            this.calories = calories;
            this.fiber = fiber;
            this.protein = protein;
            this.carbs = carbs;
            this.fat = fat;
            this.isReplaced = isReplaced;
        }
    }

    private static class NutrientTotals {
        double calories = 0, fiber = 0, protein = 0, carbs = 0, fat = 0;
    }
}