package app;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

public class MealBuilderPanel extends JPanel {
    private JDateChooser dateChooser;
    private JComboBox<MealType> mealTypeBox;
    private JTextField foodSearchField;
    private JList<String> foodList;
    private DefaultListModel<String> listModel;
    private JTextField quantityField;
    private JTextArea currentMealArea;
    private JButton addButton, saveButton;

    private List<Food> allFoods; 
    private Meal currentMeal;

    public MealBuilderPanel(List<Food> foodListFromDB) {
        this.allFoods = foodListFromDB;
        this.setLayout(new BorderLayout());

        
        JPanel topPanel = new JPanel(new FlowLayout());
        dateChooser = new JDateChooser();
        mealTypeBox = new JComboBox<>(MealType.values());
        topPanel.add(new JLabel("Date:"));
        topPanel.add(dateChooser);
        topPanel.add(new JLabel("Meal Type:"));
        topPanel.add(mealTypeBox);
        this.add(topPanel, BorderLayout.NORTH);

        
        JPanel centerPanel = new JPanel(new BorderLayout());

        JPanel foodInputPanel = new JPanel(new FlowLayout());
        foodSearchField = new JTextField(20);
        quantityField = new JTextField(5);
        addButton = new JButton("Add");

        foodInputPanel.add(new JLabel("Search Food:"));
        foodInputPanel.add(foodSearchField);
        foodInputPanel.add(new JLabel("Quantity (g):"));
        foodInputPanel.add(quantityField);
        foodInputPanel.add(addButton);

        listModel = new DefaultListModel<>();
        foodList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(foodList);
        scrollPane.setPreferredSize(new Dimension(300, 100));

        centerPanel.add(foodInputPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        this.add(centerPanel, BorderLayout.CENTER);

        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        currentMealArea = new JTextArea(8, 40);
        currentMealArea.setEditable(false);
        saveButton = new JButton("Save Meal");
        bottomPanel.add(new JScrollPane(currentMealArea), BorderLayout.CENTER);
        bottomPanel.add(saveButton, BorderLayout.SOUTH);
        this.add(bottomPanel, BorderLayout.SOUTH);

        
        foodSearchField.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void update() {
                String input = foodSearchField.getText().toLowerCase();
                listModel.clear();
                allFoods.stream()
                        .filter(f -> f.getName().toLowerCase().contains(input))
                        .limit(30)
                        .forEach(f -> listModel.addElement(f.getFoodId() + ": " + f.getName()));
            }
        });

        addButton.addActionListener(e -> addSelectedFood());
        saveButton.addActionListener(e -> saveMeal());
    }

    private void addSelectedFood() {
        String selected = foodList.getSelectedValue();
        if (selected == null || quantityField.getText().isEmpty()) return;
        try {
            int foodId = Integer.parseInt(selected.split(":")[0]);
            double qty = Double.parseDouble(quantityField.getText());

            if (currentMeal == null) {
                Date date = dateChooser.getDate();
                if (date == null) return;
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                MealType type = (MealType) mealTypeBox.getSelectedItem();
                currentMeal = new Meal(0, localDate, type);
            }

            currentMeal.addFood(MealFoodDAO.getMealFood(foodId, qty));
            currentMealArea.append(selected + " - " + qty + "g\n");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity format.");
        }
    }
    
    private void saveMeal() {
        Date date = dateChooser.getDate();
        if (date == null) {
            JOptionPane.showMessageDialog(this, "Please select a date.");
            return;
        }

        MealType type = (MealType) mealTypeBox.getSelectedItem();
        if (type == null) {
            JOptionPane.showMessageDialog(this, "Please select a meal type.");
            return;
        }

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        
        if (currentMeal == null) {
            currentMeal = new Meal(0, localDate, type);
        }

       
        currentMeal.setDate(localDate);
        currentMeal.setType(type);

        if (currentMeal.getFoods().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No foods have been added to the meal.");
            return;
        }

        int id = MealDAO.saveMeal(currentMeal);
        JOptionPane.showMessageDialog(this, "Meal saved with ID: " + id);

        // Reset
        currentMeal = null;
        currentMealArea.setText("");
        quantityField.setText("");
    }


    
    abstract class DocumentAdapter implements javax.swing.event.DocumentListener {
        public abstract void update();
        public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
        public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
        public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
    }
}
