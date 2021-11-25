package omen44.omens_economy.events;

import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.utils.EconomyUtils;
import omen44.omens_economy.utils.ShortcutsUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class EventOnPlayerDeath implements Listener {
    EconomyUtils eco = new EconomyUtils();
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
}
