package omen44.omens_economy;

import omen44.omens_economy.commands.chestshop.CommandBuyShop;
import omen44.omens_economy.commands.chestshop.CommandCancelShop;
import omen44.omens_economy.commands.economy.CommandBal;
import omen44.omens_economy.commands.economy.CommandSetMoney;
import omen44.omens_economy.commands.economy.CommandTransfer;
import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.datamanager.MySQL;
import omen44.omens_economy.events.JoinLeave;
import omen44.omens_economy.events.PlayerDeath;
import omen44.omens_economy.events.PlayerMine;
import omen44.omens_economy.listeners.ChestShopPlace;
import omen44.omens_economy.listeners.ChestShopTrade;
import omen44.omens_economy.utils.EconomyUtils;
import omen44.omens_economy.utils.SQLUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public ConfigTools configTools;
    public MySQL mySQL;
    public SQLUtils sqlUtils;
    public EconomyUtils economyUtils;

    @Override
    public void onEnable() {
        // initialize classes:
        configTools = new ConfigTools(this);
        mySQL = new MySQL(this);
        sqlUtils = new SQLUtils(this);
        economyUtils = new EconomyUtils(this);

        // register listeners:
        getServer().getPluginManager().registerEvents(new PlayerMine(this), this);
        getServer().getPluginManager().registerEvents(new JoinLeave(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(this), this);
        getServer().getPluginManager().registerEvents(new ChestShopTrade(this), this);
        getServer().getPluginManager().registerEvents(new ChestShopPlace(this), this);

        // Plugin startup logic
        configTools.generateConfig("config.yml");
        FileConfiguration config = configTools.getFileConfig("config.yml");

        String symbol = config.getString("money.moneySymbol");
        Bukkit.getLogger().info("Money symbol: " + symbol);

        //connect to the database
        this.mySQL = new MySQL(this);

        mySQL.connect();
        if (mySQL.isConnected()) {
            //create main table:
            sqlUtils.createTable("players", "UUID");
            sqlUtils.createColumn("ign", "VARCHAR(100)", "players");
            sqlUtils.createColumn("wallet", "INT(100)", "players");
            sqlUtils.createColumn("bank", "INT(100)", "players");
            Bukkit.getLogger().info(ChatColor.BLUE + "Database is connected!");
        } else {
            Bukkit.getLogger().severe("Database not connected!");
        }

        // initialise commands
        new CommandBal(this);
        new CommandSetMoney(this);
        new CommandTransfer(this);
        new CommandBuyShop(this);
        new CommandCancelShop(this);

        //initialise tab completers
        getCommand("transfer").setTabCompleter(new CommandTransfer(this));
        getCommand("setmoney").setTabCompleter(new CommandSetMoney(this));
        getCommand("bal").setTabCompleter(new CommandBal(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        mySQL.disconnect();
    }
}
