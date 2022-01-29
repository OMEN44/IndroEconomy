package io.github.omen44.IndroEconomy.commands.economy;

import io.github.omen44.IndroEconomy.utils.SQLeconomy;
import io.github.omen44.IndroEconomy.utils.ShortcutsUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandPay implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // format: /pay <player> <amount>
        if (sender instanceof Player) {
            Player p = (Player) sender;
            SQLeconomy eco = new SQLeconomy();
            if (label.equalsIgnoreCase("pay") && args.length == 2) {
                Player target = Bukkit.getServer().getPlayer(args[0]);
                if (target == null) {
                    p.sendMessage(ShortcutsUtils.mWarning + "Target must be active to pay");
                    return true;
                }

                int amount;
                try {
                    amount = Integer.parseInt(args[1]);
                    if (amount <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    p.sendMessage(ShortcutsUtils.mWarning + "<amount> must be a positive number.");
                    return true;
                }

                boolean result = eco.sendMoney(p, target, amount);
                if (result) {
                    p.sendMessage(ShortcutsUtils.mNormal + "Payment Successful.");
                } else {
                    p.sendMessage(ShortcutsUtils.mWarning + "Payment could not be made.");
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete( CommandSender sender,  Command command,  String alias,  String[] args) {
        if (args.length == 1) {
            List<String> playerNames = new ArrayList<>();
            Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
            Bukkit.getServer().getOnlinePlayers().toArray(players);
            for (Player value : players) {
                playerNames.add(value.getName());
            }
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
