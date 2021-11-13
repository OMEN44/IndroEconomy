package omen44.omens_economy.listeners;

import omen44.omens_economy.Main;
import omen44.omens_economy.utils.ShortcutsUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.TileState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ChestShopPlace implements Listener {
    private boolean shopPlacing = false;
    private int shopID;
    public Main main;

    public ChestShopPlace(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onChestShopPlace(BlockPlaceEvent event) {
        ShortcutsUtils s = new ShortcutsUtils();
        if (!(shopPlacing))
            return;
        if (event.getBlock().getType() != Material.CHEST)
            return;
        if (!(event.getBlock().getState() instanceof TileState))
            return;
        TileState state = (TileState) event.getBlock().getState();
        PersistentDataContainer container = state.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class),
                "shopChest");

        double v = Math.random() * 1000;
        shopID = (int) Math.round(v);

        container.set(key, PersistentDataType.STRING, event.getPlayer().getUniqueId().toString());
        container.set(key, PersistentDataType.INTEGER, shopID);
        state.update();

        event.getPlayer().sendMessage(s.prefix + "Shop Placed! Your shop ID is " + shopID);
        shopPlacing = false;
    }

    public boolean isShopPlacing() {
        return shopPlacing;
    }

    public void setShopPlacing(boolean shopPlacing) {
        this.shopPlacing = shopPlacing;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }
}
