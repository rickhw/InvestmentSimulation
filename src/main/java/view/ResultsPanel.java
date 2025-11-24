package view;

import model.DataPoint;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class ResultsPanel extends JSplitPane {

    private ChartPanel chartPanel;
    private JTable resultsTable;
    private DefaultTableModel tableModel;

    public ResultsPanel() {
        super(JSplitPane.VERTICAL_SPLIT);

        chartPanel = new ChartPanel();
        
        String[] columnNames = {"日期", "本月投入", "總成本", "月績效 (%)", "總資產"};
        tableModel = new DefaultTableModel(columnNames, 0);
        resultsTable = new JTable(tableModel);
        resultsTable.setFillsViewportHeight(true);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        for (int i = 1; i < columnNames.length; i++) {
            resultsTable.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }

        JScrollPane tableScrollPane = new JScrollPane(resultsTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("模擬結果"));

        setTopComponent(chartPanel);
        setBottomComponent(tableScrollPane);
        setResizeWeight(0.66);
    }

    public void updateResults(List<DataPoint> dataPoints) {
        chartPanel.setDataPoints(dataPoints);

        tableModel.setRowCount(0);
        if (dataPoints == null || dataPoints.size() < 2) return;

        for (int i = 1; i < dataPoints.size(); i++) {
            DataPoint dp = dataPoints.get(i);
            DataPoint prevDp = dataPoints.get(i-1);

            double investmentThisMonth = dp.getCost() - prevDp.getCost();
             if (i == 1) { // First month of simulation includes initial investment in cost
                 investmentThisMonth = dp.getCost();
             }
            
            // Avoid division by zero if previous value + investment is zero
            double basis = prevDp.getValue() + ( (i==1) ? 0 : investmentThisMonth) ;
            double monthlyReturn = (basis == 0) ? 0.0 : (dp.getValue() - prevDp.getValue() - ((i==1)?0:investmentThisMonth)) / basis * 100;
           

            Object[] rowData = {
                dp.getDate(),
                String.format("%.2f", investmentThisMonth),
                String.format("%.2f", dp.getCost()),
                String.format("%.2f", monthlyReturn),
                String.format("%.2f", dp.getValue())
            };
            tableModel.addRow(rowData);
        }
    }
}
