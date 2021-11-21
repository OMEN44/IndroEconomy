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

/*
    This class implements:
        - /setmoney <bank/wallet> <target> <amount>
*/

public class CommandSetMoney implements TabExecutor {
    public Main main;
    ConfigTools configTools = new ConfigTools();
    ShortcutsUtils s = new ShortcutsUtils();
    FileConfiguration config = configTools.getFileConfig("config.yml");
    String symbol = config.getString("money.moneySymbol");

    public CommandSetMoney(Main main) {
        this.main = main;
    }

    public CommandSetMoney() {}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;

        int wallet;
        int bank;
        Player target = Bukkit.getPlayer(args[1]);
        int amount;

        if (label.equalsIgnoreCase("setmoney") && args.length == 3){
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException ex){
                p.sendMessage(s.prefix + ChatColor.RED + "Error: Invalid Number");
                return false;
            }

            switch (args[0]) {
                case "wallet" -> {
                    main.economyUtils.setWallet(target, amount);
                    wallet = main.economyUtils.getMoney(p, "wallet");
                    p.sendMessage(s.prefix + ChatColor.YELLOW + "Set " + args[1] + "'s wallet to " + symbol + wallet);
                }
                case "bank" -> {
                    main.economyUtils.setBank(target, amount);
                    bank = main.economyUtils.getMoney(p, "bank");
                    p.sendMessage(s.prefix + ChatColor.YELLOW + "Set " + args[1] + "'s bank to " + symbol + bank);
                }
                default -> {
                    p.sendMessage(s.prefix + ChatColor.RED + "Error: Invalid Syntax");
                    return false;
                }
            }
            return true;
        } else {
            p.sendMessage(s.prefix + ChatColor.RED + "Error: Invalid Syntax");
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        Player player = (Player) sender;
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
