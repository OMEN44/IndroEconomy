package io.github.omen44.IndroEconomy.commands.opShop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class OPShopInv implements InventoryHolder {
    private final Inventory inv;

    public OPShopInv() {
        inv = Bukkit.createInventory(this, 9, ChatColor.YELLOW + "OpShop");
        init();
    }

    private void init() {
        //row 1
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, createItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE,"Hover over an item",1, ""));
        }
        inv.setItem(3, createItem(Material.ENCHANTED_BOOK, "Tier 1", 1,
                ChatColor.BOLD + "Costs $2,500\n" +
                        "25% chance of a over-leveled enchant.\n" +
                        "25% chance for an extra enchant, for a max of 3"));
        inv.setItem(4, createItem(Material.ENCHANTED_BOOK,"Tier 2",1,
                ChatColor.BOLD + "Costs $16,500\n" +
                        "50% chance of a over-leveled enchant.\n" +
                        "30% chance for an extra enchant, for a max of 4"));
        inv.setItem(5, createItem(Material.ENCHANTED_BOOK, "Tier 3", 1,
                ChatColor.BOLD + "Costs $24,550\n" +
                        "75% chance of a over-leveled enchant.\n" +
                        "40% chance for an extra enchant, for a max of 5."));

    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    public ItemStack createItem(Material material, String name, Integer num, String lore) {
        ItemStack item = new ItemStack(material, num);
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) return null;

        itemMeta.setDisplayName(name);
        if (!(lore.equalsIgnoreCase(""))) {
            String[] loreList = lore.split("\n");
            ArrayList<String> itemLore = new ArrayList<>(Arrays.asList(loreList));
            itemMeta.setLore(itemLore);
        }

        item.setItemMeta(itemMeta);
        return item;
    }
}
