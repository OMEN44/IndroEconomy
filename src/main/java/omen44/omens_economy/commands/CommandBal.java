package omen44.omens_economy.commands;

import omen44.omens_economy.Main;
import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.utils.ShortcutsUtils;
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

        String target = player.getDisplayName();
        int amount = 0;
        String type = "bank";
        String moneyStorage =  "wallet";

        if (args[1] != null) {target = args[1];} //who is the player targeting
        if (args[2] != null) {
            amount = Integer.parseInt(args[2]); //handles set and send commands
            type = args[2]; //handles reset command
        }
        if (args[3] != null) {moneyStorage = args[3];} //what money type do you want to reset

        if (label.equalsIgnoreCase("balance") || label.equalsIgnoreCase("bal")) {
            switch (args[0].toLowerCase(Locale.ROOT)) {
                case "w", "wallet" -> player.sendMessage(s.prefix + "You have " + symbol + wallet + " in your wallet");
                case "bank", "b" -> player.sendMessage(s.prefix + "You have " + symbol + bank + " in the bank");
                case "send" -> {
                    String status = main.economyUtils.sendMoney(player, player.getServer().getPlayerExact(target), amount);
                    switch (status) {
                        case "Successful" -> player.sendMessage(s.prefix + "Sent " + amount + " to " + target + "\n You have " + symbol + wallet + "in your wallet remaining");
                        case "Unsuccessful" -> player.sendMessage(s.prefix + ChatColor.YELLOW + "You do not have enough money to send " + amount + " to " + target);
                        default -> player.sendMessage(s.prefix + ChatColor.RED + "Not a valid targeted player!");
                    }
                }

                //only OPed people can execute this
                case "set" -> {
                    //args handling: /bal set <target> <amount> <wallet/bank>
                    if (player.isOp()) {

                        //part that executes the actual adding command
                        if ("bank".equals(type)) {
                            main.economyUtils.setBank(player.getServer().getPlayerExact(target), amount);
                            player.sendMessage(s.prefix + "Set " + target + "'s " + type + " to " + symbol + amount);
                        } else {
                            main.economyUtils.setWallet(player.getServer().getPlayerExact(target), amount);
                            player.sendMessage(s.prefix + "Set " + target + "'s wallet to " + symbol + amount);
                        }
                    } else {
                        player.sendMessage(s.prefix + ChatColor.RED + "Only OP's can use this command!");
                    }
                }
                case "reset" -> {
                    if (player.isOp()) {
                        switch (moneyStorage) {
                            case "wallet" -> main.economyUtils.setWallet(player.getServer().getPlayerExact(target), 0);
                            case "bank" -> main.economyUtils.setBank(player.getServer().getPlayerExact(target), 0);
                        }
                    } else {
                        player.sendMessage(s.prefix + "Only OP's can use this command!");
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