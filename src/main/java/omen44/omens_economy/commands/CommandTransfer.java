package omen44.omens_economy.commands;

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

/*
    This class handles:
        - /transfer <wallet/bank/target> <amount>
 */

public class CommandTransfer implements TabExecutor {
    public Main main;

    ShortcutsUtils s = new ShortcutsUtils();
    EconomyUtils eco = new EconomyUtils(null);
    FileConfiguration config = ConfigTools.getFileConfig("config.yml");
    String symbol = config.getString("money.moneySymbol");

    public CommandTransfer(Main main) {this.main = main;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        int wallet = eco.getMoney(player, "wallet");
        int bank = eco.getMoney(player, "bank");

        if (label.equalsIgnoreCase("transfer") && args.length == 2) { // todo - add a transfer() method in EconomyUtils
            switch (args[0]) {
                case "wallet" -> {
                    /* boilerplate code
                    if (transfer == true) {
                        player.sendMessage(s.prefix + ChatColor.GOLD + "Transfer was Successful! " + Symbol + amount + "was transferred to " + target);
                    } else {
                        player.sendMessage(s.prefix + ChatColor.RED + "Transfer was unsuccessful!");
                    }
                    */
                    player.sendMessage(s.prefix + ChatColor.YELLOW + "You have " + symbol + wallet + " left");
                }
                case "bank" -> {
                /* boilerplate code
                    if (transfer == true) {
                        player.sendMessage(s.prefix + ChatColor.GOLD + "Transfer was Successful! " + Symbol + amount + "was transferred to " + target);
                    } else {
                        player.sendMessage(s.prefix + ChatColor.RED + "Transfer was unsuccessful!");
                    }
                 */
                    player.sendMessage(s.prefix + ChatColor.YELLOW + "You have " + symbol + bank + " left");
                }

                default -> {
                    List<String> playerNames = new ArrayList<>();
                    Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                    Bukkit.getServer().getOnlinePlayers().toArray(players);
                    for (Player value : players) {
                        playerNames.add(value.getName());
                    }

                    if (playerNames.contains(args[1])) { //assumes that you have a whitelist
                        /* boilerplate code
                        if (transfer == true) {
                            player.sendMessage(s.prefix + ChatColor.GOLD + "Transfer was Successful! " + Symbol + amount + "was transferred to " + target);
                        } else {
                            player.sendMessage(s.prefix + ChatColor.RED + "Transfer was unsuccessful!");
                        }
                        */
                    } else {
                        player.sendMessage(s.prefix + ChatColor.RED + "Error: Invalid Syntax");
                    }
                }
            }
        } else {
            player.sendMessage(s.prefix + ChatColor.RED + "Error: Invalid Syntax");
        }
        return false;
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
