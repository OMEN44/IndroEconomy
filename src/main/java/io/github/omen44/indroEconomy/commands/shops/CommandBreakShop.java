package io.github.omen44.indroEconomy.commands.shops;

import io.github.omen44.indroEconomy.IndroEconomy;
import io.github.omen44.indroEconomy.utils.ShortcutsUtils;
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

public class CommandBreakShop implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (label.equalsIgnoreCase("breakshop")) {
                Location location = player.getLocation();

                if (location.getBlock().getType().equals(Material.CHEST)) {
                    Chest chest = (Chest) location.getBlock();

                    NamespacedKey key = new NamespacedKey(IndroEconomy.getPlugin(IndroEconomy.class), "chestshop");
                    PersistentDataContainer container = chest.getPersistentDataContainer();
                    String playerUUID = container.get(key, PersistentDataType.STRING);

                    if (playerUUID == null) {
                        player.sendMessage("The block you are standing on is not a chest shop");
                    } else if (playerUUID.equalsIgnoreCase(player.getUniqueId().toString())) {
                        player.setMetadata("canBreakShop", new FixedMetadataValue(IndroEconomy.getPlugin(IndroEconomy.class), true));
                        player.breakBlock(chest.getBlock());
                    } else {
                        player.sendMessage(ShortcutsUtils.mWarning + "You do not own this chestshop!");
                    }
                }
            }
        }

        return false;
    }
}
