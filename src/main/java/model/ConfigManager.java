package model;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigManager {

    private static ConfigManager instance;
    private Map<String, Object> config;

    private ConfigManager() {
        loadConfig();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void loadConfig() {
        Yaml yaml = new Yaml();
        try (InputStream in = ConfigManager.class.getResourceAsStream("/config.yaml")) {
            config = yaml.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getChartConfig() {
        return (Map<String, Object>) config.get("chart");
    }
}
