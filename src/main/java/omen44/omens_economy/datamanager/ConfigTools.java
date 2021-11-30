package omen44.omens_economy.datamanager;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigTools {
    public static FileConfiguration getFileConfig(String fileName) {
        File configFile = new File(Bukkit.getServer().getWorldContainer().getAbsolutePath() + "/plugins/omens_economy/"+fileName);
        return YamlConfiguration.loadConfiguration(configFile);
    }
}
