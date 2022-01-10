package omen44.omens_economy.commands.shops;

import omen44.omens_economy.Main;
import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.utils.EconomyUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static omen44.omens_economy.utils.ShortcutsUtils.*;

public class CommandCreateShop implements CommandExecutor {
    ConfigTools configTools = new ConfigTools();
    FileConfiguration config = configTools.getConfig("config.yml");
    String symbol = config.getString("money.moneySymbol");

    // command: /createshop <shopName> <shopPrice>
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            EconomyUtils eco = new EconomyUtils();

            if (label.equalsIgnoreCase("createShop") && args.length == 2) {
                int playerWallet = eco.getWallet(p);
                final int shopPrice = config.getInt("shop.shopPrice");

                if (playerWallet < shopPrice) {
                    p.sendMessage(mPrefix + mImportant + "You have insufficient funds to buy a shop!\n" +
                            mPrefix + mImportant + "You have " + symbol + playerWallet + "but you need " +
                            symbol + shopPrice + "!");
                    return true;
                }
                int playerShopPrice;
                try {
                    playerShopPrice = Integer.parseInt(args[1]);
                    if (playerShopPrice <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    p.sendMessage(mPrefix + mWarning + "<shopPrice> must be a positive number above 0.");
                    return true;
                }


                // creating the item that the player will place
                ItemStack chestShop = new ItemStack(Material.CHEST, 1);
                if (chestShop.getItemMeta() == null) {
                    p.sendMessage(mWarning + "There is a problem with the plugin, ping an admin about this.");
                    return true;
                }

                ItemMeta chestMeta = chestShop.getItemMeta();
                chestMeta.setDisplayName(p.getName() + "'s shop, priced at " + ChatColor.YELLOW + symbol + playerShopPrice);
                PersistentDataContainer chestContainer = chestMeta.getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "chestPrice");
                chestContainer.set(key, PersistentDataType.INTEGER, playerShopPrice);
                chestShop.setItemMeta(chestMeta);

                p.getInventory().addItem(chestShop);
                p.sendMessage(mPrefix + mNormal + "ChestShop given, place down to create a chest shop.");
            }
            return true;
        } else {
            Bukkit.getLogger().warning("This is a player command only.");
            return true;
        }
    }
}
