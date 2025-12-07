package com.gtcafe.investion.web;

import com.gtcafe.investion.service.EtfDataService;
import com.gtcafe.investion.model.DataPoint;
import com.gtcafe.investion.model.SimulationLogic;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class SimulatorWebController {

    private final EtfDataService etfDataService;

    public SimulatorWebController(EtfDataService etfDataService) {
        this.etfDataService = etfDataService;
    }

    @GetMapping("/")
    public String index(Model model) {
        if (etfDataService.getAllEtfData() != null) {
            model.addAttribute("etfs", etfDataService.getAllEtfData().keySet());
        }
        // Defaults
        model.addAttribute("initialInvestment", 100000);
        model.addAttribute("monthlyInvestment", 5000);
        model.addAttribute("startYear", 2003); // Default to a reasonable start year
        model.addAttribute("startMonth", 1);
        return "index";
    }

    @PostMapping("/simulate")
    public String simulate(
            @RequestParam String etf,
            @RequestParam int startYear,
            @RequestParam int startMonth,
            @RequestParam double initialInvestment,
            @RequestParam double monthlyInvestment,
            Model model) {
        
        Map<Integer, Map<Integer, Double>> etfData = etfDataService.getEtfData(etf);
        List<DataPoint> results = SimulationLogic.runSimulation(startYear, startMonth, initialInvestment, monthlyInvestment, etfData);
        
        model.addAttribute("results", results);
        model.addAttribute("etfs", etfDataService.getAllEtfData().keySet());
        model.addAttribute("selectedEtf", etf);
        model.addAttribute("initialInvestment", initialInvestment);
        model.addAttribute("monthlyInvestment", monthlyInvestment);
        model.addAttribute("startYear", startYear);
        model.addAttribute("startMonth", startMonth);
        
        // Calculate summary stats for the view
        if (!results.isEmpty()) {
            DataPoint last = results.get(results.size() - 1);
            double totalCost = last.getCost();
            double finalValue = last.getValue();
            double profit = finalValue - totalCost;
            double roi = (totalCost > 0) ? (profit / totalCost) * 100 : 0;
            
            model.addAttribute("totalCost", totalCost);
            model.addAttribute("finalValue", finalValue);
            model.addAttribute("profit", profit);
            model.addAttribute("roi", roi);
            
            // For chart
            List<String> labels = results.stream().map(DataPoint::getDate).collect(Collectors.toList());
            List<Double> values = results.stream().map(DataPoint::getValue).collect(Collectors.toList());
            List<Double> costs = results.stream().map(DataPoint::getCost).collect(Collectors.toList());
            
            model.addAttribute("chartLabels", labels);
            model.addAttribute("chartValues", values);
            model.addAttribute("chartCosts", costs);
        }

        return "index";
    }
}
