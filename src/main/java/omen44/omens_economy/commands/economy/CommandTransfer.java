package omen44.omens_economy.commands.economy;

import omen44.omens_economy.Main;
import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.utils.EconomyUtils;
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
import java.util.Locale;

/*
    This class handles:
        - /transfer <wallet/bank/target> <amount>
 */

public class CommandTransfer implements TabExecutor {
    public Main main;
    ShortcutsUtils s = new ShortcutsUtils();
    EconomyUtils eco = new EconomyUtils();

    FileConfiguration config = ConfigTools.getFileConfig("config.yml");
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
                p.sendMessage(s.prefix + s.error + "Error: Invalid Number");
                return false;
            }
            switch (args[0]) {
                case "wallet" -> {
                    eco.transferMoney(p, "wallet", amount);
                    p.sendMessage(s.prefix + s.nMessage + "You have " + symbol + wallet + " left");
                }
                case "bank" -> {
                    eco.transferMoney(p, "bank", amount);
                    p.sendMessage(s.prefix + ChatColor.YELLOW + "You have " + symbol + bank + " left");
                }

                default -> {
                    List<String> playerNames = new ArrayList<>();
                    Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                    Bukkit.getServer().getOnlinePlayers().toArray(players);
                    for (Player value : players) {
                        playerNames.add(value.getName());
                    }
                    Player target = Bukkit.getServer().getPlayer(args[1]);
                    if (playerNames.contains(args[1])) {
                        String result = eco.sendMoney(p, target, amount);
                        switch (result.toLowerCase(Locale.ROOT)) {
                            case "successful" -> {
                                p.sendMessage(s.prefix + s.iMessage + "Transfer Successful!");
                                return true;
                            }
                            case "unsuccessful" -> {
                                p.sendMessage(s.prefix + s.nMessage + "Transfer Unsuccessful, not enough Money!");
                                return true;
                            }
                            case "target not found" -> {
                                p.sendMessage(s.prefix + s.nMessage + "Warning: Player Not Found!");
                                return true;
                            }
                        }
                    } else {
                        p.sendMessage(s.prefix + s.error + "Error: Invalid Syntax");
                        return false;
                    }
                }
            }
            return true;
        } else {
            p.sendMessage(s.prefix + s.error + "Error: Invalid Syntax");
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> playerNames = new ArrayList<>();
            Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
            Bukkit.getServer().getOnlinePlayers().toArray(players);
            for (Player value : players) {
                playerNames.add(value.getName());
            }

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
