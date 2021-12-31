package omen44.omens_economy.events;

import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.utils.EconomyUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static omen44.omens_economy.utils.ShortcutsUtils.mImportant;
import static omen44.omens_economy.utils.ShortcutsUtils.mPrefix;

public class EventOnPlayerDeath implements Listener {
    EconomyUtils eco = new EconomyUtils();
    ConfigTools configTools = new ConfigTools();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        FileConfiguration config = configTools.getConfig("config.yml");
        if (!config.getBoolean("money.deathLoss")) return;

        //initialise the values needed
        Player player = event.getEntity();
        String symbol = config.getString("money.moneySymbol");
        int playerWallet = eco.getMoney(player, "wallet");

        //reduce their wallet by the percentage
        player.sendMessage(mPrefix + mImportant + "You have died!\n" +
                mPrefix + mImportant + "You have lost " + symbol + playerWallet);
        eco.setWallet(player, 0);

        if (event.getEntity().getKiller() != null && config.getBoolean("money.killerGainMoney")) {
            Player killer = event.getEntity().getKiller();
            float killerWallet = eco.getMoney(killer, "wallet");
            int dropPercent = config.getInt("money.killerGainMoneyPercentage");
            int killerEarned = (int) (playerWallet * (dropPercent/100F));
            int finalWallet = (int) (killerWallet + killerEarned);

            eco.setWallet(killer, finalWallet);
            killer.sendMessage(mPrefix + mImportant + "You have killed " + player.getName() + ", and earned " + symbol + killerEarned);
        }
    }
}
