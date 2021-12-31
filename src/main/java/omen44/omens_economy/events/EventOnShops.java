package omen44.omens_economy.events;

import omen44.omens_economy.Main;
import omen44.omens_economy.utils.EconomyUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EventOnShops implements Listener {
    @EventHandler
    public void onChestShopPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() != Material.CHEST) {
            return;
        }
        if (!(event.getBlock().getState() instanceof TileState)) {
            return;
        }

        TileState state = (TileState) event.getBlock().getState();
        PersistentDataContainer container = state.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class),
                "chestShop");

        container.set(key, PersistentDataType.INTEGER, 3);

        state.update();
    }

    @EventHandler
    public void onChestShopInteract(InventoryClickEvent event) {
        if (event.getInventory().getType() != InventoryType.CHEST) {
            return;
        }
        EconomyUtils eco = new EconomyUtils();
        Player p = (Player) event.getWhoClicked();

        int money = eco.getMoney(p, "wallet");
        final int itemPrice = 54;
    }

}
