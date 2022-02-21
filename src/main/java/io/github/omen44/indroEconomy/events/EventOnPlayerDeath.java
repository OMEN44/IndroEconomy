package io.github.omen44.indroEconomy.events;

import io.github.omen44.indroEconomy.IndroEconomy;
import io.github.omen44.indroEconomy.utils.EconomyUtils;
import io.github.omen44.indroEconomy.utils.Lang;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class EventOnPlayerDeath implements Listener {
    EconomyUtils eco = new EconomyUtils();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        FileConfiguration config = IndroEconomy.getInstance().getSavedConfig();
        final String symbol = config.getString("moneySymbol");
        final int defaultAmount = config.getInt("money.defaultMoney");

        //initialise the values needed
        Player player = event.getEntity();
        boolean deathCausePoverty = false;
        if (player.hasMetadata("deathCausePoverty")) {
            List<MetadataValue> keys = player.getMetadata("deathCausePoverty");
            for (MetadataValue key : keys) {
                if (key.getOwningPlugin() == IndroEconomy.getInstance()) {
                    deathCausePoverty = true;
                    break;
                }
            }
        }

        if (!deathCausePoverty) {
            int wallet = eco.getWallet(player);
            double moneyLost = 0;
            if (wallet > 0) {
                moneyLost = wallet * (config.getInt("money.deathLossPercent") / 100.0);
            }

            //reduce their wallet by the percentage
            player.sendMessage(Lang.TITLE + "You have died and lost " + eco.format((int) moneyLost));
            eco.minusWallet(player, (int) moneyLost);

            if (event.getEntity().getKiller() != null && config.getBoolean("money.killerGetsDeathMoney")) {
                Player killer = event.getEntity().getKiller();
                int moneyGained = (int) (moneyLost * config.getDouble("money.killerGetsDeathMoneyPercent"));
                eco.addWallet(killer, moneyGained);
                String formatted = eco.format(moneyGained);
                killer.sendMessage(String.format("%s You stole %s from %s", Lang.TITLE, formatted, player.getName()));
            }
        } else if (config.getBoolean("deathByPoverty")) {
            event.setDeathMessage(player.getName() + " ran out of money");
            player.removeMetadata("deathCausePoverty", IndroEconomy.getInstance());
            player.sendMessage(Lang.TITLE + "Resetting your wallet and bank, since you ran out of money!");
            eco.setWallet(player, defaultAmount);
            eco.setBank(player, 0);
        }
    }
}
