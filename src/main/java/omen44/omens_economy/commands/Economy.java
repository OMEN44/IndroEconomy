package omen44.omens_economy.commands;

import omen44.omens_economy.Main;
import omen44.omens_economy.datamanager.ConfigTools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class Economy implements TabExecutor {

    public Main main;
    public Economy(Main main) {this.main = main;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        FileConfiguration config = ConfigTools.getFileConfig("config.yml");
        Player player = (Player) sender;
        String symbol = config.getString("moneySymbol");

        if (label.equalsIgnoreCase("balance") || label.equalsIgnoreCase("bal")) {
            Integer wallet = main.economyUtils.getMoney(player, "wallet");
            Integer bank = main.economyUtils.getMoney(player, "bank");

            player.sendMessage("you have " + symbol + wallet + "in your wallet\nYou have " + symbol + bank + "in the bank");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
