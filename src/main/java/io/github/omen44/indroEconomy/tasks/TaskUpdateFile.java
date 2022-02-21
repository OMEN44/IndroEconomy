package io.github.omen44.indroEconomy.tasks;

import io.github.omen44.indroEconomy.storage.EconomyStorageUtil;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class TaskUpdateFile extends BukkitRunnable {
    private final JavaPlugin plugin;

    public TaskUpdateFile(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        try {
            plugin.getLogger().info("Saving accounts to file!");
            EconomyStorageUtil.saveAccounts();
        } catch (IOException e) {
            plugin.getLogger().severe("File could not be saved!");
            e.printStackTrace();
            plugin.getLogger().severe("Disabling the plugin!");
            plugin.getPluginLoader().disablePlugin(plugin);
        }
    }
}
