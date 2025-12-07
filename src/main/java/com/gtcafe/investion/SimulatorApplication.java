package com.gtcafe.investion;

import com.gtcafe.investion.desktop.controller.AppController;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.swing.SwingUtilities;

@SpringBootApplication
@ComponentScan(basePackages = {"com.gtcafe.investion"})
public class SimulatorApplication {

    public static void main(String[] args) {
        String mode = "web"; // Default mode
        
        // Simple argument parsing for app.mode
        for (String arg : args) {
            if (arg.startsWith("--app.mode=")) {
                mode = arg.substring("--app.mode=".length());
            }
        }

        SpringApplicationBuilder builder = new SpringApplicationBuilder(SimulatorApplication.class);

        if ("desktop".equalsIgnoreCase(mode)) {
            System.out.println("Starting in DESKTOP mode...");
            builder.web(WebApplicationType.NONE);
            builder.headless(false);
        } else {
            System.out.println("Starting in WEB mode...");
            builder.web(WebApplicationType.SERVLET);
        }

        ConfigurableApplicationContext context = builder.run(args);

        // Launch Swing UI only in desktop mode
        if ("desktop".equalsIgnoreCase(mode)) {
            SwingUtilities.invokeLater(() -> {
                try {
                    new AppController();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
