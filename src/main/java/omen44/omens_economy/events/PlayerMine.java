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

public class PlayerMine implements Listener {

    public Main main;
    public PlayerMine(Main main) {this.main = main;}

    public FileConfiguration config = ConfigTools.getFileConfig("config.yml");

    @EventHandler
    public void onPlayerMine(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String block = event.getBlock().getType().toString();
        String symbol = config.getString("moneySymbol");

        List<String> blocks = new ArrayList<>(config.getConfigurationSection("blocks").getKeys(false));

        if (blocks.contains(block) &&
                !(player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH))) {

            List<String> drops = new ArrayList<>(config.getStringList("blocks." + block));
            player.sendMessage(drops.toString());

            Random random = new Random();
            String drop = drops.get(random.nextInt(drops.size()));
            int amount = main.economyUtils.getMoney(player, "Wallet") + Integer.parseInt(drop);

            player.sendMessage("you got " + symbol + drop);
            main.economyUtils.setWallet(player, amount);
        }

    }
}
