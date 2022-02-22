package io.github.omen44.indroEconomy.utils;

import io.github.omen44.indroEconomy.IndroEconomy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YamlUtils {
    private final File file;

    public YamlUtils(String filename) {
        this.file = new File(IndroEconomy.getInstance().getDataFolder().getAbsolutePath() + File.separator + filename + ".yml");
    }

    public FileConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(file);
    }

    /* // create a custom file
    public void createFile() {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().severe("File could not be created!");
                e.printStackTrace();
                return;
            }
            Bukkit.getLogger().info("File successfully created!");
        }
    }
    */

    public void saveFile(FileConfiguration config) {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}