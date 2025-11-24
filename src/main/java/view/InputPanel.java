package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InputPanel extends JPanel {

    public JComboBox<String> etfSimulationSelector;
    public JComboBox<Integer> startYearComboBox;
    public JComboBox<String> startMonthComboBox;
    public JTextField initialInvestmentField;
    public JTextField monthlyInvestmentField;
    public JButton simulateButton;

    public InputPanel(String[] etfCodes, Map<String, Map<Integer, Map<Integer, Double>>> allEtfData) {
        super(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("模擬參數"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        etfSimulationSelector = new JComboBox<>(etfCodes);
        startYearComboBox = new JComboBox<>();
        startMonthComboBox = new JComboBox<>(new String[]{"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"});
        startMonthComboBox.setSelectedIndex(9); // Default to October

        initialInvestmentField = new JTextField("10", 10);
        monthlyInvestmentField = new JTextField("1", 10);
        simulateButton = new JButton("開始模擬");

        Runnable updateYearComboBox = () -> {
            String selectedEtf = (String) etfSimulationSelector.getSelectedItem();
            Map<Integer, Map<Integer, Double>> etfData = allEtfData.get(selectedEtf);
            if (etfData != null) {
                List<Integer> years = etfData.keySet().stream().sorted().collect(Collectors.toList());
                startYearComboBox.setModel(new DefaultComboBoxModel<>(years.toArray(new Integer[0])));
            }
        };

        updateYearComboBox.run();
        etfSimulationSelector.addActionListener(e -> updateYearComboBox.run());

        // Layout
        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("模擬標的:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; add(etfSimulationSelector, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; add(new JLabel("起始日期:"), gbc);
        gbc.gridx = 1; add(startYearComboBox, gbc);
        gbc.gridx = 2; add(startMonthComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("初始本金:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.HORIZONTAL; add(initialInvestmentField, gbc);
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; add(new JLabel(" 萬"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("每月定額投資:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; add(monthlyInvestmentField, gbc);
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; add(new JLabel(" 萬"), gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3; gbc.anchor = GridBagConstraints.CENTER; add(simulateButton, gbc);
    }
}
