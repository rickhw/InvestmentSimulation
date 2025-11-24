package view;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class MainView extends JFrame {

    public InputPanel inputPanel;
    public ResultsPanel resultsPanel;
    public PerformancePanel performancePanel;

    public MainView(Map<String, Map<Integer, Map<Integer, Double>>> allEtfData) {
        super("ETF 投資模擬器");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);

        // --- LEFT PANEL (Simulation) ---
        JPanel simulationPanel = new JPanel(new BorderLayout());
        
        String[] etfCodes = allEtfData.keySet().toArray(new String[0]);
        inputPanel = new InputPanel(etfCodes, allEtfData);
        resultsPanel = new ResultsPanel();
        
        simulationPanel.add(inputPanel, BorderLayout.NORTH);
        simulationPanel.add(resultsPanel, BorderLayout.CENTER);

        // --- RIGHT PANEL (Historical Performance) ---
        performancePanel = new PerformancePanel(allEtfData);

        // --- MAIN LAYOUT ---
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, simulationPanel, performancePanel);
        mainSplitPane.setResizeWeight(0.65);
        
        getContentPane().add(mainSplitPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }
}
