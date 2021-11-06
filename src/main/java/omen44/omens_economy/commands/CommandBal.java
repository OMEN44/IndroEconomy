package omen44.omens_economy.commands;

import omen44.omens_economy.Main;
import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.utils.ShortcutsUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/*this class handles
- /bal (wallet/bank)
 */

public class CommandBal implements TabExecutor {
    public Main main;
    public CommandBal(Main main) {this.main = main;}

    ShortcutsUtils s = new ShortcutsUtils();
    FileConfiguration config = ConfigTools.getFileConfig("config.yml");
    String symbol = config.getString("money.moneySymbol");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        int wallet = main.economyUtils.getMoney(player, "wallet");
        int bank = main.economyUtils.getMoney(player, "bank");
        if (label.equalsIgnoreCase("bal") && args.length <= 1) {
            if (args[0].equalsIgnoreCase("wallet")) {
                player.sendMessage(s.prefix + ChatColor.YELLOW + "You have " + symbol + wallet + " in your wallet");
            } else if (args[0].equalsIgnoreCase("bank")) {
                player.sendMessage(s.prefix + ChatColor.YELLOW + "You have " + symbol + bank + " in your bank");
            } else if (args[0].equals("")) {
                player.sendMessage(s.prefix + ChatColor.YELLOW + "You have " + symbol + bank + " in your bank");
                player.sendMessage(s.prefix + ChatColor.YELLOW + "You have " + symbol + wallet + " in your wallet");
            } else {
                player.sendMessage(s.prefix + ChatColor.RED + "Error: Invalid Syntax");
            }
        } else {
            player.sendMessage(s.prefix + ChatColor.RED + "Error: Invalid Syntax");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> args1 = new ArrayList<>();
            args1.add("bank");
            args1.add("wallet");
            return args1;
        }
        return null;

    }
}