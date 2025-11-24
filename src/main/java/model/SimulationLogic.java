package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimulationLogic {

    public static List<DataPoint> runSimulation(int startYear, int startMonth, double initialInvestment, double monthlyInvestment, Map<Integer, Map<Integer, Double>> etfData) {
        List<DataPoint> dataPoints = new ArrayList<>();
        double currentTotalValue = initialInvestment;
        double cumulativeCost = initialInvestment;
        
        String initialDate = String.format("%d/%02d", startYear, startMonth);
        dataPoints.add(new DataPoint(initialDate, currentTotalValue, cumulativeCost));

        if (etfData == null) {
            return dataPoints; // Return initial state if no data
        }

        List<Integer> years = etfData.keySet().stream().sorted().collect(Collectors.toList());

        for (int year : years) {
            if (year < startYear) continue;

            for (int month = 1; month <= 12; month++) {
                if (year == startYear && month < startMonth) continue;

                if (etfData.containsKey(year) && etfData.get(year).containsKey(month)) {
                    if (!(year == startYear && month == startMonth)) {
                        currentTotalValue += monthlyInvestment;
                        cumulativeCost += monthlyInvestment;
                    }

                    double monthlyReturn = etfData.get(year).get(month);
                    currentTotalValue += currentTotalValue * (monthlyReturn / 100.0);
                    
                    String date = String.format("%d/%02d", year, month);
                    dataPoints.add(new DataPoint(date, currentTotalValue, cumulativeCost));
                }
            }
        }
        return dataPoints;
    }
}
