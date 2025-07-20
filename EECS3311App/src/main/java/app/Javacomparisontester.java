package app;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Javacomparisontester {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Try to load two meals from today's date
            

            List<MealFood> foods= new ArrayList<MealFood>();
            MealFood food1 =MealFoodDAO.getMealFood(133, 100);
            MealFood food2 =MealFoodDAO.getMealFood(1223, 80);
            MealFood food3 =MealFoodDAO.getMealFood(1142, 100);
            foods.add(food1);
            foods.add(food2);
            foods.add(food3);
            LocalDate date = LocalDate.now();
            Meal original= new Meal(100,date,MealType.BREAKFAST,foods );
            Meal modified = MealSwapSearch.suggestSubstitute(original, NutrientConstants.PROTEIN, 1.1);

            // Open comparison window
            JFrame frame = new JFrame("Meal Comparison Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new MealComparisonTab(original, modified));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
