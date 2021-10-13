package omen44.omens_economy;

import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.datamanager.MySQL;
import omen44.omens_economy.events.PlayerMine;
import omen44.omens_economy.utils.EconomyUtils;
import omen44.omens_economy.utils.SQLUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

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

        // Plugin startup logic
        configTools.generateConfig("config.yml");
        FileConfiguration config = configTools.getFileConfig("config.yml");

        String symbol = config.getString("moneySymbol");
        Bukkit.getLogger().info(symbol);

        // connects to the database:
        this.mySQL = new MySQL(this);

        try {
            mySQL.connect();
        } catch (ClassNotFoundException | SQLException e) {
            Bukkit.getLogger().severe("Database not connected!");
        }

        if (mySQL.isConnected()) {
            Bukkit.getLogger().info(ChatColor.BLUE + "Database is connected!");
        }

        //create main table:
        sqlUtils.createTable("players", "UUID");
        sqlUtils.createColumn("ign", "VARCHAR(100)", "players");
        sqlUtils.createColumn("wallet", "INT(100)", "players");
        sqlUtils.createColumn("bank", "INT(100)", "players");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        mySQL.disconnect();
    }
}
