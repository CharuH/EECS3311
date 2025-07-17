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
import java.util.Set;
import java.util.HashSet;
import java.time.LocalDate;

public class MealComparisonPanel extends JPanel {
    private List<Food> allFoods;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private Meal originalMeal;
    private Meal modifiedMeal;
    private String currentView = "meal"; // "meal" or "nutrients"
    private Set<Integer> replacedFoodIds; // Track which foods were replaced

    // Colors for visual indicators
    private static final Color ORIGINAL_COLOR = new Color(239, 68, 68); // Red
    private static final Color MODIFIED_COLOR = new Color(34, 197, 94); // Green
    private static final Color REPLACED_COLOR = new Color(220, 252, 231); // Light green
    private static final Color IMPROVEMENT_COLOR = new Color(34, 197, 94); // Green
    private static final Color DECLINE_COLOR = new Color(239, 68, 68); // Red

    public MealComparisonPanel(List<Food> allFoods) {
        this.allFoods = allFoods;
        this.replacedFoodIds = new HashSet<>();
        initializeData();
        setupUI();
    }

    private void initializeData() {
        // Initialize with sample meals using your existing structure
        createSampleMeals();
    }

    private void createSampleMeals() {
        // Create sample original meal
        originalMeal = new Meal(1, LocalDate.now(), MealType.BREAKFAST);

        // Add sample foods to original meal (you'll need to create these MealFood objects)
        originalMeal.addFood(new MealFood(1, 60.0, "White Bread Toast")); // 60g
        originalMeal.addFood(new MealFood(2, 10.0, "Butter")); // 10g
        originalMeal.addFood(new MealFood(3, 200.0, "Regular Milk")); // 200ml

        // Create sample modified meal
        modifiedMeal = new Meal(2, LocalDate.now(), MealType.BREAKFAST);

        // Add improved foods to modified meal
        modifiedMeal.addFood(new MealFood(4, 60.0, "Whole Wheat Bread Toast")); // 60g
        modifiedMeal.addFood(new MealFood(5, 15.0, "Avocado Spread")); // 15g
        modifiedMeal.addFood(new MealFood(6, 200.0, "Almond Milk")); // 200ml

        // Track which foods were replaced (by food ID)
        replacedFoodIds.add(4); // Whole wheat bread replaces white bread
        replacedFoodIds.add(5); // Avocado replaces butter
        replacedFoodIds.add(6); // Almond milk replaces regular milk
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Create main panel with toggle and content
        JPanel mainPanel = new JPanel(new BorderLayout());

        // View toggle buttons
        JPanel togglePanel = createTogglePanel();
        mainPanel.add(togglePanel, BorderLayout.NORTH);

        // Main content with CardLayout
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);

        mainContentPanel.add(createMealComparisonView(), "meal");
        mainContentPanel.add(createNutrientAnalysisView(), "nutrients");

        mainPanel.add(mainContentPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
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
        JPanel originalPanel = createMealPanel("Original " + getMealTypeString(originalMeal.getType()),
                originalMeal, ORIGINAL_COLOR, false);

        // Modified meal panel
        JPanel modifiedPanel = createMealPanel(getMealTypeString(modifiedMeal.getType()) + " (Optimized)",
                modifiedMeal, MODIFIED_COLOR, true);

        mealPanel.add(originalPanel);
        mealPanel.add(modifiedPanel);

        return mealPanel;
    }

    private String getMealTypeString(MealType type) {
        if (type == null) return "Meal";
        return type.toString().charAt(0) + type.toString().substring(1).toLowerCase();
    }

    private JPanel createMealPanel(String title, Meal meal, Color titleColor, boolean showReplacements) {
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

        // Use the existing getCalories() method from Meal class
        double totalCalories = meal.getCalories();
        double totalFiber = calculateTotalFiber(meal);
        double totalProtein = calculateTotalProtein(meal);

        // Display each food in the meal using getFoods() method
        for (MealFood mealFood : meal.getFoods()) {
            JPanel mealItemPanel = createMealFoodPanel(mealFood, showReplacements);
            mealsPanel.add(mealItemPanel);
            mealsPanel.add(Box.createVerticalStrut(5));
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

    // Helper methods to calculate nutrients from MealFood
    private double calculateTotalFiber(Meal meal) {
        double total = 0.0;
        for (MealFood mealFood : meal.getFoods()) {
            // Get fiber data from CalorieDAO or similar data source
            // For now, using placeholder values - you'll need to implement this
            total += getFiberFromMealFood(mealFood);
        }
        return total;
    }

    private double calculateTotalProtein(Meal meal) {
        double total = 0.0;
        for (MealFood mealFood : meal.getFoods()) {
            // Get protein data from CalorieDAO or similar data source
            total += getProteinFromMealFood(mealFood);
        }
        return total;
    }

    private JPanel createMealFoodPanel(MealFood mealFood, boolean showReplacements) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Check if this food was replaced using the foodId from MealFood
        boolean isReplaced = checkIfReplaced(mealFood, showReplacements);

        if (showReplacements && isReplaced) {
            itemPanel.setBackground(REPLACED_COLOR);
            itemPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(MODIFIED_COLOR, 2),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
        } else {
            itemPanel.setBackground(new Color(249, 250, 251)); // Light gray
        }

        // Get food name using getName() method from Food class
        String foodName = getFoodDisplayName(mealFood);
        // Use existing returnCalories() method from MealFood
        double calories = mealFood.returnCalories();
        double fiber = getFiberFromMealFood(mealFood);
        double protein = getProteinFromMealFood(mealFood);

        JLabel nameLabel = new JLabel(foodName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        if (showReplacements && isReplaced) {
            nameLabel.setForeground(new Color(21, 128, 61)); // Dark green
        }

        JLabel detailsLabel = new JLabel(String.format("%.0f kcal • %.1fg fiber • %.1fg protein",
                calories, fiber, protein));
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        detailsLabel.setForeground(Color.GRAY);

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(nameLabel, BorderLayout.NORTH);
        textPanel.add(detailsLabel, BorderLayout.SOUTH);

        itemPanel.add(textPanel, BorderLayout.CENTER);

        if (showReplacements && isReplaced) {
            JLabel checkLabel = new JLabel("✓");
            checkLabel.setForeground(MODIFIED_COLOR);
            checkLabel.setFont(new Font("Arial", Font.BOLD, 14));
            itemPanel.add(checkLabel, BorderLayout.EAST);

            // Add tooltip functionality
            itemPanel.setToolTipText(getMealFoodTooltipText(mealFood));
        }

        return itemPanel;
    }

    // Helper methods integrated with your existing classes
    private boolean checkIfReplaced(MealFood mealFood, boolean showReplacements) {
        // Use the foodId from MealFood to check if it was replaced
        return showReplacements && replacedFoodIds.contains(mealFood.getFoodId());
    }

    private String getFoodDisplayName(MealFood mealFood) {
        // Use getName() method from Food class and getQuantity() from MealFood
        return mealFood.getName() + " (" + String.format("%.0f", mealFood.getQuantity()) + "g)";
    }

    private double getFiberFromMealFood(MealFood mealFood) {
        // You'll need to extend CalorieDAO or create similar method for fiber
        // For now, using sample values based on food ID
        double fiberPer100g = getFiberByFoodId(mealFood.getFoodId());
        return (fiberPer100g / 100.0) * mealFood.getQuantity();
    }

    private double getProteinFromMealFood(MealFood mealFood) {
        // Similar to fiber, you'll need to extend CalorieDAO or create similar method
        double proteinPer100g = getProteinByFoodId(mealFood.getFoodId());
        return (proteinPer100g / 100.0) * mealFood.getQuantity();
    }

    // Sample methods - you'll need to implement these similar to CalorieDAO
    private double getFiberByFoodId(int foodId) {
        // Sample fiber values per 100g
        switch (foodId) {
            case 1: return 2.1; // White bread
            case 2: return 0.0; // Butter
            case 3: return 0.0; // Regular milk
            case 4: return 4.2; // Whole wheat bread
            case 5: return 6.7; // Avocado
            case 6: return 0.6; // Almond milk
            default: return 0.0;
        }
    }

    private double getProteinByFoodId(int foodId) {
        // Sample protein values per 100g
        switch (foodId) {
            case 1: return 8.7; // White bread
            case 2: return 0.1; // Butter
            case 3: return 3.2; // Regular milk
            case 4: return 10.2; // Whole wheat bread
            case 5: return 2.0; // Avocado
            case 6: return 0.5; // Almond milk
            default: return 0.0;
        }
    }

    private String getMealFoodTooltipText(MealFood mealFood) {
        double calories = mealFood.returnCalories();
        double fiber = getFiberFromMealFood(mealFood);
        double protein = getProteinFromMealFood(mealFood);

        return String.format("<html>%s (%.0fg)<br/>Calories: %.0f kcal<br/>Fiber: %.1fg<br/>Protein: %.1fg<br/>✓ Healthier alternative</html>",
                mealFood.getName(), mealFood.getQuantity(), calories, fiber, protein);
    }

    private JPanel createNutrientAnalysisView() {
        JPanel nutrientPanel = new JPanel(new BorderLayout());
        nutrientPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Nutritional Changes", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Calculate totals using existing Meal methods
        NutrientTotals originalTotals = calculateTotals(originalMeal);
        NutrientTotals modifiedTotals = calculateTotals(modifiedMeal);

        // Create nutrient comparison grid
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 10, 10));

        addNutrientCard(gridPanel, "Calories", originalTotals.calories, modifiedTotals.calories, "kcal", "reduce");
        addNutrientCard(gridPanel, "Fiber", originalTotals.fiber, modifiedTotals.fiber, "g", "increase");
        addNutrientCard(gridPanel, "Protein", originalTotals.protein, modifiedTotals.protein, "g", "maintain");

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
        double changePercent = original != 0 ? (change / original) * 100 : 0;

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

        content.add(new JLabel(String.format("✓ Calories: %.0f kcal (%s)",
                Math.abs(calorieChange), calorieChange < 0 ? "reduced" : "increased")));
        content.add(new JLabel(String.format("✓ Fiber: +%.1fg (increased)", fiberChange)));
        content.add(new JLabel(String.format("✓ Protein: %+.1fg", proteinChange)));

        summaryPanel.add(content, BorderLayout.CENTER);
        return summaryPanel;
    }

    private NutrientTotals calculateTotals(Meal meal) {
        NutrientTotals totals = new NutrientTotals();

        // Use the existing getCalories() method from Meal class
        totals.calories = meal.getCalories();

        // Calculate other nutrients by iterating through MealFood items
        for (MealFood mealFood : meal.getFoods()) {
            totals.fiber += getFiberFromMealFood(mealFood);
            totals.protein += getProteinFromMealFood(mealFood);
        }

        return totals;
    }

    // Method to set meals for comparison (called from other parts of your application)
    public void setMealsForComparison(Meal original, Meal modified, Set<Integer> replacedFoodIds) {
        this.originalMeal = original;
        this.modifiedMeal = modified;
        this.replacedFoodIds = replacedFoodIds != null ? replacedFoodIds : new HashSet<>();

        // Refresh the UI to show the new meals
        refreshUI();
    }

    // Overloaded method for backward compatibility
    public void setMealsForComparison(Meal original, Meal modified) {
        setMealsForComparison(original, modified, null);
    }

    private void refreshUI() {
        // Remove existing content and recreate with new meal data
        mainContentPanel.removeAll();
        mainContentPanel.add(createMealComparisonView(), "meal");
        mainContentPanel.add(createNutrientAnalysisView(), "nutrients");

        // Show the current view
        cardLayout.show(mainContentPanel, currentView);

        // Refresh the display
        revalidate();
        repaint();
    }

    // Helper classes
    private static class NutrientTotals {
        double calories = 0, fiber = 0, protein = 0;
    }
}

