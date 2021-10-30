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

public class PlayerMine implements Listener {

    public Main main;
    public PlayerMine(Main main) {this.main = main;}

    public FileConfiguration config = ConfigTools.getFileConfig("config.yml");

    @EventHandler
    public void onPlayerMine(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        List<String> blocks = new ArrayList<>(config.getConfigurationSection("blocks").getKeys(false));

        if (blocks.contains(block.getType().toString()) &&
                !(player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH))) {
            main.economyUtils.setWallet(player, "");
        }

    }
}
