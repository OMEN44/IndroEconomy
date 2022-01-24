package omen44.omens_economy;

import omen44.omens_economy.commands.economy.CommandBal;
import omen44.omens_economy.commands.economy.CommandPay;
import omen44.omens_economy.commands.economy.CommandSetMoney;
import omen44.omens_economy.commands.economy.CommandTransfer;
import omen44.omens_economy.commands.opShop.CommandOpShop;
import omen44.omens_economy.commands.shops.CommandCreateShop;
import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.events.EventOnPlayerDeath;
import omen44.omens_economy.events.EventOnPlayerJoinLeave;
import omen44.omens_economy.events.EventOnPlayerMine;
import omen44.omens_economy.events.EventOnShops;
import omen44.omens_economy.utils.SQLUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;

import static omen44.omens_economy.utils.ShortcutsUtils.mPrefix;

public class Main extends JavaPlugin {
    Connection connection;
    SQLUtils sqlUtils;

    @Override
    public void onEnable() {
        // Plugin startup logic

        // instantiation of classes
        PluginManager pm = getServer().getPluginManager();
        ConfigTools configTools = new ConfigTools();
        configTools.saveDefaultConfig("config.yml");
        configTools.saveDefaultConfig("daily.yml");
        FileConfiguration config = configTools.getConfig("config.yml");

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
        sqlUtils.createColumn("wallet", "VARCHAR(100)", "economy");
        sqlUtils.createColumn("bank", "VARCHAR(100)", "economy");
    }
}
