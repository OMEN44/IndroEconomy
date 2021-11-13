package omen44.omens_economy.datamanager;

import omen44.omens_economy.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigTools {
    private static Main main;
    public ConfigTools(Main main) {
        ConfigTools.main = main;
    }

    public static FileConfiguration getFileConfig(String fileName) {
        File configFile = new File(Bukkit.getServer().getWorldContainer().getAbsolutePath() + "/plugins/omens_economy/"+fileName); // First we
        // will load
        // the file.
        return YamlConfiguration.loadConfiguration(configFile);
    }

    public static void generateConfig(String configName) {
        File configA = new File(main.getDataFolder(), configName);

        if (!configA.exists()) {
            configA.getParentFile().mkdirs();
            main.saveResource(configName, false);
        }
        FileConfiguration config = new YamlConfiguration();

        try {
            config.load(configA);
        } catch (IOException | InvalidConfigurationException e ) {
            e.printStackTrace();
        }
    }
}
