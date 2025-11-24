package model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DataManager {

    public static Map<String, Map<Integer, Map<Integer, Double>>> loadAllEtfData(String... resourceNames) throws IOException {
        Map<String, Map<Integer, Map<Integer, Double>>> allData = new HashMap<>();
        for (String resourceName : resourceNames) {
            String etfCode = resourceName.split("_")[0];
            Map<Integer, Map<Integer, Double>> etfData = loadHistoricalData(resourceName);
            allData.put(etfCode, etfData);
        }
        return allData;
    }

    private static Map<Integer, Map<Integer, Double>> loadHistoricalData(String resourceName) throws IOException {
        Map<Integer, Map<Integer, Double>> data = new HashMap<>();
        
        InputStream is = DataManager.class.getClassLoader().getResourceAsStream(resourceName);
        if (is == null) {
            throw new IOException("Resource not found: " + resourceName);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                try {
                    int year = Integer.parseInt(values[0]);
                    Map<Integer, Double> monthlyReturns = new HashMap<>();
                    for (int i = 1; i <= 12; i++) {
                        if (i < values.length && !values[i].trim().isEmpty() && !values[i].trim().equals("-")) {
                            monthlyReturns.put(i, Double.parseDouble(values[i]));
                        }
                    }
                    data.put(year, monthlyReturns);
                } catch (NumberFormatException e) {
                   // Ignore malformed lines
                }
            }
        }
        return data;
    }
}