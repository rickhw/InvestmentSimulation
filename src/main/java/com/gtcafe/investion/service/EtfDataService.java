package com.gtcafe.investion.service;

import com.gtcafe.investion.model.DataManager;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

@Service
public class EtfDataService {

    private Map<String, Map<Integer, Map<Integer, Double>>> allEtfData;

    @PostConstruct
    public void init() {
        try {
            // Load data using the existing DataManager
            allEtfData = DataManager.loadAllEtfData("0050_23y.csv", "00999_20y.csv");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load ETF data", e);
        }
    }

    public Map<String, Map<Integer, Map<Integer, Double>>> getAllEtfData() {
        return allEtfData;
    }
    
    public Map<Integer, Map<Integer, Double>> getEtfData(String etfName) {
        return allEtfData.get(etfName);
    }
}
