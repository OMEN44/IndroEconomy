package io.github.omen44.indroEconomy.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskStockUpdate extends BukkitRunnable {
    private final int median;
    private final int value;
    private static int diamondStock;
    private static int diamondPrice;

    public TaskStockUpdate(JavaPlugin plugin) {
        FileConfiguration config = plugin.getConfig();
        diamondStock = config.getInt("stock.defaultStock");
        this.median = config.getInt("stock.median");
        this.value = config.getInt("stock.value");
        diamondPrice = 0;
    }

    @Override
    public void run() {
        diamondPrice = median * value / diamondStock;
        Bukkit.broadcast(ChatColor.BLUE + "Stocks Recalculated!", "indroEconomy.stock.insider");
    }

    // getters and setters

    public static int getDiamondPrice() {
        return diamondPrice;
    }

    public static int getDiamondStock() {
        return diamondStock;
    }

    public static void setDiamondStock(int diamondStock) {
        TaskStockUpdate.diamondStock = diamondStock;
    }
}
