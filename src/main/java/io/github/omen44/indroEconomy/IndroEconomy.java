package io.github.omen44.indroEconomy;

import io.github.omen44.indroEconomy.events.EventOnPlayerJoinLeave;
import io.github.omen44.indroEconomy.commands.economy.CommandBal;
import io.github.omen44.indroEconomy.commands.economy.CommandPay;
import io.github.omen44.indroEconomy.commands.economy.CommandSetMoney;
import io.github.omen44.indroEconomy.commands.economy.CommandTransfer;
import io.github.omen44.indroEconomy.commands.opShop.CommandOpShop;
import io.github.omen44.indroEconomy.commands.shops.CommandCreateShop;
import io.github.omen44.indroEconomy.datamanager.ConfigTools;
import io.github.omen44.indroEconomy.events.EventOnPlayerDeath;
import io.github.omen44.indroEconomy.events.EventOnPlayerMine;
import io.github.omen44.indroEconomy.events.EventOnShops;
import io.github.omen44.indroEconomy.utils.JsonSaver;
import io.github.omen44.indroEconomy.utils.SQLUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;

import static io.github.omen44.indroEconomy.utils.ShortcutsUtils.mPrefix;

public class IndroEconomy extends JavaPlugin {
    Connection connection;
    SQLUtils sqlUtils;
    JsonSaver jsonSaver;
    @Override
    public void onEnable() {
        // Plugin startup logic

        // instantiation of classes
        PluginManager pm = getServer().getPluginManager();
        ConfigTools configTools = new ConfigTools();
        configTools.saveDefaultConfig("config.yml");
        FileConfiguration config = configTools.getConfig("config.yml");
        jsonSaver = new JsonSaver();

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

        String symbol = config.getString("money.moneySymbol");
        Bukkit.getLogger().info("Money symbol: " + symbol);

        // commands
        // initialise commands
        getCommand("bal").setExecutor(new CommandBal());
        getCommand("pay").setExecutor(new CommandPay());
        getCommand("setmoney").setExecutor(new CommandSetMoney());
        getCommand("transfer").setExecutor(new CommandTransfer());
        getCommand("opshop").setExecutor(new CommandOpShop());
        getCommand("createshop").setExecutor(new CommandCreateShop());

        //initialise tab completers
        getCommand("transfer").setTabCompleter(new CommandTransfer());
        getCommand("setmoney").setTabCompleter(new CommandSetMoney());
        getCommand("bal").setTabCompleter(new CommandBal());
        getCommand("pay").setTabCompleter(new CommandPay());
        getCommand("createshop").setTabCompleter(new CommandCreateShop());

        //register events
        pm.registerEvents(new EventOnPlayerJoinLeave(), this);
        pm.registerEvents(new EventOnPlayerDeath(), this);
        pm.registerEvents(new EventOnPlayerMine(), this);
        pm.registerEvents(new EventOnShops(), this);
        pm.registerEvents(new CommandOpShop(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        sqlUtils.closeConnection(connection);
    }

    void dbCreation() {
        // handles creation of the economy table
        sqlUtils.createTable("economy", "UUID");
        sqlUtils.createColumn("wallet", "INT(100)", "economy");
        sqlUtils.createColumn("bank", "INT(100)", "economy");
        jsonSaver.createFile("daily");
    }
}
