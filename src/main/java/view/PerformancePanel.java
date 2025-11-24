package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

public class PerformancePanel extends JPanel {

    private JComboBox<String> etfSelector;
    private JTable performanceTable;
    private DefaultTableModel tableModel;
    private Map<String, Map<Integer, Map<Integer, Double>>> allEtfData;

    public PerformancePanel(Map<String, Map<Integer, Map<Integer, Double>>> allEtfData) {
        this.allEtfData = allEtfData;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("ETF 歷史績效"));

        // ETF Selector
        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        etfSelector = new JComboBox<>(allEtfData.keySet().toArray(new String[0]));
        selectorPanel.add(new JLabel("選擇 ETF:"));
        selectorPanel.add(etfSelector);

        // Add listener to update table on selection change
        etfSelector.addActionListener(e -> displayPerformance());

        // Performance Table
        String[] columnNames = {"年份", "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
        tableModel = new DefaultTableModel(columnNames, 0);
        performanceTable = new JTable(tableModel);
        performanceTable.setFillsViewportHeight(true);
        performanceTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        PerformanceCellRenderer performanceRenderer = new PerformanceCellRenderer();
        for (int i = 1; i < columnNames.length; i++) {
            performanceTable.getColumnModel().getColumn(i).setCellRenderer(performanceRenderer);
        }

        add(selectorPanel, BorderLayout.NORTH);
        add(new JScrollPane(performanceTable), BorderLayout.CENTER);

        // Initial display
        displayPerformance();
    }

    private void displayPerformance() {
        String selectedEtf = (String) etfSelector.getSelectedItem();
        if (selectedEtf == null) return;

        Map<Integer, Map<Integer, Double>> data = allEtfData.get(selectedEtf);
        if (data == null) return;

        tableModel.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#.##'%'");

        for (Integer year : data.keySet().stream().sorted().collect(Collectors.toList())) {
            Vector<String> row = new Vector<>();
            row.add(year.toString());
            Map<Integer, Double> monthlyReturns = data.get(year);
            for (int i = 1; i <= 12; i++) {
                if (monthlyReturns.containsKey(i)) {
                    row.add(df.format(monthlyReturns.get(i)));
                } else {
                    row.add("-");
                }
            }
            tableModel.addRow(row);
        }
    }
}
