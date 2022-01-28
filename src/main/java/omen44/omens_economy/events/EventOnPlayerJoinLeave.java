package omen44.omens_economy.events;

import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.utils.SQLeconomy;
import omen44.omens_economy.utils.SQLUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static omen44.omens_economy.utils.ShortcutsUtils.mPrefix;

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

    //HashMap<UUID, LocalDateTime> cooldown = new HashMap<>();

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

        /*
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime tomorrow = LocalDateTime.now().plusHours(1L);

        Configuration daily = configTools.getConfig("daily.yml");
        UUID playerUUID = player.getUniqueId();

        LocalDateTime userCheckInTime = null;
        int days = daily.getInt("daily." + playerUUID);
        final boolean userExistsOnCooldown = cooldown.containsKey(playerUUID);
        if (userExistsOnCooldown) {
            userCheckInTime = cooldown.get(playerUUID);
        }

        // checks for daily rewards

        if (days == 0) { // does the player exist?
            daily.set("daily." + playerUUID, 1);
            cooldown.put(playerUUID, LocalDateTime.from(currentTime.plusHours(30L)));

            eco.addWallet(player, 200);
            player.sendMessage(mNormal + "You have earned $200 for joining for the first time.");
            return;
        }

        if (userExistsOnCooldown && userCheckInTime.isAfter(currentTime)) {
             daily.set("daily." + playerUUID, 1);
             player.sendMessage(mNormal + "Daily streak broken! Resetting to day 1.");
             return;
        }

        if (userExistsOnCooldown &&
                userCheckInTime.isBefore(currentTime) && userCheckInTime.isAfter(currentTime.minusHours(6L))) {
            days += 1;
            daily.set("daily." + playerUUID, days);

            int nextAmount = 3 * days + 200;
            eco.addWallet(player, nextAmount);
            player.sendMessage(mNormal + "" + days + " day streak achieved! " + symbol + nextAmount + "earned.");
        }
         */
    }
}
