package omen44.omens_economy;

import omen44.omens_economy.commands.economy.CommandBal;
import omen44.omens_economy.commands.economy.CommandPay;
import omen44.omens_economy.commands.economy.CommandSetMoney;
import omen44.omens_economy.commands.economy.CommandTransfer;
import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.datamanager.MySQL;
import omen44.omens_economy.events.EventOnPlayerDeath;
import omen44.omens_economy.events.EventOnPlayerJoinLeave;
import omen44.omens_economy.events.EventOnPlayerMine;
import omen44.omens_economy.utils.SQLUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;

import static omen44.omens_economy.utils.ShortcutsUtils.mPrefix;

public class Main extends JavaPlugin {
    //public static Main getInstance;
    MySQL SQL;
    Connection connection;
    SQLUtils sqlUtils;

    @Override
    public void onEnable() {
        // getInstance = this;
        PluginManager pm = getServer().getPluginManager();
        ConfigTools configTools = new ConfigTools();
        SQL = new MySQL();
        connection = SQL.getConnection();
        sqlUtils = new SQLUtils(connection);
        // Plugin startup logic
        configTools.saveDefaultConfig("config.yml");

        FileConfiguration config = configTools.getConfig("config.yml");

        // initialize classes:
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
        this.getCommand("bal").setExecutor(new CommandBal());
        this.getCommand("setmoney").setExecutor(new CommandSetMoney());
        this.getCommand("transfer").setExecutor(new CommandTransfer());
        this.getCommand("pay").setExecutor(new CommandPay());

        //initialise tab completers
        getCommand("transfer").setTabCompleter(new CommandTransfer());
        getCommand("setmoney").setTabCompleter(new CommandSetMoney());
        getCommand("bal").setTabCompleter(new CommandBal());
        getCommand("pay").setTabCompleter(new CommandPay());

        //register events
        pm.registerEvents(new EventOnPlayerJoinLeave(), this);
        pm.registerEvents(new EventOnPlayerDeath(), this);
        pm.registerEvents(new EventOnPlayerMine(), this);
        // pm.registerEvents(new EventOnShops(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        MySQL.closeConnection(connection);
    }

    void dbCreation() {
        // handles creation of the economy table
        sqlUtils.createDBTable("economy", "UUID");
        sqlUtils.createDBColumn("wallet", "VARCHAR(100)", "economy");
        sqlUtils.createDBColumn("bank", "VARCHAR(100)", "economy");

        // handles creation of the shops table
        // sqlUtils.createShopsTable();
    }
}
