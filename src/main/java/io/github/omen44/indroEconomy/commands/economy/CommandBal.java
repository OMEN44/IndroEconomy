package io.github.omen44.indroEconomy.commands.economy;

import io.github.omen44.indroEconomy.utils.EconomyUtils;
import io.github.omen44.indroEconomy.utils.Lang;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(Lang.TITLE.toString() + Lang.PLAYER_ONLY);
        } else {
            EconomyUtils eco = new EconomyUtils();

            int bank;
            int wallet;

            try {
                wallet = eco.getWallet(player);
                bank = eco.getBank(player);
            } catch (NullPointerException e) {
                player.sendMessage("Your account doesn't exist, contact an admin to get it fixed");
                return;
            }

            if (args.length == 2 && args[1].equalsIgnoreCase("bank")) {
                player.sendMessage(Lang.TITLE + "Bank Balance: " + eco.format(bank));
            } else {
                player.sendMessage(Lang.TITLE + "Wallet Balance: " + eco.format(wallet));
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length == 2) {
            arguments.add("bank");
            return arguments;
        }
        return null;
    }
}