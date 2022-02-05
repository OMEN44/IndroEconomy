package io.github.omen44.indroEconomy.menus;

import io.github.omen44.indroEconomy.IndroEconomy;
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
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.bukkit.Material.RED_STAINED_GLASS_PANE;

public class MysteryMenu extends Menu {
    private int clicks = 1;
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
        Player player = playerMenuUtility.getOwner();
        if (player.hasMetadata("clicksRemaining")) {
            List<MetadataValue> keys = player.getMetadata("clicksRemaining");
            for (MetadataValue key : keys) {
                if (key.getOwningPlugin() == IndroEconomy.getInstance()) {
                    this.clicks = key.asInt();
                    break;
                }
            }
        }
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType().equals(RED_STAINED_GLASS_PANE)) {
            player.closeInventory();
        } else {
            int currentItemIndex = e.getCurrentItem().getItemMeta().getCustomModelData();
            String openedPresentID = "http://textures.minecraft.net/texture/bfe732b3ecb2fabc038fb06db8c53a7ffb030db92544e1b2256f01cb2eb822b7";
            inventory.setItem(currentItemIndex, SkullCreator.itemFromUrl(openedPresentID));
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
            this.clicks--;
            player.setMetadata("clicksRemaining", new FixedMetadataValue(IndroEconomy.getInstance(), clicks));

            List<String> lore = new ArrayList<>();
            lore.add(ColorTranslator.translateColorCodes("&6Clicks Remaining: 3"));

            inventory.setItem(49, makeItem(Material.TOTEM_OF_UNDYING, ChatColor.YELLOW + "INFO", String.valueOf(lore)));
        }
        if (this.clicks <= 0) {
            player.sendMessage(ChatColor.GOLD + "Out of Clicks, see you soon!");
            player.closeInventory();
        }
    }

    @Override
    public void setMenuItems() {
        String closedPresentID = "http://textures.minecraft.net/texture/8c9fd224ee2dc7c84556c9c2e82a2d95d1ae355c79af7b5859b95138c25cf918";
        ItemStack mysteryBox = SkullCreator.itemFromUrl(closedPresentID);
        ItemMeta mysteryMeta = mysteryBox.getItemMeta();

        assert mysteryMeta != null;
        mysteryMeta.setDisplayName(ColorTranslator.translateColorCodes("&6Mystery Box"));

        for (int i = 1; i < 46; i++) {
            mysteryMeta.setCustomModelData(i-1);
            mysteryBox.setItemMeta(mysteryMeta);
            inventory.addItem(mysteryBox);
        }

        List<String> lore = new ArrayList<>();
        lore.add(ColorTranslator.translateColorCodes("&6Clicks Remaining: 3"));

        inventory.setItem(49, makeItem(Material.TOTEM_OF_UNDYING, ChatColor.YELLOW + "INFO", String.valueOf(lore)));
    }
}
