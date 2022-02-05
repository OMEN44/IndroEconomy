package io.github.omen44.indroEconomy;

import io.github.omen44.indroEconomy.commands.economy.*;
import io.github.omen44.indroEconomy.datamanager.ConfigTools;
import io.github.omen44.indroEconomy.events.EventOnPlayerDeath;
import io.github.omen44.indroEconomy.events.EventOnPlayerJoinLeave;
import io.github.omen44.indroEconomy.events.EventOnPlayerMine;
import io.github.omen44.indroEconomy.storage.EconomyStorageUtil;
import io.github.omen44.indroEconomy.utils.EconomyImplementer;
import io.github.omen44.indroEconomy.utils.YamlUtils;
import me.kodysimpson.simpapi.command.CommandManager;
import me.kodysimpson.simpapi.menu.MenuManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class IndroEconomy extends JavaPlugin {
    private static IndroEconomy plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic

        // set up the MenuManager
        MenuManager.setup(getServer(), this);
        plugin = this;

        // instantiation of classes
        PluginManager pm = getServer().getPluginManager();
        ConfigTools configTools = new ConfigTools();
        configTools.saveDefaultConfig("config.yml");
        FileConfiguration config = configTools.getConfig("config.yml");
        YamlUtils yamlUtils = new YamlUtils("backLocation");
        yamlUtils.createFile();

        String symbol = config.getString("money.moneySymbol");
        Bukkit.getLogger().info("Money symbol: " + symbol);

        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
            Bukkit.getServer().getServicesManager().register(Economy.class, new EconomyImplementer(), this, ServicePriority.Highest);
            this.getLogger().info("Vault Found, integrating with it.");
        }

        // commands
        try {
            CommandManager.createCoreCommand(this, "eco",
                    "The Economy Module of the Plugin.", "/eco",
                    null,
                    CommandBal.class, CommandOpShop.class, CommandSend.class, CommandSetMoney.class,
                    CommandTransfer.class, CommandGamble.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            EconomyStorageUtil.loadAccounts();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //register events
        pm.registerEvents(new EventOnPlayerJoinLeave(), this);
        pm.registerEvents(new EventOnPlayerDeath(), this);
        pm.registerEvents(new EventOnPlayerMine(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            EconomyStorageUtil.saveAccounts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static IndroEconomy getInstance() {
        return plugin;
    }
}
