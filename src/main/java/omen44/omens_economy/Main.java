package omen44.omens_economy;

import omen44.omens_economy.commands.economy.CommandBal;
import omen44.omens_economy.commands.economy.CommandSetMoney;
import omen44.omens_economy.commands.economy.CommandTransfer;
import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.datamanager.MySQL;
import omen44.omens_economy.discordLink.Bot;
import omen44.omens_economy.events.JoinLeave;
import omen44.omens_economy.events.PlayerDeath;
import omen44.omens_economy.events.PlayerMine;
import omen44.omens_economy.utils.EconomyUtils;
import omen44.omens_economy.utils.SQLUtils;
import omen44.omens_economy.utils.ShortcutsUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;

public final class Main extends JavaPlugin {

    public ConfigTools configTools;
    public MySQL mySQL;
    public SQLUtils SQLU;
    public EconomyUtils economyUtils;


    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        // initialize classes:
        configTools = new ConfigTools(this);
        mySQL = new MySQL();
        SQLU = new SQLUtils();
        economyUtils = new EconomyUtils();
        ShortcutsUtils s = new ShortcutsUtils();

        // register listeners:
        pm.registerEvents(new PlayerMine(this), this);
        pm.registerEvents(new JoinLeave(this), this);
        pm.registerEvents(new PlayerDeath(this), this);

        // Plugin startup logic
        ConfigTools.generateConfig("config.yml");
        FileConfiguration config = ConfigTools.getFileConfig("config.yml");

        String symbol = config.getString("money.moneySymbol");
        Bukkit.getLogger().info("Money symbol: " + symbol);

        try {
            Bukkit.getLogger().info(s.prefix + "Attempting to connect to MySQL!");
            mySQL.connectDB();
        } catch (ClassNotFoundException | SQLException e) {
            Bukkit.getLogger().info(s.prefix + "Database Failed to connect!");
            e.printStackTrace();
        }
        if (mySQL.isConnected()) {
            Bukkit.getLogger().info(s.prefix + "Database Successfully Connected!");
            //create main table:
            dbCreation();
            Bukkit.getLogger().info(s.prefix + "Database Initialised!");
        } else {
            Bukkit.getLogger().severe("Database not connected!");
        }


        // initialise commands
        this.getCommand("bal").setExecutor(new CommandBal());
        this.getCommand("setmoney").setExecutor(new CommandSetMoney());
        this.getCommand("transfer").setExecutor(new CommandTransfer());

        //initialise tab completers
        getCommand("transfer").setTabCompleter(new CommandTransfer(this));
        getCommand("setmoney").setTabCompleter(new CommandSetMoney(this));
        getCommand("bal").setTabCompleter(new CommandBal(this));

        //create the bot
        try {
            Bot.main(new String[]{"OTEwNzg5MjE4NDg4NDM4ODM0.YZX8jw.2FQmRVO37KJFWceaFF-he_d_bdI"});
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        mySQL.disconnectDB();
    }

    void dbCreation() {
        // handles creation of the economy table
        SQLU.createIDTable("economy");
        SQLU.createDBColumn("wallet", "VARCHAR(100)", "economy");
        SQLU.createDBColumn("bank", "VARCHAR(100)", "economy");

        // handles creation of the shops table
        SQLU.createIDTable("shops");
        SQLU.createDBColumn("shopID", "VARCHAR(100)", "shops");
        SQLU.createDBColumn("shopPrice", "VARCHAR(100)", "shops");

        // handles creation of the linked accounts table
        SQLU.createIDTable("accounts");
        SQLU.createDBColumn("discordIGN", "VARCHAR(100)", "accounts");
        SQLU.createDBColumn("minecraftIGN", "VARCHAR(100)", "accounts");
    }
}
