package omen44.omens_economy.commands.economy;

import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.utils.EconomyUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static omen44.omens_economy.utils.ShortcutsUtils.*;

/*this class handles
- /bal (wallet/bank)
 */

public class CommandBal implements TabExecutor {
    ConfigTools configTools = new ConfigTools();
    FileConfiguration config = configTools.getConfig("config.yml");
    String symbol = config.getString("money.moneySymbol");
    EconomyUtils eco = new EconomyUtils();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            final int wallet = eco.getMoney(p, "wallet");
            final int bank = eco.getMoney(p, "bank");

            if (label.equalsIgnoreCase("bal")) {
                String type;

                if (args.length == 1) {
                    type = args[0];
                } else if (args.length == 0) {
                    type = "";
                } else {
                    p.sendMessage(mPrefix + mError + "Error: Invalid Syntax");
                    return false;
                }

                switch (type) {
                    case "wallet" -> p.sendMessage(mPrefix + mNormal + "You have " + symbol + wallet + " in your wallet");
                    case "bank" -> p.sendMessage(mPrefix + mNormal + "You have " + symbol + bank + " in your bank");
                    case "" -> {
                        p.sendMessage(mPrefix + mNormal + "You have " + symbol + bank + " in your bank");
                        p.sendMessage(mPrefix + mNormal + "You have " + symbol + wallet + " in your wallet");
                    }
                }
                return true;
            } else {
                p.sendMessage(mPrefix + ChatColor.RED + "Error: Invalid Syntax");
                return false;
            }
        } else {
            System.err.println("Error: Must be executed by a player!");
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