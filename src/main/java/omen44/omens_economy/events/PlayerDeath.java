package omen44.omens_economy.events;

import omen44.omens_economy.Main;
import omen44.omens_economy.datamanager.ConfigTools;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerDeath implements Listener {

    public Main main;
    public PlayerDeath(Main main) {this.main = main;}

    public FileConfiguration config = ConfigTools.getFileConfig("config.yml");

    @EventHandler
    public void onPlayerDeath(BlockBreakEvent event) {
        //initialise the values needed
        Player player = event.getPlayer();
        int moneyLossPercent = config.getInt("deathLossPercent");
        int wallet = main.economyUtils.getMoney(player, "wallet");
        String symbol = config.getString("moneySymbol");
        float moneyLost = wallet / (100 / moneyLossPercent);

        //reduce their wallet by the percentage
        player.sendMessage("You have died!\n You have lost " + symbol + moneyLost);
        int finalWallet = wallet - (int) moneyLost;
        main.economyUtils.setWallet(player, finalWallet);
    }
}
