package controller;

import model.DataManager;
import model.DataPoint;
import model.SimulationLogic;
import view.MainView;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class AppController {

    private MainView mainView;
    private Map<String, Map<Integer, Map<Integer, Double>>> allEtfData;

    public AppController() {
        try {
            // Hardcoding the resource names is more reliable for classpath loading
            allEtfData = DataManager.loadAllEtfData("0050_23y.csv", "00999_20y.csv");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "無法載入數據: " + e.getMessage(), "啟動錯誤", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // Exit if data can't be loaded
        }

        mainView = new MainView(allEtfData);
        attachListeners();
        mainView.setVisible(true);
    }

    private void attachListeners() {
        mainView.inputPanel.simulateButton.addActionListener(e -> runSimulation());
    }

    private void runSimulation() {
        try {
            String selectedEtf = (String) mainView.inputPanel.etfSimulationSelector.getSelectedItem();
            Map<Integer, Map<Integer, Double>> etfData = allEtfData.get(selectedEtf);

            Object selectedYearItem = mainView.inputPanel.startYearComboBox.getSelectedItem();
            if (selectedYearItem == null) {
                 JOptionPane.showMessageDialog(mainView, "沒有可用的年份進行模擬。", "設定錯誤", JOptionPane.WARNING_MESSAGE);
                 return;
            }

            int startYear = (int) selectedYearItem;
            int startMonth = mainView.inputPanel.startMonthComboBox.getSelectedIndex() + 1;
            
            double initialInvestment = Double.parseDouble(mainView.inputPanel.initialInvestmentField.getText());
            double monthlyInvestment = Double.parseDouble(mainView.inputPanel.monthlyInvestmentField.getText());

            List<DataPoint> results = SimulationLogic.runSimulation(startYear, startMonth, initialInvestment, monthlyInvestment, etfData);
            
            mainView.resultsPanel.updateResults(results);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(mainView, "無效輸入或模擬出錯。請檢查您的參數。", "模擬錯誤", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppController::new);
    }
}
