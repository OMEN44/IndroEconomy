package omen44.omens_economy.commands.economy;

import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.utils.EconomyUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static omen44.omens_economy.utils.ShortcutsUtils.*;

/*
    This class handles:
        - /transfer <wallet/bank/target> <amount>
 */

public class CommandTransfer implements TabExecutor {
    EconomyUtils eco = new EconomyUtils();
    ConfigTools configTools = new ConfigTools();
    FileConfiguration config = configTools.getConfig("config.yml");
    String symbol = config.getString("money.moneySymbol");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            int amount;

            if (label.equalsIgnoreCase("transfer") && args.length == 2) {
                try {
                    amount = Integer.parseInt(args[1]);
                    if (amount <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    p.sendMessage(mPrefix + mError + "Error: Must be a number above 0");
                    return true;
                }
                switch (args[0]) {
                    case "deposit" -> {
                        boolean result = eco.depositPlayer(p, amount);
                        if (result) {
                            p.sendMessage(mPrefix + mNormal + "Deposited " + ChatColor.YELLOW + symbol + amount +
                                    mNormal + "to the bank");
                        } else {
                            p.sendMessage(mPrefix + mWarning + "Unable to transfer due to insufficient funds.");
                        }
                    }
                    case "bank" -> {
                        boolean result = eco.withdrawPlayer(p, amount);
                        if (result) {
                            p.sendMessage(mPrefix + mNormal + "Withdrew " + ChatColor.YELLOW + symbol + amount +
                                    mNormal + "from the bank");
                        } else {
                            p.sendMessage(mPrefix + mWarning + "Unable to transfer due to insufficient funds.");
                        }
                    }
                    default -> {
                        return false;
                    }
                }
                return true;
            }
        } else {
            Bukkit.getLogger().warning("Warning: Only player executable");
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
        if (args.length == 2) {
            List<String> args2 = new ArrayList<>();
            args2.add("<amount>");
            return args2;
        }
        return null;
    }
}
