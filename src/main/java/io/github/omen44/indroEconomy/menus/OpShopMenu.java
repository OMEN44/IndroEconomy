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
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Random;

import static io.github.omen44.indroEconomy.utils.ShortcutsUtils.*;

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

        int extraEnchChance;
        int overlevelChance;
        int maxEnchants;

        switch (itemName) {
            /*
              <Tier 1>
              - Cost: $2,500
              - Extra Enchant Chance: 25%
              - Overlevel Chance: 25%
             */
            case "Tier 1" -> {
                if (eco.getWallet(player) >= 2500) {
                    eco.minusWallet(player, 2500);

                    //init chance gates
                    extraEnchChance = 25;
                    overlevelChance = 25;
                    maxEnchants = 3;

                    ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
                    ItemStack enchBook = generateEnchants(book, maxEnchants, overlevelChance, extraEnchChance);
                    player.getInventory().addItem(enchBook);
                } else {
                    player.sendMessage(mWarning + "Sorry, you don't have enough money to buy this");
                    return;
                }
            }


            /*
              <Tier 2>
              - Cost: $16,500
              - Extra Enchant Chance: 30%
              - Overlevel Chance: 50%
             */
            case "Tier 2" -> {
                if (eco.getWallet(player) >= 16500) {
                    eco.minusWallet(player, 16500);

                    //init chance gates
                    extraEnchChance = 30;
                    overlevelChance = 50;
                    maxEnchants = 4;

                    ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
                    ItemStack enchBook = generateEnchants(book, maxEnchants, overlevelChance, extraEnchChance);
                    player.getInventory().addItem(enchBook);
                } else {
                    player.sendMessage(mWarning + "Sorry, you don't have enough money to buy this");
                    return;
                }
            }


            /*
              <Tier 3>
              - Cost: $24,550
              - Extra Enchant Chance: 40%
              - Overlevel Chance: 75%
             */
            case "Tier 3" -> {
                if (eco.getWallet(player) >= 24550) {
                    eco.minusWallet(player, 24500);

                    //init chance gates
                    extraEnchChance = 40;
                    overlevelChance = 75;
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
                ChatColor.BOLD + "Costs $2,500\n" +
                        "25% chance of a over-leveled enchant.\n" +
                        "25% chance for an extra enchant, for a max of 3");
        ItemStack tierTwo = makeItem(Material.ENCHANTED_BOOK, "Tier 2",
                ChatColor.BOLD + "Costs $16,500\n" +
                        "50% chance of a over-leveled enchant.\n" +
                        "30% chance for an extra enchant, for a max of 4");
        ItemStack tierThree = makeItem(Material.ENCHANTED_BOOK, "Tier 3",
                ChatColor.BOLD + "Costs $24,550\n" +
                        "75% chance of a over-leveled enchant.\n" +
                        "40% chance for an extra enchant, for a max of 5.") ;

        inventory.setItem(3, tierOne);
        inventory.setItem(4, tierTwo);
        inventory.setItem(5, tierThree);

        setFillerGlass();
    }


    private ItemStack generateEnchants(ItemStack enchantedItem, int maxEnch, int overLevelChance, int extraEnchantChance) {
        int enchCount = 0;
        int percentRoll = 0;

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

        ItemMeta itemMeta = enchantedItem.getItemMeta();
        Random random = new Random(142141);

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
            itemMeta.addEnchant(enchantment, maxEnchLvl, true);
            percentRoll = random.nextInt(100);
        } while (percentRoll <= extraEnchantChance && enchCount != maxEnch);

        // set the enchanted items to the item
        enchantedItem.setItemMeta(itemMeta);
        return enchantedItem;
    }


    private boolean isEnchOverleveled(int chance) {
        int roll = (int) (Math.random()*100);
        return roll <= chance;
    }
}
