package omen44.omens_economy.commands.economy;

import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.utils.SQLeconomy;
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
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (label.equalsIgnoreCase("setmoney") && args.length == 3) {
                String type = args[0];
                Player target = Bukkit.getServer().getPlayer(args[1]);

                int amount;
                try {
                    amount = Integer.parseInt(args[2]);
                    if (amount < 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    p.sendMessage(mWarning + "<amount> must be a positive number bigger than 0.");
                    return true;
                }

                if (target == null) {
                    p.sendMessage(mWarning + "Target must be active");
                    return true;
                }

                switch (type) {
                    case "wallet" -> {
                        eco.setWallet(target, amount);
                        int wallet = eco.getWallet(p);
                        p.sendMessage(mPrefix + mNormal + "Set " + ChatColor.YELLOW + target.getName() + mNormal + "'s wallet to " + symbol + wallet);
                        return true;
                    }
                    case "bank" -> {
                        eco.setBank(target, amount);
                        int bank = eco.getBank(p);
                        p.sendMessage(mPrefix + mNormal + "Set " + ChatColor.YELLOW + target.getName() + mNormal + "'s bank to " + symbol + bank);
                        return true;
                    }
                }
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
            List<String> playerNames = new ArrayList<>();
            Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
            Bukkit.getServer().getOnlinePlayers().toArray(players);
            for (Player value : players) {
                playerNames.add(value.getName());
            }
            return playerNames;
        }
        if (args.length == 3) {
            List<String> args3 = new ArrayList<>();
            args3.add("<amount>");
            return args3;
        }
        return null;
    }
}
