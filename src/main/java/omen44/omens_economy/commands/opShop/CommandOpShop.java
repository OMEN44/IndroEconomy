package omen44.omens_economy.commands.opShop;

import omen44.omens_economy.utils.SQLeconomy;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

import static omen44.omens_economy.utils.ShortcutsUtils.*;

public class CommandOpShop implements CommandExecutor, Listener {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (label.equalsIgnoreCase("opshop")) {
                OPShopInv screen = new OPShopInv();
                p.openInventory(screen.getInventory());
                p.sendMessage(mNormal + "Welcome to the OP Shop!");
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void clickEvent(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();
        SQLeconomy eco = new SQLeconomy();
        if (e.getClickedInventory() == null) return;

        if (e.getClickedInventory().getHolder() instanceof OPShopInv) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null) {
                String itemName = e.getCurrentItem().getItemMeta().getDisplayName();
                Random random = new Random();

                int percentRoll;
                int extraEnchChance;
                int overlevelChance;
                int enchCount = 0;

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

                            ItemStack enchBook = new ItemStack(Material.ENCHANTED_BOOK, 1);

                            //creating the enchanted book
                            ItemMeta itemMeta = enchBook.getItemMeta();
                            if (itemMeta == null) {
                                player.sendMessage(mError + "Null Error occurred, contact an admin for a refund.");
                                return;
                            }
                            do {
                                enchCount++;
                                player.sendMessage("Rolling for Enchantment " + enchCount + "...");
                                Enchantment enchantment = getEnchantment();
                                int maxEnchLvl;
                                if (isEnchOverleveled(overlevelChance)) {
                                    maxEnchLvl = 5;
                                } else {
                                    maxEnchLvl = enchantment.getMaxLevel();
                                }
                                itemMeta.addEnchant(enchantment, maxEnchLvl, true);
                                percentRoll = random.nextInt(100);
                            } while (percentRoll <= extraEnchChance && enchCount != 3);

                            //giving the player the book
                            enchBook.setItemMeta(itemMeta);
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

                            ItemStack enchBook = new ItemStack(Material.ENCHANTED_BOOK, 1);

                            //creating the enchanted book
                            ItemMeta itemMeta = enchBook.getItemMeta();
                            if (itemMeta == null) {
                                player.sendMessage(mError + "Null Error occurred, contact an admin for a refund.");
                                return;
                            }

                            do {
                                enchCount++;
                                player.sendMessage("Rolling for Enchantment " + enchCount + "...");
                                Enchantment enchantment = getEnchantment();
                                int maxEnchLvl;
                                if (isEnchOverleveled(overlevelChance)) {
                                    maxEnchLvl = 5;
                                } else {
                                    maxEnchLvl = enchantment.getMaxLevel();
                                }
                                itemMeta.addEnchant(enchantment, maxEnchLvl, true);
                                percentRoll = random.nextInt(100);
                            } while (percentRoll <= extraEnchChance && enchCount != 4);

                            //giving the player the book
                            enchBook.setItemMeta(itemMeta);
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

                            ItemStack enchBook = new ItemStack(Material.ENCHANTED_BOOK, 1);

                            //creating the enchanted book
                            ItemMeta itemMeta = enchBook.getItemMeta();
                            if (itemMeta == null) {
                                player.sendMessage(mError + "Null Error occurred, contact an admin for a refund.");
                                return;
                            }

                            do {
                                enchCount++;
                                player.sendMessage("Rolling for Enchantment " + enchCount + "...");
                                Enchantment enchantment = getEnchantment();
                                int maxEnchLvl;
                                if (isEnchOverleveled(overlevelChance)) {
                                    maxEnchLvl = 5;
                                } else {
                                    maxEnchLvl = enchantment.getMaxLevel();
                                }
                                itemMeta.addEnchant(enchantment, maxEnchLvl, true);
                                percentRoll = random.nextInt(100);
                            } while (percentRoll <= extraEnchChance && enchCount != 5);

                            //giving the player the book
                            enchBook.setItemMeta(itemMeta);
                            player.getInventory().addItem(enchBook);
                        } else {
                            player.sendMessage(mWarning + "Sorry, you don't have enough money to buy this");
                            return;
                        }
                    }
                }
                player.sendMessage(mNormal + "Recieved an enchanted book!");
            }
        }
    }

    public Enchantment getEnchantment() {
        return Enchantment.values()[(int) (Math.random()*Enchantment.values().length)];
    }

    public boolean isEnchOverleveled(int chance) {
        int roll = (int) (Math.random()*100);
        return roll <= chance;
    }
}
