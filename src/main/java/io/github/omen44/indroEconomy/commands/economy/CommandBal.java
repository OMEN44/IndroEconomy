package io.github.omen44.indroEconomy.commands.economy;

import io.github.omen44.indroEconomy.datamanager.ConfigTools;
import io.github.omen44.indroEconomy.utils.EconomyUtils;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static io.github.omen44.indroEconomy.utils.ShortcutsUtils.mNormal;

public class CommandBal extends SubCommand {
    @Override
    public String getName() {
        return "bal";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Get your balance on the server ";
    }

    @Override
    public String getSyntax() {
        return "/eco bal (wallet/bank)";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        // checks if the sender is a player
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This is a player only command!");
        } else {
            Player player = (Player) commandSender;
            EconomyUtils eco = new EconomyUtils();
            ConfigTools configTools = new ConfigTools();
            FileConfiguration config = configTools.getConfig("config.yml");
            String symbol = config.getString("money.moneySymbol");

            final int wallet = eco.getWallet(player);
            final int bank = eco.getBank(player);
            final int totalBalance = wallet + bank;
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("wallet")) {
                    player.sendMessage(mNormal + "Wallet Balance: " + symbol + wallet);
                } else if (args[1].equalsIgnoreCase("bank")) {
                    player.sendMessage(mNormal + "Bank Balance: " + symbol +  bank);
                }
            } else {
                player.sendMessage(mNormal + "Total Amount: " + symbol + totalBalance);
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length == 2) {
            arguments.add("bank");
            arguments.add("wallet");
            return arguments;
        }
        return null;
    }
}