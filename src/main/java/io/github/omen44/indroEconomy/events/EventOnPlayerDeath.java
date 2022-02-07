package io.github.omen44.indroEconomy.events;

import io.github.omen44.indroEconomy.IndroEconomy;
import io.github.omen44.indroEconomy.utils.EconomyUtils;
import io.github.omen44.indroEconomy.datamanager.ConfigTools;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

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
            player.sendMessage(mPrefix + "You have died and lost " + symbol + ((int) (moneyLost)));
            eco.minusWallet(player, (int) moneyLost);

            if (event.getEntity().getKiller() != null && config.getBoolean("money.killerGetsDeathMoney")) {
                Player killer = event.getEntity().getKiller();
                double moneyGained = moneyLost * config.getDouble("money.killerGetsDeathMoneyPercent");
                eco.addWallet(killer, (int) moneyGained);
                killer.sendMessage("You stole " + symbol + ((int) moneyGained));
            }
        } else {
            event.setDeathMessage(player.getName() + " ran out of money");
            player.removeMetadata("deathCausePoverty", IndroEconomy.getInstance());
            eco.setWallet(player, 200);
        }
    }
}
