package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class PerformanceCellRenderer extends DefaultTableCellRenderer {

    private static final Color LIGHT_RED = new Color(255, 220, 220);
    private static final Color LIGHT_GREEN = new Color(220, 255, 220);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                 boolean isSelected, boolean hasFocus,
                                                 int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Reset to default
        c.setBackground(table.getBackground());
        c.setFont(table.getFont());

        if (value instanceof String) {
            String stringValue = (String) value;
            stringValue = stringValue.replace("%", "").trim();

            try {
                double numericValue = Double.parseDouble(stringValue);
                
                // Set alignment for all numerical cells
                setHorizontalAlignment(SwingConstants.RIGHT);

                if (numericValue > 10) {
                    c.setBackground(LIGHT_GREEN);
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else if (numericValue > 5) {
                    c.setBackground(LIGHT_GREEN);
                } else if (numericValue < 0) {
                    c.setBackground(LIGHT_RED);
                }

            } catch (NumberFormatException e) {
                // Not a number, reset to default left alignment
                setHorizontalAlignment(SwingConstants.LEFT);
            }
        }
        
        // Handle selection color
        if (isSelected) {
            c.setBackground(table.getSelectionBackground());
            c.setForeground(table.getSelectionForeground());
        } else {
             c.setForeground(table.getForeground());
        }


        return c;
    }
}
