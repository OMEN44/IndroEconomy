package omen44.omens_economy.datamanager;

import omen44.omens_economy.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class ConfigTools {
    public Plugin plugin = Main.getPlugin(Main.class);
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public void reloadConfig(String fileName) {
        if (this.configFile == null) {
            this.configFile = new File(plugin.getDataFolder(), fileName);
        }

        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = plugin.getResource(fileName);
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }

    }

    public FileConfiguration getConfig(String filename) {
        if (this.dataConfig == null) {
            this.reloadConfig(filename);
        }
        return this.dataConfig;
    }

    public void saveConfig(String fileName) {
        if (this.dataConfig != null && this.configFile != null) {
            try {
                this.getConfig(fileName).save(this.configFile);
            } catch (IOException var3) {
                plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, var3);
            }

        }
    }

    public void saveDefaultConfig(String fileName) {
        if (this.configFile == null) {
            this.configFile = new File(plugin.getDataFolder(), fileName);
        }

        if (!this.configFile.exists()) {
            plugin.saveResource(fileName, false);
        }
    }

    /*
    public static FileConfiguration getFileConfig(String fileName) {
        File configFile = new File(Bukkit.getServer().getWorldContainer().getAbsolutePath() + "/plugins/omens_economy/"+fileName);
        return YamlConfiguration.loadConfiguration(configFile);
    }
     */
}

