package io.github.omen44.indroEconomy.commands.economy;

import io.github.omen44.indroEconomy.utils.EconomyUtils;
import io.github.omen44.indroEconomy.utils.Lang;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static io.github.omen44.indroEconomy.utils.ShortcutsUtils.mNormal;
import static io.github.omen44.indroEconomy.utils.ShortcutsUtils.mWarning;

public class CommandTransfer extends SubCommand {
    @Override
    public String getName() {
        return "transfer";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Allows you to transfer money to and from your bank, keeping it safe";
    }

    @Override
    public String getSyntax() {
        return "/eco transfer <bank/wallet> <amount>";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(Lang.TITLE.toString() + Lang.PLAYER_ONLY);
        } else {
            if (args.length == 3) {
                EconomyUtils eco = new EconomyUtils();

                // initialise values
                String type = args[1];
                int amount;
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage(mNormal + "<amount> must be a positive, non-negative integer!");
                    return;
                }

                if (type == null) {
                    player.sendMessage(mWarning + "Syntax Error! \n" + mWarning + "Format: /eco transfer <deposit/withdraw> <amount>");
                    return;
                }

                boolean result = eco.transferMoney(player, type, amount);
                if (result) {
                    final int wallet = eco.getWallet(player);
                    final int bank = eco.getBank(player);
                    player.sendMessage(ColorTranslator.translateColorCodes("&aTransfer was successful!"));
                    player.sendMessage(Lang.TITLE + "Current Wallet Balance: " + eco.format(wallet));
                    player.sendMessage(Lang.TITLE + "Current Bank Balance: " + eco.format(bank));
                } else {
                    player.sendMessage(Lang.TITLE + "Payment could not be done, cancelling transaction!");
                }
            } else {
                player.sendMessage(mWarning + "Syntax Error! \n" + mWarning + "Format: /eco transfer <bank/wallet> <amount>");
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length == 2) {
            arguments.add("withdraw");
            arguments.add("deposit");
            return arguments;
        }
        if (args.length == 3) {
            arguments.add("<amount>");
            return arguments;
        }
        return null;
    }
}
