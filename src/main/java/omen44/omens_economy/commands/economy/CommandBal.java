package omen44.omens_economy.commands.economy;

import omen44.omens_economy.Main;
import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.utils.ShortcutsUtils;
import org.bukkit.Bukkit;
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
    public CommandBal(Main main) {
        this.main = main;
    }
    ShortcutsUtils s = new ShortcutsUtils();
    FileConfiguration config = ConfigTools.getFileConfig("config.yml");
    String symbol = config.getString("money.moneySymbol");

    public CommandBal() {}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            int wallet = main.eco.getMoney(p, "wallet");
            int bank = main.eco.getMoney(p, "bank");

            if (label.equalsIgnoreCase("bal")) {
                String type;

                if (args.length == 1) type = args[0];
                else if (args.length == 0) type = "";
                else type = "error";

                switch (type) {
                    case "wallet" -> p.sendMessage(s.prefix + ChatColor.YELLOW + "You have " + symbol + wallet + " in your wallet");
                    case "bank" -> p.sendMessage(s.prefix + ChatColor.YELLOW + "You have " + symbol + bank + " in your bank");
                    case "" -> {
                        p.sendMessage(s.prefix + ChatColor.YELLOW + "You have " + symbol + bank + " in your bank");
                        p.sendMessage(s.prefix + ChatColor.YELLOW + "You have " + symbol + wallet + " in your wallet");
                    }
                    default -> {
                        p.sendMessage(s.prefix + ChatColor.RED + "Error: Invalid Syntax");
                        return false;
                    }
                }
                return true;
            } else {
                p.sendMessage(s.prefix + ChatColor.RED + "Error: Invalid Syntax");
                return false;
            }
        } else {
            System.out.println("Error: Must be executed by a player!");
            return true;
        }
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