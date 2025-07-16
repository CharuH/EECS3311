package app;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.toedter.calendar.JDateChooser;

import com.toedter.calendar.JDateChooser;

public class MealSwapViewerPanel extends JPanel {
    private JDateChooser dateChooser;
    private JButton loadButton, openSwapButton;
    private DefaultListModel<Meal> mealListModel;
    private JList<Meal> mealList;
    

    public MealSwapViewerPanel() {
        this.setLayout(new BorderLayout());

        // Top panel with date chooser and buttons
        JPanel topPanel = new JPanel(new FlowLayout());
        dateChooser = new JDateChooser();
        loadButton = new JButton("Load Meals");
        openSwapButton = new JButton("Modify Meal");
        openSwapButton.setEnabled(false);

        topPanel.add(new JLabel("Select Date:"));
        topPanel.add(dateChooser);
        topPanel.add(loadButton);
        topPanel.add(openSwapButton);
        this.add(topPanel, BorderLayout.NORTH);

        // Center panel with meal list
        mealListModel = new DefaultListModel<>();
        mealList = new JList<>(mealListModel);
        mealList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mealList.setVisibleRowCount(10);
        mealList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Meal meal) {
                    label.setText(meal.getType() + " - " + meal.getCalories() + " kcal");
                }
                return label;
            }
        });
        this.add(new JScrollPane(mealList), BorderLayout.CENTER);

        // Action listeners
        loadButton.addActionListener(e -> loadMeals());
        openSwapButton.addActionListener(e -> openMealSwapper());

        mealList.addListSelectionListener(e -> openSwapButton.setEnabled(!mealList.isSelectionEmpty()));
    }

    private void loadMeals() {
        Date selectedDate = dateChooser.getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Please select a date.");
            return;
        }

        LocalDate localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        List<Meal> meals = MealDAO.getMealsByDate(localDate);

        mealListModel.clear();
        if (meals.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No meals recorded for this date.");
        } else {
            for (Meal meal : meals) {
                mealListModel.addElement(meal);
            }
        }
    }

    private void openMealSwapper() {
        Meal selected = mealList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Select a meal first.");
            return;
        }

        JFrame swapFrame = new JFrame("Meal Modifier");
        swapFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        swapFrame.setContentPane(new MealSwapControlPanel(selected));
        swapFrame.pack();
        swapFrame.setLocationRelativeTo(this);
        swapFrame.setVisible(true);
    }
}
