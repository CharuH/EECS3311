package app;

import javax.swing.*;

import accCreate.MainUI;

import java.util.List;

public class Log extends JTabbedPane {
    public Log(MainUI main) {
        // Load food list from DB (once)
        List<Food> allFoods = MealFoodDAO.getAllFoods();

        // Switch between MealBuilder and Viewer
        add("Log Meal", new MealBuilderPanel(allFoods));
        add("View Meals", new MealViewerPanel());
    }
}
