package io.github.omen44.indroEconomy.commands.economy;

import io.github.omen44.indroEconomy.utils.EconomyUtils;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static io.github.omen44.indroEconomy.utils.ShortcutsUtils.*;

/*
    This class implements:
        - /setmoney <bank/wallet> <target> <amount>

    TODO: add a way for the console to edit usernames
*/

public class CommandSetMoney extends SubCommand {
    @Override
    public String getName() {
        return "setmoney";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Allows admins to configure the amount of money a player has!";
    }

    @Override
    public String getSyntax() {
        return "/eco setmoney <bank/wallet> <player> <amount>";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        EconomyUtils eco = new EconomyUtils();
        if (commandSender.hasPermission("indroEconomy.admin.setMoney")) {
            if (args.length == 4) {
                // initialising values
                final String type = args[1];
                final Player target = Bukkit.getPlayer(args[2]);
                int amount;

                try {
                    amount = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(mNormal + "<amount> must be a positive, non-negative integer!");
                    return;
                }

                if (target == null || !eco.hasAccount(target)) {
                    commandSender.sendMessage(mWarning + "<target> must be a valid Minecraft Username, and have joined at least once!");
                    return;
                }

                if (type.equals("wallet")) {
                    eco.setWallet(target, amount);
                    String formatted = eco.format(eco.getWallet(target));
                    commandSender.sendMessage(ColorTranslator.translateColorCodes("&fSet &a" + target.getName() + "'s &fwallet to " + formatted));
                } else if (type.equals("bank")) {
                    eco.setBank(target, amount);
                    String formatted = eco.format(eco.getBank(target));
                    commandSender.sendMessage(ColorTranslator.translateColorCodes("&fSet &a" + target.getName() + "'s &fbank to " + formatted));
                }
            } else {
                commandSender.sendMessage(mWarning + "Syntax Error! \n" + mWarning + "Format: /eco setmoney <player> <amount>");
            }
        } else {
            commandSender.sendMessage(mError + "You do not have permission to do this!");
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
        if (args.length == 3) {
            for (Player player1: Bukkit.getOnlinePlayers()) {
                arguments.add(player1.getName());
            }
            return arguments;
        }
        if (args.length == 4) {
            arguments.add("<amount>");
            return arguments;
        }
        return null;
    }
}
