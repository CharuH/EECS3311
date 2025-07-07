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
    private JButton loadButton, viewBreakdownButton;
    private DefaultListModel<Meal> mealListModel;
    private JList<Meal> mealList;

    public MealViewerPanel() {
        this.setLayout(new BorderLayout());

        // Top panel with date chooser and buttons
        JPanel topPanel = new JPanel(new FlowLayout());
        dateChooser = new JDateChooser();
        loadButton = new JButton("Load Meals");
        viewBreakdownButton = new JButton("View Breakdown");
        viewBreakdownButton.setEnabled(false); // disable until meal is selected

        topPanel.add(new JLabel("Select Date:"));
        topPanel.add(dateChooser);
        topPanel.add(loadButton);
        topPanel.add(viewBreakdownButton);
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
        viewBreakdownButton.addActionListener(e -> showBreakdown());

        mealList.addListSelectionListener(e -> {
            viewBreakdownButton.setEnabled(!mealList.isSelectionEmpty());
        });
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

    private void showBreakdown() {
        Meal selected = mealList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a meal to view its breakdown.");
            return;
        }

        JFrame breakdownFrame = new JFrame("Meal Breakdown");
        breakdownFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        breakdownFrame.setContentPane(new MealBreakdownTab(selected));
        breakdownFrame.pack();
        breakdownFrame.setLocationRelativeTo(this);
        breakdownFrame.setVisible(true);
    }
}