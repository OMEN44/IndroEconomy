package omen44.omens_economy;

import omen44.omens_economy.commands.economy.CommandBal;
import omen44.omens_economy.commands.economy.CommandSetMoney;
import omen44.omens_economy.commands.economy.CommandTransfer;
import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.datamanager.MySQL;
import omen44.omens_economy.discordLink.Bot;
import omen44.omens_economy.discordLink.CommandRegister;
import omen44.omens_economy.events.JoinLeave;
import omen44.omens_economy.events.PlayerDeath;
import omen44.omens_economy.events.PlayerMine;
import omen44.omens_economy.events.WhitelistRegister;
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
    public CommandRegister cr;
    public WhitelistRegister wr;

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        // initialize classes:
        configTools = new ConfigTools();
        mySQL = new MySQL();
        SQLU = new SQLUtils(this);
        economyUtils = new EconomyUtils(this);
        cr = new CommandRegister(this);
        wr = new WhitelistRegister(this);
        ShortcutsUtils s = new ShortcutsUtils();

        // register listeners:
        pm.registerEvents(new PlayerMine(this), this);
        pm.registerEvents(new JoinLeave(this), this);
        pm.registerEvents(new PlayerDeath(this), this);

        // Plugin startup logic
        this.saveDefaultConfig();
        FileConfiguration config = ConfigTools.getFileConfig("config.yml");

        String symbol = config.getString("money.moneySymbol");
        Bukkit.getLogger().info("Money symbol: " + symbol);


        Bukkit.getLogger().info(s.prefix + "Attempting to connect to MySQL!");
        try {
            mySQL.connect();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        if (mySQL.isConnected()) {
            Bukkit.getLogger().info(s.prefix + "Database Successfully Connected!");
            //create table:
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
        getCommand("transfer").setTabCompleter(new CommandTransfer( ));
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
        mySQL.disconnect();
    }

    void dbCreation() {
        if (mySQL.getConnection() == null) {
            Bukkit.getLogger().severe("Database failed to connect!");
            return;
        }
        // handles creation of the economy table
        SQLU.createDBTable("economy", "accountID");
        SQLU.createDBColumn("wallet", "VARCHAR(100)", "economy");
        SQLU.createDBColumn("bank", "VARCHAR(100)", "economy");

        // handles creation of the shops table
        SQLU.createDBTable("shops", "accountID");
        SQLU.createDBColumn("shopID", "VARCHAR(100)", "shops");
        SQLU.createDBColumn("shopPrice", "VARCHAR(100)", "shops");

        // handles creation of the linked accounts table
        SQLU.createIDTable("accounts","discordIGN", "VARCHAR(100)");
        SQLU.createDBColumn("minecraftIGN", "VARCHAR(100)", "accounts");
    }
}
