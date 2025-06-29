package app;

import javax.swing.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Load food list from DB (once)
        List<Food> allFoods = getAllMealFoodNames.getAllFoods();

        // Create the main application
        JFrame frame = new JFrame("Diet Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Switch between MealBuilder and Viewer
        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Log Meal", new MealBuilderPanel(allFoods));
        tabs.add("View Meals", new MealViewerPanel());

        frame.setContentPane(tabs);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
