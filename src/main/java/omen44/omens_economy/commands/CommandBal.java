package omen44.omens_economy.commands;

import omen44.omens_economy.Main;
import omen44.omens_economy.datamanager.ConfigTools;
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

/*this command handles
 - /bal <wallet/bank>
 - /bal reset <targetPlayer>
 - /bal set <targetPlayer> <value> <wallet/bank>
 - /bal send <targetPlayer> <value>
 - /bal reset <targetPlayer> <wallet/bank>

    ToDo - initialise the arguments
         - handle all of the problems that come with that
 */

public class CommandBal implements TabExecutor {

    public Main main;

    public CommandBal(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ShortcutsUtils s = new ShortcutsUtils();

        FileConfiguration config = ConfigTools.getFileConfig("config.yml");
        Player player = (Player) sender;
        String symbol = config.getString("money.moneySymbol");

        int wallet = main.economyUtils.getMoney(player, "wallet");
        int bank = main.economyUtils.getMoney(player, "bank");

        String commandCalled = "";
        if (args.length >= 1) {
            commandCalled = args[0]; //args[0] handles this
        }
        Player target;
        int amount; //args[2] handles this
        String type; //and this if it is a string

        if (label.equalsIgnoreCase("balance") || label.equalsIgnoreCase("bal")) {
            switch (commandCalled) {
                case "w", "wallet" -> player.sendMessage(s.prefix + "You have " + symbol + wallet + " in your wallet");
                case "bank", "b" -> player.sendMessage(s.prefix + "You have " + symbol + bank + " in the bank");
                case "send" -> {
                    if (args.length == 3) {
                        target = Bukkit.getPlayer(args[1]);
                        amount = Integer.parseInt(args[2]);

                        String status = main.economyUtils.sendMoney(player, target, amount);
                        switch (status) {
                            case "Successful" -> player.sendMessage(s.prefix + "Sent " + amount + " to " + args[1] + "\n" + s.prefix + " You have " + symbol + wallet + "in your wallet remaining");
                            case "Unsuccessful" -> player.sendMessage(s.prefix + ChatColor.YELLOW + "You do not have enough money to send " + amount + " to " + target);
                            default -> player.sendMessage(s.prefix + ChatColor.RED + "Not a valid targeted player!");
                        }
                    } else {
                        player.sendMessage(s.prefix + ChatColor.YELLOW + "Error: Invalid Syntax");
                    }
                }
                //only OPed people can execute this
                case "set" -> {
                    if (args.length == 4) {
                        target = Bukkit.getPlayer(args[1]);
                        amount = Integer.parseInt(args[2]);
                        type = args[3];

                        //args handling: /bal set <target> <amount> <wallet/bank>
                        if (player.isOp() && target != null) {
                            //part that executes the actual adding command
                            if ("bank".equals(type)) {
                                main.economyUtils.setBank(target, amount);
                                player.sendMessage(s.prefix + "Set " + args[1] + "'s " + type + " to " + symbol + amount);
                            } else {
                                main.economyUtils.setWallet(target, amount);
                                player.sendMessage(s.prefix + "Set " + args[1] + "'s wallet to " + symbol + amount);
                            }
                        } else {
                            player.sendMessage(s.prefix + ChatColor.RED + "Only OP's can use this command!");
                        }
                    } else {
                        player.sendMessage(s.prefix + ChatColor.YELLOW + "Error: Invalid Syntax");
                    }
                }
                case "reset" -> {
                    if (args.length == 3) {
                        target = Bukkit.getPlayer(args[1]);
                        type = args[2];

                        if (player.isOp() && target != null) {
                            switch (type) {
                                case "wallet" -> main.economyUtils.setWallet(target, 0);
                                case "bank" -> main.economyUtils.setBank(target, 0);
                            }
                            player.sendMessage(s.prefix + "Reset " + args[1] + "'s " + type + " to " + symbol + "0");
                        } else {
                            player.sendMessage(s.prefix + "Only OP's can use this command!");
                        }
                    } else {
                        player.sendMessage(s.prefix + ChatColor.YELLOW + "Error: Invalid Syntax");
                    }
                }
                default -> player.sendMessage(s.prefix + "Use /bal <bank/wallet>");
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        Player player = (Player) sender;
        if (args.length == 1) { //this is the first argument after bal e.g. /bal reset
            List<String> args1 = new ArrayList<>();
            args1.add("bank");
            args1.add("wallet");
            args1.add("send");
            if (player.isOp()) {
                args1.add("reset");
                args1.add("set");
            }
            return args1;
        } else if (args.length == 3) {
            List<String> args3 = new ArrayList<>();
            if (args[0].equals("reset")) {
                args3.add("wallet");
                args3.add("bank");
            } else {
                args3.add("<value>");
            }
            return args3;
        } else {
            return null;
        }
    }
}