package com.gtcafe.investion.desktop.view;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class InputPanel extends JPanel {

    private JComboBox<String> etfSimulationSelector;
    private JComboBox<Integer> startYearComboBox;
    private JComboBox<String> startMonthComboBox;
    private JTextField initialInvestmentField;
    private JTextField monthlyInvestmentField;
    private JButton simulateButton;

    public InputPanel(String[] etfCodes, Map<String, Map<Integer, Map<Integer, Double>>> allEtfData) {
        super(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder("模擬參數"));

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
        add(new JLabel("模擬標的:"));
        add(etfSimulationSelector);
        add(new JLabel("起始日期:"));
        add(startYearComboBox);
        add(startMonthComboBox);
        add(new JLabel("初始本金(萬):"));
        add(initialInvestmentField);
        add(new JLabel("每月定額投資(萬):"));
        add(monthlyInvestmentField);
        add(simulateButton);
    }
}
