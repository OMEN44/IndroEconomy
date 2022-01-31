package io.github.omen44.indroEconomy.events;

import io.github.omen44.indroEconomy.utils.EconomyUtils;
import io.github.omen44.indroEconomy.datamanager.ConfigTools;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static io.github.omen44.indroEconomy.utils.ShortcutsUtils.mPrefix;

public class EventOnPlayerDeath implements Listener {
    EconomyUtils eco = new EconomyUtils();
    ConfigTools configTools = new ConfigTools();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        FileConfiguration config = configTools.getConfig("config.yml");
        String symbol = config.getString("money.moneySymbol");

        //initialise the values needed
        Player player = event.getEntity();
        int wallet = eco.getWallet(player);
        double moneyLost = wallet-(config.getInt("money.deathLossPercent") / 100.0);
        wallet -= moneyLost;

        //reduce their wallet by the percentage
        player.sendMessage(mPrefix + "You have died and lost " + symbol + moneyLost);
        eco.setWallet(player, wallet);
    }
}
