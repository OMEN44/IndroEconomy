package io.github.omen44.indroEconomy.menus;

import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.heads.SkullCreator;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Material.RED_STAINED_GLASS_PANE;

public class MysteryMenu extends Menu {
    public MysteryMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return ChatColor.GOLD + "Mystery Box Menu";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {
        int clicks = 3;
        Player player = playerMenuUtility.getOwner();
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType().equals(RED_STAINED_GLASS_PANE)) {
            player.closeInventory();
        } else {
            int currentItemIndex = e.getCurrentItem().getItemMeta().getCustomModelData();
            String openedPresentID = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmZlNzMyYjNlY2IyZmFiYzAzOGZiMDZkYjhjNTNhN2ZmYjAzMGRiOTI1NDRlMWIyMjU2ZjAxY2IyZWI4MjJiNyJ9fX0=";
            inventory.setItem(currentItemIndex, SkullCreator.itemFromBase64(openedPresentID));
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
            playerMenuUtility.setData("clicksRemaining", clicks);
        }
        if ((int) (playerMenuUtility.getData("clicksRemaining")) == 0) {
            player.sendMessage(ChatColor.GOLD + "Out of Clicks, see you soon!");
            player.closeInventory();
        }
    }

    @Override
    public void setMenuItems() {
        String closedPresentID = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGM5ZmQyMjRlZTJkYzdjODQ1NTZjOWMyZTgyYTJkOTVkMWFlMzU1Yzc5YWY3YjU4NTliOTUxMzhjMjVjZjkxOCJ9fX0=";
        ItemStack mysteryBox = SkullCreator.itemFromBase64(closedPresentID);
        ItemMeta mysteryMeta = mysteryBox.getItemMeta();

        assert mysteryMeta != null;
        mysteryMeta.setDisplayName(ColorTranslator.translateColorCodes("&6Mystery Box"));

        for (int i = 0; i < 44; i++) {
            mysteryMeta.setCustomModelData(i);
            mysteryBox.setItemMeta(mysteryMeta);
            inventory.addItem(mysteryBox);
        }

        int clicksRemain = (int) playerMenuUtility.getData("clicksRemaining");
        List<String> lore = new ArrayList<>();
        lore.add(ColorTranslator.translateColorCodes("&6Clicks Remaining: " + clicksRemain));

        inventory.setItem(48, makeItem(Material.TOTEM_OF_UNDYING, ChatColor.YELLOW + "INFO", String.valueOf(lore)));
    }
}
