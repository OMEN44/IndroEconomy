package omen44.omens_economy.commands.shops;

import omen44.omens_economy.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static omen44.omens_economy.utils.ShortcutsUtils.mWarning;

public class CommandBreakShop implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (label.equalsIgnoreCase("breakshop")) {
                Location location = player.getLocation();

                if (location.getBlock().getType().equals(Material.CHEST)) {
                    Chest chest = (Chest) location.getBlock();

                    NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "chestshop");
                    PersistentDataContainer container = chest.getPersistentDataContainer();
                    String playerUUID = container.get(key, PersistentDataType.STRING);

                    if (playerUUID == null) {
                        player.sendMessage("The block you are standing on is not a chest shop");
                    } else if (playerUUID.equalsIgnoreCase(player.getUniqueId().toString())) {
                        player.setMetadata("canBreakShop", new FixedMetadataValue(Main.getPlugin(Main.class), true));
                        player.breakBlock(chest.getBlock());
                    } else {
                        player.sendMessage(mWarning + "You do not own this chestshop!");
                    }
                }
            }
        }

        return false;
    }
}
