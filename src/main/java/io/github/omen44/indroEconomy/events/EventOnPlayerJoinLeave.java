package io.github.omen44.indroEconomy.events;

import io.github.omen44.indroEconomy.IndroEconomy;
import io.github.omen44.indroEconomy.utils.EconomyUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static io.github.omen44.indroEconomy.utils.ShortcutsUtils.mPrefix;

public class EventOnPlayerJoinLeave implements Listener {
    EconomyUtils eco = new EconomyUtils();
    // HashMap<String, LocalDateTime> cooldown = new HashMap<>();

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = IndroEconomy.getInstance().getSavedConfig();

        // getting config values
        int defaultMoney = config.getInt("money.defaultAmount");

        // creating a player if they don't exist
        if (!player.hasPlayedBefore()) {
            player.sendMessage(mPrefix + "You start with " + eco.format(defaultMoney));
        }
        if (!eco.hasAccount(player)) {
            eco.createAccount(player);
        }
    }
}
