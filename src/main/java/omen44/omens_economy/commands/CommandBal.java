package omen44.omens_economy.commands;

import omen44.omens_economy.Main;
import omen44.omens_economy.datamanager.ConfigTools;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*this command handles
 - /bal <wallet/bank>
 - /bal reset <player>
 - /bal set <player> <value>
 - /bal send <targetPlayer> <value>
 - /bal reset <targetPLayer>
*/

public class CommandBal implements TabExecutor {

    public Main main;
    public CommandBal(Main main) {this.main = main;}
    public CommandBal() {}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        FileConfiguration config = ConfigTools.getFileConfig("config.yml");
        Player player = (Player) sender;
        String symbol = config.getString("moneySymbol");

        if (label.equalsIgnoreCase("balance") || label.equalsIgnoreCase("bal")) {
            int wallet = main.economyUtils.getMoney(player, "wallet");
            int bank = main.economyUtils.getMoney(player, "bank");

            String target = args[0];
            int amount = (Integer.parseInt(args[1]));

            switch (args[0].toLowerCase(Locale.ROOT)) {
                case "w", "wallet": player.sendMessage("You have " + symbol + wallet + "in your wallet");
                case "bank", "b": player.sendMessage("You have " + symbol + bank + "in the bank");
                case "send":
                    String status = main.economyUtils.sendMoney(player, player.getServer().getPlayerExact(target), amount);
                    switch (status) {
                        case "Successful" -> player.sendMessage("Sent " + amount + " to " + target + "\n You have " + symbol + wallet + "in your wallet remaining");
                        case "Unsuccessful" -> player.sendMessage(ChatColor.YELLOW + "You do not have enough money to send " + amount + " to " + target);
                        default -> player.sendMessage(ChatColor.RED +"Not a valid targeted player!");
                    }

                //only OPed people can execute this
                case "set":
                    //args handling: /bal set <target> <amount> <wallet/bank>
                    if (player.isOp()) {
                        //error checker
                        boolean error = false;

                        String moneyStorage = args[2];

                        //determine if the player targeted is set
                        if (target == null){
                            player.sendMessage("Must target a player!");
                            error = true;
                        }

                        //determines if the player has declared a storage to change
                        if (moneyStorage == null){
                            player.sendMessage("Must target a storage to change!");
                            error = true;
                        }

                        //determines if the player has declared an amount
                        if (amount < 0) {
                            player.sendMessage("Must be a number bigger than 0!");
                            error = true;
                        }

                        //part that executes the actual adding command
                        if (!error){
                            switch (moneyStorage) {
                                case "wallet" -> main.economyUtils.setWallet(player.getServer().getPlayerExact(target), amount);
                                case "bank" -> main.economyUtils.setBank(player.getServer().getPlayerExact(target), amount);
                            }
                        }
                    } else {player.sendMessage("Only OP's can use this command!");}
                case "reset":
                    if (player.isOp()) {
                        //unfinished - unable to access the mySQL for testing
                        player.sendMessage(target + "'s money has been reset!");
                    } else {player.sendMessage("Only OP's can use this command!");}
                default: player.sendMessage("Use /bal <bank/wallet>");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        Player player = (Player) sender;
        if (args.length == 1) {
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
            args3.add("value>");
        } else {
            return null;
        }
    }
}