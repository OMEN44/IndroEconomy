package omen44.omens_economy;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import omen44.omens_economy.commands.economy.CommandBal;
import omen44.omens_economy.commands.economy.CommandSetMoney;
import omen44.omens_economy.commands.economy.CommandTransfer;
import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.datamanager.MySQL;
import omen44.omens_economy.discordLink.Bot;
import omen44.omens_economy.discordLink.CommandRegister;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends JavaPlugin implements Listener {
    public MySQL SQL;
    public SQLUtils sqlUtils = new SQLUtils(this);
    public ShortcutsUtils s = new ShortcutsUtils();
    public EconomyUtils eco = new EconomyUtils();
    public CommandRegister cr = new CommandRegister(this);

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        // Plugin startup logic
        this.saveDefaultConfig();
        FileConfiguration config = ConfigTools.getFileConfig("config.yml");

        // initialize classes:
        this.SQL = new MySQL();

        try {
            SQL.connect();
        } catch (ClassNotFoundException | SQLException e) {
            //e.printStackTrace();
            //this means either login info is incorrect, or not used a database
            Bukkit.getLogger().info("Database not connected");
        }
        if (SQL.isConnected()) {
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
        this.getCommand("setmoney").setExecutor(new CommandSetMoney(this));
        this.getCommand("transfer").setExecutor(new CommandTransfer());

        //initialise tab completers
        getCommand("transfer").setTabCompleter(new CommandTransfer( ));
        getCommand("setmoney").setTabCompleter(new CommandSetMoney(this));
        getCommand("bal").setTabCompleter(new CommandBal(this));


        // register listeners:
        pm.registerEvents(new Main(), this);

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
        SQL.disconnect();
    }

    void dbCreation() {
        if (SQL.getConnection() == null) {
            Bukkit.getLogger().severe("Database failed to connect!");
            return;
        }
        // handles creation of the economy table
        sqlUtils.createDBTable("economy", "accountID");
        sqlUtils.createDBColumn("wallet", "VARCHAR(100)", "economy");
        sqlUtils.createDBColumn("bank", "VARCHAR(100)", "economy");

        // handles creation of the shops table
        sqlUtils.createDBTable("shops", "accountID");
        sqlUtils.createDBColumn("shopID", "VARCHAR(100)", "shops");
        sqlUtils.createDBColumn("shopPrice", "VARCHAR(100)", "shops");

        // handles creation of the linked accounts table
        sqlUtils.createIDTable("accounts","discordIGN", "VARCHAR(100)");
        sqlUtils.createDBColumn("minecraftIGN", "VARCHAR(100)", "accounts");
    }

    //events go here

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        FileConfiguration config = ConfigTools.getFileConfig("config.yml");

        sqlUtils.createRow("UUID", uuid, "players");
        event.setJoinMessage(ChatColor.YELLOW + "Welcome to IndroCraft!");
        if (!player.hasPlayedBefore()) {
            eco.setBank(player, config.getInt("defaultAmount"));
            player.sendMessage(s.prefix + "You start with " + config.getString("money.moneySymbol") + config.getInt("money.defaultAmount"));
        }
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        ShortcutsUtils s = new ShortcutsUtils();
        FileConfiguration config = ConfigTools.getFileConfig("config.yml");

        //initialise the values needed
        Player player = event.getEntity().getPlayer();
        double moneyLossPercent = config.getInt("money.deathLossPercent") / 100.0;
        int wallet = eco.getMoney(player, "wallet");
        String symbol = config.getString("money.moneySymbol");
        double moneyLost = wallet * moneyLossPercent;
        int finalWallet = wallet - (int) moneyLost;

        //reduce their wallet by the percentage
        player.sendMessage(s.prefix + "You have died!\n" + s.prefix + "You have lost " + symbol + moneyLost);
        eco.setWallet(player, finalWallet);
    }

    @EventHandler
    public void onPlayerMine(BlockBreakEvent event) {
        FileConfiguration config = ConfigTools.getFileConfig("config.yml");
        Player player = event.getPlayer();
        String block = event.getBlock().getType().toString();
        String symbol = config.getString("money.moneySymbol");


        List<String> blocks = new ArrayList<>(config.getConfigurationSection("blocks").getKeys(false));
        if (blocks.contains(block) &&
                !(player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH))) {

            List<String> drops = new ArrayList<>(config.getStringList("blocks." + block));

            Random random = new Random();
            String drop = drops.get(random.nextInt(drops.size()));
            int amount = eco.getMoney(player, "Wallet") + Integer.parseInt(drop);
            eco.setWallet(player, amount);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GREEN + "+" + symbol + Integer.parseInt(drop)));
        }
    }
}
