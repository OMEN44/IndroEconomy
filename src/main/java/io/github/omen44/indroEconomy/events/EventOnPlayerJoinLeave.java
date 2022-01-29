package io.github.omen44.indroEconomy.events;

import io.github.omen44.indroEconomy.datamanager.ConfigTools;
import io.github.omen44.indroEconomy.utils.JsonSaver;
import io.github.omen44.indroEconomy.utils.SQLUtils;
import io.github.omen44.indroEconomy.utils.SQLeconomy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import static io.github.omen44.indroEconomy.utils.ShortcutsUtils.mNormal;
import static io.github.omen44.indroEconomy.utils.ShortcutsUtils.mPrefix;

public class EventOnPlayerJoinLeave implements Listener {
    SQLeconomy eco = new SQLeconomy();
    ConfigTools configTools = new ConfigTools();
    FileConfiguration config = configTools.getConfig("config.yml");

    private final String host = config.getString("database.host");
    private final String port = config.getString("database.port");
    private final String database = config.getString("database.database");
    private final String username = config.getString("database.username");
    private final String password = config.getString("database.password");

    SQLUtils sqlUtils = new SQLUtils(database, host, port, username, password);
    HashMap<UUID, LocalDateTime> cooldown = new HashMap<>();

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = configTools.getConfig("config.yml");
        event.setJoinMessage(ChatColor.YELLOW + "Welcome to IndroCraft!");

        // getting config values
        String symbol = config.getString("money.moneySymbol");
        int defaultMoney = config.getInt("money.defaultAmount");

        // creating a player if they don't exist
        if (!player.hasPlayedBefore()) {
            sqlUtils.createRow("UUID", event.getPlayer().getUniqueId().toString(), "economy");
            eco.setWallet(player, defaultMoney);
            eco.setBank(player, 0);
            player.sendMessage(mPrefix + "You start with " + symbol + defaultMoney);
        }
        dailyRewardtask(player);
    }


    public void dailyRewardtask(Player player) {
        // initialising values
        FileConfiguration config = configTools.getConfig("config.yml");
        String symbol = config.getString("money.moneySymbol");
        LocalDateTime currentTime = LocalDateTime.now();
        UUID playerUUID = player.getUniqueId();
        JsonSaver jsonSaver = new JsonSaver();
        int days;
        try {
            days = Integer.parseInt(jsonSaver.getString(playerUUID.toString(), "daily"));
        } catch (NumberFormatException e) {
            Bukkit.getLogger().warning("JSON file returned null, assuming 0.");
            days = 0;
        }

        // check if they joined
        LocalDateTime userCheckInTime = null;
        final boolean userExistsOnCooldown = cooldown.containsKey(playerUUID);
        if (userExistsOnCooldown) {
            userCheckInTime = cooldown.get(playerUUID);
        }

        // checks for daily rewards

        if (days == 0) { // does the player exist?
            jsonSaver.setData(playerUUID, 1, "daily");
            cooldown.put(playerUUID, LocalDateTime.from(currentTime.plusHours(30L))); // remember to set this to .plusHours(30L)

            eco.addWallet(player, 200);
            player.sendMessage(mNormal + "You have earned $200 for joining for the first time.");
            return;
        }

        if (userExistsOnCooldown && userCheckInTime.isAfter(currentTime)) {
             jsonSaver.setData(playerUUID, 1, "daily");
             player.sendMessage(mNormal + "Daily streak broken! Resetting to day 1.");
             return;
        }

        if (userExistsOnCooldown &&
                userCheckInTime.isBefore(currentTime) && userCheckInTime.isAfter(currentTime.minusHours(6L))) { // remember to set this to .minusHours(6L)
            days += 1;
            jsonSaver.setData(playerUUID, days, "daily");

            int nextAmount = 3 * days + 200;
            eco.addWallet(player, nextAmount);
            player.sendMessage(mNormal + "" + days + " day streak achieved! " + symbol + nextAmount + "earned.");
        }
    }
}
