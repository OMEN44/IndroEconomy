package omen44.omens_economy.commands.economy;

import omen44.omens_economy.utils.EconomyUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static omen44.omens_economy.utils.ShortcutsUtils.mNormal;
import static omen44.omens_economy.utils.ShortcutsUtils.mWarning;

public class CommandPay implements TabExecutor {
    EconomyUtils eco = new EconomyUtils();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // format: /pay <player> <amount>
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (label.equalsIgnoreCase("pay") && args.length == 2) {
                Player target = Bukkit.getServer().getPlayer(args[0]);
                if (target == null) {
                    p.sendMessage(mWarning + "Target must be active to pay");
                    return true;
                }

                int amount;
                try {
                    amount = Integer.parseInt(args[1]);
                    if (amount <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    p.sendMessage(mWarning + "<amount> must be a positive number.");
                    return true;
                }
                boolean result = eco.sendMoney(p, target, amount);
                if (result) {
                    p.sendMessage(mNormal + "Payment Successful");
                } else {
                    p.sendMessage(mWarning + "Payment could not be made");
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
