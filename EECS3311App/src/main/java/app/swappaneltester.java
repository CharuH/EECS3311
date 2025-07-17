package app;
import javax.swing.*;
import java.util.List;
public class swappaneltester {
	
	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> {
	            JFrame frame = new JFrame("Meal Swap by Date");
	            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	            frame.setContentPane(new MealSwapViewerPanel());
	            frame.pack();
	            frame.setLocationRelativeTo(null);
	            frame.setVisible(true);
	        });
	    }
	

}
