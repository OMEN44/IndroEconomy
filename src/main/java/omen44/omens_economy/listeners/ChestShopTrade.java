package omen44.omens_economy.listeners;

import omen44.omens_economy.Main;
import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.utils.EconomyUtils;
import omen44.omens_economy.utils.ShortcutsUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.TileState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class ChestShopTrade implements Listener {
    public EconomyUtils economyUtils;
    public ChestShopPlace csp;
    ConfigTools configTools;
    ShortcutsUtils s;

    public Main main;
    public ChestShopTrade(Main main) {this.main = main;}

    @EventHandler
    public void onChestShopOpen(PlayerInteractEvent event, InventoryClickEvent event2) {
        if (!event.hasBlock())
            return;
        if (event.getClickedBlock().getType() != Material.CHEST)
            return;
        if (!(event.getClickedBlock().getState() instanceof TileState))
            return;

        TileState state = (TileState) event.getClickedBlock().getState();
        PersistentDataContainer container = state.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class),
                "shopChest");

        if (!container.has(key, PersistentDataType.STRING))
            return;

        FileConfiguration config = configTools.getFileConfig("config.yml");
        String moneySymbol = config.getString("money.moneySymbol");

        if (event.getPlayer().getUniqueId().toString().equalsIgnoreCase(
                container.get(key, PersistentDataType.STRING))) { //if the person accessing it is the owner
            if (isAdding(event2)) {
                ItemStack addedItem = event2.getCurrentItem();
                int shopPrice = 500; // this should be the shop price
                ItemMeta itemMeta = addedItem.getItemMeta();
                itemMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, shopPrice);
                addedItem.setItemMeta(itemMeta);
                event.getPlayer().sendMessage(s.prefix + "Added " + addedItem.getType() +
                        "to Shop " + csp.getShopID() + " for " + moneySymbol + shopPrice);
            }
        } else {
            int wallet = economyUtils.getMoney(event.getPlayer(), "wallet");

        }
    }

    public boolean isAdding(InventoryClickEvent event) {
        if (!Objects.equals(event.getClickedInventory().getType().toString(), "CHEST")
                && event.getCursor() != null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isRemoving(InventoryClickEvent event) {
        if (!Objects.equals(event.getClickedInventory().getType().toString(), "CHEST")
                && event.getCursor() == null) {
            return false;
        } else {
            return true;
        }
    }
}
