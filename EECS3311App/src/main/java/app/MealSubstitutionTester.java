package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MealSubstitutionTester extends JFrame {
    private JComboBox<String> nutrientBox;
    private JTextField multiplierField;
    private JButton runButton;
    private JPanel resultPanel;
    private Meal baseMeal;

    public MealSubstitutionTester(Meal meal) {
        super("Meal Substitution Tester");
        this.baseMeal = meal;

        setLayout(new BorderLayout());

        // --- Top Input Panel ---
        JPanel inputPanel = new JPanel(new FlowLayout());

        nutrientBox = new JComboBox<>();
        for (Integer id : NutritionDAO.getAllNutrientIDs()) {
            nutrientBox.addItem(id + " - " + NutritionDAO.getNutrientNameByID(id));
        }

        multiplierField = new JTextField("1.10", 5);
        runButton = new JButton("Suggest Swaps");

        inputPanel.add(new JLabel("Target Nutrient:"));
        inputPanel.add(nutrientBox);
        inputPanel.add(new JLabel("Multiplier:"));
        inputPanel.add(multiplierField);
        inputPanel.add(runButton);

        add(inputPanel, BorderLayout.NORTH);

        // --- Result Display Panel ---
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        add(new JScrollPane(resultPanel), BorderLayout.CENTER);

        // --- Button Action ---
        runButton.addActionListener(this::handleSuggest);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void handleSuggest(ActionEvent e) {
        resultPanel.removeAll();
        resultPanel.add(new JLabel("Searching for substitutes..."));
        resultPanel.revalidate();
        resultPanel.repaint();

        SwingWorker<List<Meal>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Meal> doInBackground() throws Exception {
                String selected = (String) nutrientBox.getSelectedItem();
                int nutrientId = Integer.parseInt(selected.split(" - ")[0]);
                double multiplier = Double.parseDouble(multiplierField.getText());

                MealSwapSearch engine = new MealSwapSearch(); // your logic class
                return engine.suggestSubstitutes(baseMeal, nutrientId, multiplier);
            }

            @Override
            protected void done() {
                resultPanel.removeAll();
                try {
                    List<Meal> candidates = get();
                    if (candidates.isEmpty()) {
                        resultPanel.add(new JLabel("No suitable substitutions found."));
                    } else {
                        for (Meal candidate : candidates) {
                            JButton btn = new JButton("View " + candidate.toString());
                            btn.addActionListener(ev -> showMealChart(candidate));
                            resultPanel.add(btn);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    resultPanel.add(new JLabel("Error: " + ex.getMessage()));
                }
                resultPanel.revalidate();
                resultPanel.repaint();
            }
        };

        worker.execute();
    }

    private void showMealChart(Meal meal) {
        JFrame chartFrame = new JFrame("Breakdown for Substitute");
        chartFrame.setContentPane(new MealBreakdownTab(meal));
        chartFrame.pack();
        chartFrame.setVisible(true);
    }
    
    
    public static void main(String args[]) {
    	
    	List<MealFood> foods= new ArrayList<MealFood>();
    	MealFood food1 =MealFoodDAO.getMealFood(133, 100);
    	MealFood food2 =MealFoodDAO.getMealFood(1223, 80);
    	MealFood food3 =MealFoodDAO.getMealFood(1142, 100);
    	foods.add(food1);
    	foods.add(food2);
    	foods.add(food3);
    	LocalDate date = LocalDate.now();
    	Meal meal= new Meal(100,date,MealType.BREAKFAST,foods );
    	
    	Meal testMeal = MealDAO.getMealById(2); // Or create a test meal
    	System.out.println(testMeal.toString());
    	new MealSubstitutionTester(testMeal);
    }
}