package app;

import javax.swing.*;

import accCreate.MainUI;

import java.util.List;

public class Log extends JTabbedPane{
    public Log(MainUI main) {
        // Load food list from DB (once)
        List<Food> allFoods = MealFoodDAO.getAllFoods();

        String username = main.getUser().getUsername();
        add("Log Meal", new MealBuilderPanel(allFoods, username));
        add("View Meals", new MealViewerPanel());
        add("Adjust Meals", new MealSwapViewerPanel());

    }
}
