package view;

import model.DataPoint;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class ChartPanel extends JPanel {
    private List<DataPoint> dataPoints;

    public ChartPanel() {
        this.dataPoints = Collections.emptyList();
        setBorder(BorderFactory.createTitledBorder("投資成長圖"));
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (dataPoints == null || dataPoints.isEmpty() || dataPoints.size() < 2) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int padding = 40;
        int labelPadding = 40; // Increased padding for labels
        int bottomPadding = 50; // Extra padding for x-axis labels
        int width = getWidth();
        int height = getHeight();

        // Draw white background for the chart area
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, width - (2 * padding) - labelPadding, height - padding - bottomPadding - labelPadding);
        g2.setColor(Color.BLACK);

        // --- Y-Axis Scaling ---
        double yMin = 0; // Cost and value won't be negative
        double yMaxValue = dataPoints.stream().mapToDouble(DataPoint::getValue).max().orElse(0);
        double yMaxCost = dataPoints.stream().mapToDouble(DataPoint::getCost).max().orElse(0);
        double yMax = Math.max(yMaxValue, yMaxCost);

        // Create hatch marks and grid lines for y-axis.
        int numberYDivisions = 10;
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = width - padding;
            int y0 = height - ((i * (height - padding - bottomPadding - labelPadding)) / numberYDivisions + padding + bottomPadding);
            int y1 = y0;
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawLine(x0, y0, x1, y1); // grid line
            g2.setColor(Color.BLACK);
            String yLabel = String.format("%,.2f", yMin + (yMax - yMin) * ((i * 1.0) / numberYDivisions));
            FontMetrics metrics = g2.getFontMetrics();
            int labelWidth = metrics.stringWidth(yLabel);
            g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
        }
        
        // Draw axis lines
        g2.drawLine(padding + labelPadding, height - padding - bottomPadding, padding + labelPadding, padding); // y-axis
        g2.drawLine(padding + labelPadding, height - padding - bottomPadding, width - padding, height - padding - bottomPadding); // x-axis
        
        double xScale = ((double) width - 2 * padding - labelPadding) / (dataPoints.size() - 1);

        // --- X-Axis Labels with thinning ---
        int maxLabels = 8;
        int totalPoints = dataPoints.size();
        int step = (totalPoints <= maxLabels) ? 1 : (int) Math.ceil((double) totalPoints / maxLabels);

        for (int i = 0; i < totalPoints; i += step) {
             int x0 = (int)(i * xScale) + padding + labelPadding;
             int y0 = height - padding - bottomPadding;
             g2.setColor(Color.LIGHT_GRAY);
             g2.drawLine(x0, y0, x0, padding); // grid line
             g2.setColor(Color.BLACK);
             String xLabel = dataPoints.get(i).getDate();
             FontMetrics metrics = g2.getFontMetrics();
             int labelWidth = metrics.stringWidth(xLabel);
             g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 5);
        }
        // Ensure last label is drawn
        if ((totalPoints -1) % step != 0) {
            int i = totalPoints - 1;
            int x0 = (int)(i * xScale) + padding + labelPadding;
            int y0 = height - padding - bottomPadding;
            String xLabel = dataPoints.get(i).getDate();
            FontMetrics metrics = g2.getFontMetrics();
            int labelWidth = metrics.stringWidth(xLabel);
            g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 5);
        }


        Stroke oldStroke = g2.getStroke();
        
        // --- Draw Value Line (Blue) ---
        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(2f));
        double yScale = ((double) height - padding - bottomPadding - labelPadding) / (yMax - yMin);

        for (int i = 0; i < dataPoints.size() - 1; i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1_val = (int) ((yMax - dataPoints.get(i).getValue()) * yScale + padding);
            int x2 = (int) ((i + 1) * xScale + padding + labelPadding);
            int y2_val = (int) ((yMax - dataPoints.get(i + 1).getValue()) * yScale + padding);
            g2.drawLine(x1, y1_val, x2, y2_val);
        }

        // --- Draw Cost Line (Red) ---
        g2.setColor(Color.RED);
        for (int i = 0; i < dataPoints.size() - 1; i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1_cost = (int) ((yMax - dataPoints.get(i).getCost()) * yScale + padding);
            int x2 = (int) ((i + 1) * xScale + padding + labelPadding);
            int y2_cost = (int) ((yMax - dataPoints.get(i + 1).getCost()) * yScale + padding);
            g2.drawLine(x1, y1_cost, x2, y2_cost);
        }

        g2.setStroke(oldStroke);

        // --- Draw Legend ---
        g2.setColor(Color.BLUE);
        g2.fillRect(width - padding - 80, 10, 10, 10);
        g2.setColor(Color.BLACK);
        g2.drawString("總資產", width - padding - 65, 20);

        g2.setColor(Color.RED);
        g2.fillRect(width - padding - 80, 30, 10, 10);
        g2.setColor(Color.BLACK);
        g2.drawString("總成本", width - padding - 65, 40);
    }
}
