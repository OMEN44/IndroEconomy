package omen44.omens_economy.commands.economy;

import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.utils.EconomyUtils;
import org.bukkit.Bukkit;
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
    EconomyUtils eco = new EconomyUtils();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //Server s = (Server) sender
        Player p = (Player) sender;

        int wallet;
        int bank;
        int amount;
        Player target = Bukkit.getPlayer(args[1]);

        if (label.equalsIgnoreCase("setmoney") && args.length == 3){
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException ex){
                p.sendMessage(mPrefix + mError + "Error: Invalid Number");
                return false;
            }

            switch (args[0]) {
                case "wallet" -> {
                    eco.setWallet(target, amount);
                    wallet = eco.getMoney(p, "wallet");
                    p.sendMessage(mPrefix + mNormal + "Set " + args[1] + "'s wallet to " + symbol + wallet);
                }
                case "bank" -> {
                    eco.setBank(target, amount);
                    bank = eco.getMoney(p, "bank");
                    p.sendMessage(mPrefix + mNormal + "Set " + args[1] + "'s bank to " + symbol + bank);
                }
                default -> {
                    p.sendMessage(mPrefix + mError + "Error: Invalid Syntax");
                    return false;
                }
            }
            return true;
        } else {
            p.sendMessage(mPrefix + mError + "Error: Invalid Syntax");
            return false;
        }
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
