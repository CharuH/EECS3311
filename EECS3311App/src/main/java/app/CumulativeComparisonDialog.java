package app;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class CumulativeComparisonDialog {

    public static void showCumulativeComparisonDialog(
            Nutrition original, Nutrition swapped, int mealCount,
            Runnable onApplySwaps) {

        JDialog dialog = new JDialog((Frame) null, "Cumulative Nutrition Comparison", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(650, 420);
        dialog.setLocationRelativeTo(null);

        String[] columns = {"Nutrient", "Original Total", "Swapped Total", "% Change"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        Set<Integer> allKeys = new HashSet<>();
        allKeys.addAll(original.getAll().keySet());
        allKeys.addAll(swapped.getAll().keySet());

        List<Integer> sortedKeys = new ArrayList<>(allKeys);
        sortedKeys.sort(Comparator.comparing(id -> NutritionDAO.getNutrientNameByID(id)));

        for (int id : sortedKeys) {
            String name = NutritionDAO.getNutrientNameByID(id);
            String unit = NutritionDAO.getNutrientUnitByID(id);
            double originalVal = original.getNutrient(id);
            double swappedVal = swapped.getNutrient(id);

            if (originalVal == 0 && swappedVal == 0) continue;

            double pctChange = originalVal == 0 ? 100.0 : ((swappedVal - originalVal) / originalVal) * 100.0;

            model.addRow(new Object[]{
                    name + " (" + unit + ")",
                    String.format("%.2f", originalVal),
                    String.format("%.2f", swappedVal),
                    String.format("%.1f%%", pctChange)
            });
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);

        // Color column
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String text = value.toString().replace("%", "");
                try {
                    double pct = Double.parseDouble(text);
                    if (pct > 0) c.setForeground(new Color(0, 153, 0)); // green
                    else if (pct < 0) c.setForeground(Color.RED);      // red
                    else c.setForeground(Color.DARK_GRAY);
                } catch (NumberFormatException ex) {
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });

        dialog.setLayout(new BorderLayout());
        dialog.add(new JLabel("Impact of swapping " + mealCount + " meals:"), BorderLayout.NORTH);
        dialog.add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JButton applyBtn = new JButton("Apply Swaps");
        JButton closeBtn = new JButton("Cancel");

        applyBtn.addActionListener(e -> {
            if (onApplySwaps != null) {
                onApplySwaps.run();
            }
            dialog.dispose();
        });

        closeBtn.addActionListener(e -> dialog.dispose());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(applyBtn);
        bottom.add(closeBtn);

        dialog.add(bottom, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}