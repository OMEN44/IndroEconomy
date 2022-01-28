package omen44.omens_economy.commands.shops;

import omen44.omens_economy.Main;
import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.utils.SQLeconomy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

import static omen44.omens_economy.utils.ShortcutsUtils.*;

public class CommandCreateShop implements TabExecutor {
    ConfigTools configTools = new ConfigTools();
    FileConfiguration config = configTools.getConfig("config.yml");
    String symbol = config.getString("money.moneySymbol");

    // command: /createshop <shopPrice>
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            SQLeconomy eco = new SQLeconomy();

            if (label.equalsIgnoreCase("createShop") && args.length == 1) {
                int playerWallet = eco.getWallet(p);
                final int shopPrice = config.getInt("shop.shopPrice");

                // error parsing
                if (playerWallet < shopPrice) {
                    p.sendMessage(mPrefix + mImportant + "You have insufficient funds to buy a shop!\n" +
                            mPrefix + mImportant + "You have " + symbol + playerWallet + " but you need " +
                            symbol + shopPrice + "!");
                    return true;
                }
                int playerShopPrice;
                try {
                    playerShopPrice = Integer.parseInt(args[0]);
                    if (playerShopPrice <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    p.sendMessage(mPrefix + mWarning + "<shopPrice> must be a positive number above 0.");
                    return true;
                }

                // generating a shop id for the item.

                // creating the item that the player will place
                ItemStack chestShop = new ItemStack(Material.CHEST, 1);
                if (chestShop.getItemMeta() == null) {
                    p.sendMessage(mWarning + "There is a problem with the plugin, ping an admin about this.");
                    return true;
                }

                NamespacedKey nsk = new NamespacedKey(Main.getPlugin(Main.class), "chestprice");
                ItemMeta chestMeta = chestShop.getItemMeta();
                chestMeta.setDisplayName(p.getName() + "'s shop, priced at " + ChatColor.YELLOW + symbol + playerShopPrice);
                PersistentDataContainer container = chestMeta.getPersistentDataContainer();
                container.set(nsk, PersistentDataType.INTEGER, playerShopPrice);
                container.set(nsk, PersistentDataType.STRING, p.getUniqueId().toString());
                chestShop.setItemMeta(chestMeta);

                p.getInventory().addItem(chestShop);
                p.sendMessage(mPrefix + mNormal + "ChestShop given, place down to create a chest shop at the placed location.");
            }
        } else {
            Bukkit.getLogger().warning("This is a player command only.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> args2 = new ArrayList<>();
            args2.add("<amount>");
            return args2;
        }
        return null;
    }
}
