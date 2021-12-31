package omen44.omens_economy.commands.economy;

import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.utils.EconomyUtils;
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
        Player p = (Player) sender;

        int wallet = eco.getMoney(p, "wallet");
        int bank = eco.getMoney(p, "bank");
        int amount;

        if (label.equalsIgnoreCase("transfer") && args.length == 2) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException ex){
                p.sendMessage(mPrefix + mError + "Error: Invalid Number");
                return false;
            }
            switch (args[0]) {
                case "wallet" -> {
                    boolean result = eco.transferMoney(p, "wallet", amount);
                    if (result) {
                        p.sendMessage(mPrefix + mNormal + "Transfer successful.");
                        p.sendMessage(mPrefix + mNormal + "Bank Balance: " + symbol + bank);
                        p.sendMessage(mPrefix + mNormal + "Wallet Balance: " + symbol + wallet);
                    } else {
                        p.sendMessage(mPrefix + mWarning + "Unable to transfer due to insufficient funds.");
                    }
                }
                case "bank" -> {
                    boolean result = eco.transferMoney(p, "bank", amount);
                    if (result) {
                        p.sendMessage(mPrefix + mNormal + "Transfer successful.");
                        p.sendMessage(mPrefix + mNormal + "Bank Balance: " + symbol + bank);
                        p.sendMessage(mPrefix + mNormal + "Wallet Balance: " + symbol + wallet);
                    } else {
                        p.sendMessage(mPrefix + mWarning + "Unable to transfer due to insufficient funds.");
                    }
                }

                default -> {
                    p.sendMessage(mPrefix + mError + "Error: Invalid Syntax");
                    return false;
                }
            }
            return true;

        } else {
            p.sendMessage(mPrefix + mError + "Error: Invalid Syntax");
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> playerNames = new ArrayList<>();
            playerNames.add("bank");
            playerNames.add("wallet");
            return playerNames;
        }
        if (args.length == 2) {
            List<String> args2 = new ArrayList<>();
            args2.add("<amount>");
            return args2;
        }
        return null;
    }
}
