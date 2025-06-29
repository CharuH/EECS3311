package app;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class MealViewerPanel extends JPanel {
    private JDateChooser dateChooser;
    private JTextArea mealSummaryArea;
    private JButton loadButton;

    public MealViewerPanel() {
        this.setLayout(new BorderLayout());

        // Date Picker part
        JPanel topPanel = new JPanel(new FlowLayout());
        dateChooser = new JDateChooser();
        loadButton = new JButton("Load Meals");
        topPanel.add(new JLabel("Select Date:"));
        topPanel.add(dateChooser);
        topPanel.add(loadButton);
        this.add(topPanel, BorderLayout.NORTH);

        // Meal Summary only displays mealtype and calories currently
        mealSummaryArea = new JTextArea(15, 40);
        mealSummaryArea.setEditable(false);
        this.add(new JScrollPane(mealSummaryArea), BorderLayout.CENTER);

        // Load meals
        loadButton.addActionListener(e -> loadMeals());
    }

    private void loadMeals() {
        Date selectedDate = dateChooser.getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Please select a date.");
            return;
        }

        LocalDate localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        List<Meal> meals = MealDAO.getMealsByDate(localDate);

        mealSummaryArea.setText("");
        if (meals.isEmpty()) {
            mealSummaryArea.setText("No meals recorded for this date.");
            return;
        }

        for (Meal meal : meals) {
            double totalCalories = meal.getCalories();
            mealSummaryArea.append(meal.getType() + " - " + totalCalories + " kcal\n");
        }
    }
}
