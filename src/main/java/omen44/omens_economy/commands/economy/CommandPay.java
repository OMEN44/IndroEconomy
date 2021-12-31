package omen44.omens_economy.commands.economy;

import omen44.omens_economy.utils.EconomyUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static omen44.omens_economy.utils.ShortcutsUtils.*;

public class CommandPay implements TabExecutor {
    EconomyUtils eco = new EconomyUtils();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // format: /pay <player> <amount>
        Player p = (Player) sender;
        if (label.equalsIgnoreCase("pay") && args.length == 2) {
            List<String> playerNames = new ArrayList<>();
            Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
            Bukkit.getServer().getOnlinePlayers().toArray(players);
            for (Player value : players) {
                playerNames.add(value.getName());
            }

            Player target = Bukkit.getServer().getPlayer(args[0]);
            if (target != null) {
                int amount;
                try {
                    amount = Integer.parseInt(args[1]);
                } catch (NumberFormatException exception) {
                    p.sendMessage(mPrefix + mError + "Error: invalid number format");
                    return false;
                }

                if (playerNames.contains(target.toString())) {
                    String result = eco.sendMoney(p, target, amount);
                    switch (result.toLowerCase(Locale.ROOT)) {
                        case "successful" -> {
                            p.sendMessage(mPrefix + mImportant + "Transfer Successful!");
                            return true;
                        }
                        case "unsuccessful" -> {
                            p.sendMessage(mPrefix + mImportant + "Transfer Unsuccessful, not enough Money!");
                            return true;
                        }
                        case "target not found" -> {
                            p.sendMessage(mPrefix + mWarning + "Warning: Player does not exist");
                            return true;
                        }
                    }
                }
            }
            p.sendMessage(mPrefix + mWarning + "Warning: Player not Found");
            return false;
        }
        p.sendMessage(mPrefix + mError + "Error: Invalid Syntax");
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
            List<String> args3 = new ArrayList<>();
            args3.add("<amount>");
            return args3;
        }
        return null;
    }
}
