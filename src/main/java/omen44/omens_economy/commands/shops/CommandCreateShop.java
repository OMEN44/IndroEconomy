package omen44.omens_economy.commands.shops;

import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.utils.EconomyUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static omen44.omens_economy.utils.ShortcutsUtils.mImportant;
import static omen44.omens_economy.utils.ShortcutsUtils.mPrefix;

public class CommandCreateShop implements CommandExecutor {
    ConfigTools configTools = new ConfigTools();
    FileConfiguration config = configTools.getConfig("config.yml");
    String symbol = config.getString("money.moneySymbol");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            EconomyUtils eco = new EconomyUtils();

            if (label.equalsIgnoreCase("createShop") && args.length == 1) {
                int playerWallet = eco.getMoney(p, "wallet");
                final int shopPrice = config.getInt("shop.shopPrice");

                if (playerWallet < shopPrice) {
                    p.sendMessage(mPrefix + mImportant + "You have insufficient funds to buy a shop!\n" +
                            mPrefix + mImportant + "You have " + symbol + playerWallet + "but you need " +
                            symbol + shopPrice + "!");
                    return true;
                }

                ItemStack chestShop = new ItemStack(Material.CHEST);
                ItemMeta chestMeta = chestShop.getItemMeta();
                chestMeta.setDisplayName("ChestShop");

            }
        }
        return false;
    }
}
