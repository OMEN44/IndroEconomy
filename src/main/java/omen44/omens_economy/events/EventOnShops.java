package omen44.omens_economy.events;

import omen44.omens_economy.Main;
import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.utils.SQLeconomy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.TileState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static omen44.omens_economy.utils.ShortcutsUtils.mNormal;
import static omen44.omens_economy.utils.ShortcutsUtils.mWarning;

public class EventOnShops implements Listener {
    @EventHandler
    public void onShopCreate(BlockPlaceEvent event) {
        if (event.getBlock().getType() != Material.CHEST) return;
        if (!(event.getBlock().getState() instanceof TileState)) return;

        ItemMeta itemMeta = event.getItemInHand().getItemMeta();
        if (itemMeta == null) return;
        PersistentDataContainer container1 = itemMeta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "chestprice");

        if (!(container1.has(key, PersistentDataType.INTEGER) && container1.has(key, PersistentDataType.STRING))) return;
        Integer shopCost = container1.get(key, PersistentDataType.INTEGER);
        String ownerUUID = container1.get(key, PersistentDataType.STRING);

        if (shopCost == null || ownerUUID == null) return; // checks if keys exist

        TileState state = (TileState) event.getBlock().getState();
        PersistentDataContainer container = state.getPersistentDataContainer();

        container.set(key, PersistentDataType.INTEGER, shopCost);
        container.set(key, PersistentDataType.STRING, ownerUUID);
        state.update();
        event.getPlayer().sendMessage(mNormal + "Successfully generated a chestshop");
    }

    @EventHandler
    public void onInventoryInteract(final InventoryClickEvent event) {
        ConfigTools configTools = new ConfigTools();
        FileConfiguration config = configTools.getConfig("config.yml");
        String symbol = config.getString("money.moneySymbol");

        // item check
        if (!(event.getWhoClicked() instanceof Player)) return;
        final ItemStack current = event.getCurrentItem();
        final ItemStack cursor = event.getCursor();
        final Player p = (Player) event.getWhoClicked();
        InventoryHolder ih = event.getInventory().getHolder();
        SQLeconomy eco = new SQLeconomy();


        if (ih instanceof Chest || ih instanceof DoubleChest) {
            Chest chest = (Chest) event.getInventory().getHolder();
            if (chest == null) return;
            PersistentDataContainer container = chest.getPersistentDataContainer();
            NamespacedKey nsk = new NamespacedKey(Main.getPlugin(Main.class), "chestprice");

            String ownerUUID = container.get(nsk, PersistentDataType.STRING);
            Integer shopCost = container.get(nsk, PersistentDataType.INTEGER);

            if (shopCost == null || ownerUUID == null) return;
            if (ownerUUID.equals(p.getUniqueId().toString())) return;

            Player owner = Bukkit.getPlayer(ownerUUID);
            if (owner == null) return;
            String ownerDisplayName = owner.getDisplayName();

            // processing costs
            int wallet = eco.getWallet(p);
            if (current != null && cursor != null) {
                if (current.getType().equals(Material.AIR)) {
                    // player put item to inventory
                     p.sendMessage("<" + ownerDisplayName + ">" +
                             mNormal + "Stop that! I'm not taking your rubbish!");
                     event.setCancelled(true);
                } else if (!current.getType().equals(Material.AIR) && cursor.getType().equals(Material.AIR)) {
                    // player take item from inventory
                    if (wallet >= shopCost) {
                        p.sendMessage("<" + ownerDisplayName + ">" +
                                mNormal + " That'll be " + symbol + shopCost);
                        eco.minusWallet(p, shopCost);
                        eco.addWallet(owner, shopCost);
                        owner.sendMessage(p.getName() + mNormal + "has bought from your shop");
                    } else {
                        p.sendMessage("<" + ownerDisplayName + ">" +
                                mNormal + " You know, you have to pay for that, right?");
                        event.setCancelled(true);
                    }
                } else if (!current.getType().equals(Material.AIR) && !cursor.getType().equals(Material.AIR)) {
                    // player swap item in inventory
                    p.sendMessage("<" + ownerDisplayName + ">"
                            + mNormal + " Stop that! I'm not taking your rubbish!");
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onChestBreak(BlockBreakEvent event) {
        if (event.getBlock() instanceof Chest) {
            Player p = event.getPlayer();
            Chest chest = (Chest) event.getBlock();
            PersistentDataContainer container = chest.getPersistentDataContainer();
            NamespacedKey nsk = new NamespacedKey(Main.getPlugin(Main.class), "chestprice");

            String ownerUUID = container.get(nsk, PersistentDataType.STRING);
            Integer shopCost = container.get(nsk, PersistentDataType.INTEGER);

            if (shopCost == null || ownerUUID == null) return;
            if (p.isOp()) {
                event.setCancelled(false);
                return;
            }
            event.setCancelled(true);
            if (ownerUUID.equals(p.getUniqueId().toString())) {
                p.sendMessage(mWarning + "Use /breakshop standing on top of the chest to break the chestshop");
            } else {
                p.sendMessage(mWarning + "Only the owner of this shop can break this chestshop");
            }
        }
    }
}
