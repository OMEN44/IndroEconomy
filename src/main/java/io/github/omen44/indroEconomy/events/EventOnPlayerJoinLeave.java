package io.github.omen44.indroEconomy.events;

import io.github.omen44.indroEconomy.datamanager.ConfigTools;
import io.github.omen44.indroEconomy.utils.EconomyUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalDateTime;
import java.util.HashMap;

import static io.github.omen44.indroEconomy.utils.ShortcutsUtils.mPrefix;

public class EventOnPlayerJoinLeave implements Listener {
    EconomyUtils eco = new EconomyUtils();
    ConfigTools configTools = new ConfigTools();
    HashMap<String, LocalDateTime> cooldown = new HashMap<>();

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = configTools.getConfig("config.yml");

        // getting config values
        String symbol = config.getString("money.moneySymbol");
        int defaultMoney = config.getInt("money.defaultAmount");

        // creating a player if they don't exist
        if (!player.hasPlayedBefore() && !eco.hasAccount(player)) {
            eco.createAccount(player);
            player.sendMessage(mPrefix + "You start with " + symbol + defaultMoney);
        }
        // dailyRewardtask(player);
    }

    /*
    public void dailyRewardtask(Player player) {
        // initialising values
        FileConfiguration config = configTools.getConfig("config.yml");
        String symbol = config.getString("money.moneySymbol");
        LocalDateTime currentTime = LocalDateTime.now();
        String playerUUID = player.getUniqueId().toString();

        int days;
        try {
            days = jsonSaver.getInt(playerUUID, "daily");
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
     */
}
