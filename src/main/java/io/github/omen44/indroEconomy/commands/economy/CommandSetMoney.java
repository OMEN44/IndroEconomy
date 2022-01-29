package io.github.omen44.indroEconomy.commands.economy;

import io.github.omen44.indroEconomy.utils.SQLeconomy;
import io.github.omen44.indroEconomy.datamanager.ConfigTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static io.github.omen44.indroEconomy.utils.ShortcutsUtils.*;

/*
    This class implements:
        - /setmoney <bank/wallet> <target> <amount>

    TODO: add a way for the console to edit usernames
*/

public class CommandSetMoney implements TabExecutor {
    ConfigTools configTools = new ConfigTools();
    FileConfiguration config = configTools.getConfig("config.yml");
    String symbol = config.getString("money.moneySymbol");
    SQLeconomy eco = new SQLeconomy();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("setmoney") && args.length == 3) {
            String type = args[0];
            Player target = Bukkit.getServer().getPlayer(args[1]);

            int amount;
            try {
                amount = Integer.parseInt(args[2]);
                if (amount < 0 || amount >= 100000000) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(mWarning + "<amount> must be a positive number bigger than 0, " +
                        "and smaller than 10,000,000.");
                return true;
            }

            if (target == null) {
                sender.sendMessage(mWarning + "Target must Exist!");
                return true;
            }

            switch (type) {
                case "wallet" -> {
                    eco.setWallet(target, amount);
                    int wallet = eco.getWallet(target);
                    sender.sendMessage(mPrefix + mNormal + " Set " + ChatColor.YELLOW + target.getName() + mNormal + "'s wallet to " + symbol + wallet);
                    return true;
                }
                case "bank" -> {
                    eco.setBank(target, amount);
                    int bank = eco.getBank(target);
                    sender.sendMessage(mPrefix + mNormal + " Set " + ChatColor.YELLOW + target.getName() + mNormal + "'s bank to " + symbol + bank);
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> args1 = new ArrayList<>();
            args1.add("wallet");
            args1.add("bank");
            return args1;
        }
        if (args.length == 2) {
            return new ArrayList<>(eco.getAccountList("UUID"));
        }
        if (args.length == 3) {
            List<String> args3 = new ArrayList<>();
            args3.add("<amount>");
            return args3;
        }
        return null;
    }
}
