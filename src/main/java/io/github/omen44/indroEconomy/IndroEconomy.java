package io.github.omen44.indroEconomy;

import io.github.omen44.indroEconomy.commands.economy.*;
import io.github.omen44.indroEconomy.events.EventOnPlayerJoinLeave;
import io.github.omen44.indroEconomy.datamanager.ConfigTools;
import io.github.omen44.indroEconomy.events.EventOnPlayerDeath;
import io.github.omen44.indroEconomy.events.EventOnPlayerMine;
import io.github.omen44.indroEconomy.storage.EconomyStorageUtil;
import io.github.omen44.indroEconomy.utils.EconomyImplementer;
import io.github.omen44.indroEconomy.utils.SQLUtils;
import io.github.omen44.indroEconomy.utils.sqlite.Database;
import io.github.omen44.indroEconomy.utils.sqlite.SQLite;
import me.kodysimpson.simpapi.command.CommandManager;
import me.kodysimpson.simpapi.menu.MenuManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.Connection;

import static io.github.omen44.indroEconomy.utils.ShortcutsUtils.mPrefix;

public class IndroEconomy extends JavaPlugin {
    Connection connection;
    SQLUtils sqlUtils;

    private static IndroEconomy plugin;
    private Database db;

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

        String dataType = config.getString("saveData");

        if (dataType == null) {
            Bukkit.getLogger().severe("Data Type not Specified, Assuming JSON!");
            config.set("saveData", "JSON");
        } else if (dataType.equalsIgnoreCase("mysql")) { // mysql initialisation
            final String host = config.getString("database.host");
            final String port = config.getString("database.port");
            final String database = config.getString("database.database");
            final String username = config.getString("database.username");
            final String password = config.getString("database.password");
            sqlUtils = new SQLUtils(database, host, port, username, password);
            connection = sqlUtils.getConnection();

            // connecting classes:
            if (connection != null) {
                Bukkit.getLogger().info(mPrefix + "Database Successfully Connected!");
                //create table:
                dbCreation();
                Bukkit.getLogger().info(mPrefix + "Database Initialised!");
            } else {
                Bukkit.getLogger().severe("Database not connected!");
            }
        } else if (dataType.equalsIgnoreCase("sqlite")) {
            this.db = new SQLite(this);
            this.db.load();
        }

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
            EconomyStorageUtil.saveAccounts();
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
        if (connection != null) {
            sqlUtils.closeConnection(connection);
        }
        try {
            EconomyStorageUtil.saveAccounts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void dbCreation() {
        // handles creation of the economy table
        sqlUtils.createTable("economy", "UUID");
        sqlUtils.createColumn("wallet", "INT(100)", "economy");
        sqlUtils.createColumn("bank", "INT(100)", "economy");
    }

    public static IndroEconomy getPlugin() {
        return plugin;
    }
}
