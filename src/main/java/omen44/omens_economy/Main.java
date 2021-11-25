package omen44.omens_economy;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import omen44.omens_economy.commands.economy.CommandBal;
import omen44.omens_economy.commands.economy.CommandSetMoney;
import omen44.omens_economy.commands.economy.CommandTransfer;
import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.datamanager.MySQL;
import omen44.omens_economy.discordLink.Bot;
import omen44.omens_economy.events.EventOnPlayerDeath;
import omen44.omens_economy.events.EventOnPlayerJoinLeave;
import omen44.omens_economy.events.EventOnPlayerMine;
import omen44.omens_economy.utils.EconomyUtils;
import omen44.omens_economy.utils.SQLUtils;
import omen44.omens_economy.utils.ShortcutsUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends JavaPlugin implements Listener {
    private MySQL SQL = new MySQL();
    public static Connection connection;
    ShortcutsUtils s = new ShortcutsUtils();
    SQLUtils sqlUtils;
    EconomyUtils eco = new EconomyUtils();

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        // Plugin startup logic
        this.saveDefaultConfig();
        FileConfiguration config = ConfigTools.getFileConfig("config.yml");

        // initialize classes:
        connection = SQL.getConnection();
        sqlUtils = new SQLUtils(connection);

        if (connection != null) {
            Bukkit.getLogger().info(s.prefix + "Database Successfully Connected!");
            //create table:
            dbCreation();
            Bukkit.getLogger().info(s.prefix + "Database Initialised!");
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

        //initialise tab completers
        getCommand("transfer").setTabCompleter(new CommandTransfer( ));
        getCommand("setmoney").setTabCompleter(new CommandSetMoney());
        getCommand("bal").setTabCompleter(new CommandBal());

        //register events
        pm.registerEvents(new EventOnPlayerJoinLeave(), this);
        pm.registerEvents(new EventOnPlayerDeath(), this);
        pm.registerEvents(new EventOnPlayerMine(), this);

        //create the discord bot
        try {
            Bot.main(new String[] {config.getString("discord.token")});
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bot bot = new Bot();
        bot.shutdownBot();
        MySQL.closeConnection(connection);
    }

    void dbCreation() {
        // handles creation of the economy table
        sqlUtils.createDBTable("economy", "accountID");
        sqlUtils.createDBColumn("wallet", "VARCHAR(100)", "economy");
        sqlUtils.createDBColumn("bank", "VARCHAR(100)", "economy");

        // handles creation of the shops table
        sqlUtils.createDBTable("shops", "accountID");
        sqlUtils.createDBColumn("shopID", "VARCHAR(100)", "shops");
        sqlUtils.createDBColumn("shopPrice", "VARCHAR(100)", "shops");

        // handles creation of the linked accounts table
        sqlUtils.createAccountTable();
        sqlUtils.createDBColumn("discordIGN", "VARCHAR(100)", "accounts");
        sqlUtils.createDBColumn("minecraftIGN", "VARCHAR(100)", "accounts");
    }

    public MySQL getSQL() {
        return SQL;
    }

    public void setSQL(MySQL SQL) {
        this.SQL = SQL;
    }
}
