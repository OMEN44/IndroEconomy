package io.github.omen44.indroEconomy;

import io.github.omen44.indroEconomy.commands.economy.*;
import io.github.omen44.indroEconomy.events.EventOnEntityKill;
import io.github.omen44.indroEconomy.events.EventOnPlayerDeath;
import io.github.omen44.indroEconomy.events.EventOnPlayerJoinLeave;
import io.github.omen44.indroEconomy.events.EventOnPlayerMine;
import io.github.omen44.indroEconomy.integerations.EconomyImplementer;
import io.github.omen44.indroEconomy.storage.EconomyStorageUtil;
import io.github.omen44.indroEconomy.tasks.TaskStockUpdate;
import io.github.omen44.indroEconomy.tasks.TaskUpdateFile;
import io.github.omen44.indroEconomy.utils.Lang;
import me.kodysimpson.simpapi.command.CommandManager;
import me.kodysimpson.simpapi.menu.MenuManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class IndroEconomy extends JavaPlugin {
    private static IndroEconomy plugin;

    private static FileConfiguration config;

    private Logger log;
    public static YamlConfiguration LANG;
    public static File LANG_FILE;
    @Override
    public void onEnable() {
        // Set up the MenuManager
        MenuManager.setup(getServer(), this);
        plugin = this;
        log = this.getLogger();
        this.saveDefaultConfig();
        config = this.getConfig();

        loadLang();
        registerCommands();
        registerIntegrations();
        applyBukkitTasks();

        // load accounts from the accountProgram
        try {
            EconomyStorageUtil.loadAccounts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getScheduler().cancelTasks(this);

        try {
            EconomyStorageUtil.saveAccounts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // an instance of the JavaPlugin program

    public static IndroEconomy getInstance() {
        return plugin;
    }
    /**
     * Load the lang.yml file.
     */
    public void loadLang() {
        File lang = new File(getDataFolder(), "lang.yml");
        if (!lang.exists()) {
            try {
                getDataFolder().mkdir();
                lang.createNewFile();
                InputStream defConfigStream = this.getResource("lang.yml");
                if (defConfigStream != null) {
                    InputStreamReader reader = new InputStreamReader(defConfigStream);
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(reader);
                    defConfig.save(lang);
                    Lang.setFile(defConfig);
                    return;
                }
            } catch(IOException e) {
                e.printStackTrace(); // So they notice
                log.severe("[IndroEconomy] Couldn't create language file.");
                log.severe("[IndroEconomy] This is a fatal error. Now disabling");
                this.setEnabled(false); // Without it loaded, we can't send them messages
            }
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
        for(Lang item:Lang.values()) {
            if (conf.getString(item.getPath()) == null) {
                conf.set(item.getPath(), item.getDefault());
            }
        }
        Lang.setFile(conf);
        IndroEconomy.LANG = conf;
        IndroEconomy.LANG_FILE = lang;
        try {
            conf.save(getLangFile());
        } catch(IOException e) {
            log.warning("IndroEconomy: Failed to save lang.yml.");
            log.warning("IndroEconomy: Report this stack trace to <your name>.");
            e.printStackTrace();
        }
    }

    /**
     * Gets the lang.yml config.
     * @return The lang.yml config.
     */
    public YamlConfiguration getLang() {
        return LANG;
    }
    /**
     * Get the lang.yml file.
     * @return The lang.yml file.
     */
    public File getLangFile() {
        return LANG_FILE;
    }

    private void registerCommands() {
        // commands
        PluginManager pm = getServer().getPluginManager();

        try {
            CommandManager.createCoreCommand(this, "eco",
                    "The Economy Module of the Plugin.", "/eco",
                    null,
                    CommandBal.class, CommandOpShop.class, CommandSend.class, CommandSetMoney.class,
                    CommandTransfer.class, CommandStock.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        //register events
        pm.registerEvents(new EventOnPlayerJoinLeave(), this);
        pm.registerEvents(new EventOnPlayerDeath(), this);
        pm.registerEvents(new EventOnPlayerMine(), this);
        pm.registerEvents(new EventOnEntityKill(), this);
    }

    private void registerIntegrations() {
        // checks if vault exists
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
            Bukkit.getServer().getServicesManager().register(Economy.class, new EconomyImplementer(), this, ServicePriority.Highest);
            this.getLogger().info("Vault Found, integrating with it.");
        } else {
            this.getLogger().info("Vault could not be found, disabling Vault Integration.");
        }
    }

    private void applyBukkitTasks() {
        final int period = config.getInt("stock.openTime");
        new TaskStockUpdate(this).runTaskTimer(this, 60L, period);
        new TaskUpdateFile(this).runTaskTimer(this, 20L, 6000L);
    }

    public static void setSavedConfig(FileConfiguration config) {
        IndroEconomy.config = config;
    }

    public FileConfiguration getSavedConfig() {
        return config;
    }
}
