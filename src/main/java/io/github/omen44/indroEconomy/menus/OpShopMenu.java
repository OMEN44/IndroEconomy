package io.github.omen44.indroEconomy.menus;

import io.github.omen44.indroEconomy.utils.EconomyUtils;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.ArrayList;
import java.util.Random;

import static io.github.omen44.indroEconomy.utils.ShortcutsUtils.mNormal;
import static io.github.omen44.indroEconomy.utils.ShortcutsUtils.mWarning;

public class OpShopMenu extends Menu {
    public OpShopMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "OP Shop";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) throws MenuManagerNotSetupException, MenuManagerException {
        ItemStack itemStack = inventoryClickEvent.getCurrentItem();
        if (itemStack == null) return;
        if (itemStack.getItemMeta() == null) return;
        String itemName = itemStack.getItemMeta().getDisplayName();
        Player player = (Player) inventoryClickEvent.getWhoClicked();

        EconomyUtils eco = new EconomyUtils();

        try {
            int wallet = eco.getWallet(player);
        } catch (NullPointerException e) {
            player.sendMessage("Your account doesn't exist, contact an admin to get it fixed");
            return;
        }

        int extraEnchChance;
        int overlevelChance;
        int maxEnchants;

        switch (itemName) {
            case "Tier 1" -> {
                if (eco.getWallet(player) >= 2500) {
                    eco.minusWallet(player, 2500);

                    //init chance gates
                    extraEnchChance = 25;
                    overlevelChance = 100;
                    maxEnchants = 3;

                    ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
                    ItemStack enchBook = generateEnchants(book, maxEnchants, overlevelChance, extraEnchChance);
                    player.getInventory().addItem(enchBook);
                } else {
                    player.sendMessage(mWarning + "Sorry, you don't have enough money to buy this");
                    return;
                }
            }

            case "Tier 2" -> {
                if (eco.getWallet(player) >= 16500) {
                    eco.minusWallet(player, 16500);

                    //init chance gates
                    extraEnchChance = 50;
                    overlevelChance = 100;
                    maxEnchants = 4;

                    ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
                    ItemStack enchBook = generateEnchants(book, maxEnchants, overlevelChance, extraEnchChance);
                    player.getInventory().addItem(enchBook);
                } else {
                    player.sendMessage(mWarning + "Sorry, you don't have enough money to buy this");
                    return;
                }
            }

            case "Tier 3" -> {
                if (eco.getWallet(player) >= 24550) {
                    eco.minusWallet(player, 24500);

                    //init chance gates
                    extraEnchChance = 75;
                    overlevelChance = 100;
                    maxEnchants = 5;

                    ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
                    ItemStack enchBook = generateEnchants(book, maxEnchants, overlevelChance, extraEnchChance);
                    player.getInventory().addItem(enchBook);
                } else {
                    player.sendMessage(mWarning + "Sorry, you don't have enough money to buy this");
                    return;
                }
            }
        }
        player.sendMessage(mNormal + "Recieved an enchanted book!");
    }

    @Override
    public void setMenuItems() {

        ItemStack tierOne = makeItem(Material.ENCHANTED_BOOK, "Tier 1",
                ChatColor.BOLD + "Costs $2,500",
                        "Guaranteed max level enchants.",
                        "25% chance for an extra enchant, for a max of 3");
        ItemStack tierTwo = makeItem(Material.ENCHANTED_BOOK, "Tier 2",
                ChatColor.BOLD + "Costs $16,500",
                        "Guaranteed max level enchants.",
                        "50% chance for an extra enchant, for a max of 4");
        ItemStack tierThree = makeItem(Material.ENCHANTED_BOOK, "Tier 3",
                ChatColor.BOLD + "Costs $24,550",
                        "Guaranteed max level enchants.",
                        "75% chance for an extra enchant, for a max of 5.");

        inventory.setItem(3, tierOne);
        inventory.setItem(4, tierTwo);
        inventory.setItem(5, tierThree);

        setFillerGlass();
    }


    private ItemStack generateEnchants(ItemStack enchantedItem, int maxEnch, int overLevelChance, int extraEnchantChance) {
        int enchCount = 0;
        int percentRoll;

        // enchants that break at lvl 5
        ArrayList<Enchantment> blackListedEnchants = new ArrayList<>();
        blackListedEnchants.add(Enchantment.CHANNELING);
        blackListedEnchants.add(Enchantment.ARROW_FIRE);
        blackListedEnchants.add(Enchantment.ARROW_INFINITE);
        blackListedEnchants.add(Enchantment.MULTISHOT);
        blackListedEnchants.add(Enchantment.SILK_TOUCH);
        blackListedEnchants.add(Enchantment.WATER_WORKER);
        blackListedEnchants.add(Enchantment.DEPTH_STRIDER);
        blackListedEnchants.add(Enchantment.MENDING);
        blackListedEnchants.add(Enchantment.BINDING_CURSE);
        blackListedEnchants.add(Enchantment.VANISHING_CURSE);

        EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) enchantedItem.getItemMeta();
        Random random = new Random();

        if (itemMeta == null) return null;

        do {
            enchCount++;
            Enchantment enchantment = Enchantment.values()[(int) (Math.random()*Enchantment.values().length)];
            int maxEnchLvl;
            if (isEnchOverleveled(overLevelChance) && !blackListedEnchants.contains(enchantment)) {
                maxEnchLvl = 5;
            } else {
                maxEnchLvl = enchantment.getMaxLevel();
            }
            itemMeta.addStoredEnchant(enchantment, maxEnchLvl, true);
            percentRoll = random.nextInt(100);
        } while (percentRoll <= extraEnchantChance && enchCount != maxEnch);

        // set the enchanted items to the item
        enchantedItem.setItemMeta(itemMeta);
        return enchantedItem;
    }


    private boolean isEnchOverleveled(int chance) {
        int roll = new Random().nextInt(100);
        return roll <= chance;
    }
}
