package io.github.omen44.indroEconomy.commands.economy;

import io.github.omen44.indroEconomy.utils.EconomyUtils;
import io.github.omen44.indroEconomy.utils.Lang;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static io.github.omen44.indroEconomy.utils.ShortcutsUtils.mNormal;
import static io.github.omen44.indroEconomy.utils.ShortcutsUtils.mWarning;

public class CommandSend extends SubCommand {
    @Override
    public String getName() {
        return "send";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Allows sending money from your wallet to another active player!";
    }

    @Override
    public String getSyntax() {
        return "/eco send <player> <amount>";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(Lang.TITLE.toString() + Lang.PLAYER_ONLY);
        } else {
            EconomyUtils eco = new EconomyUtils();

            // /eco pay <player> <amount>
            if (args.length > 3) {
                Player target = Bukkit.getPlayer(args[1]);
                int amount;

                // error checkers
                if (target == null || !eco.hasAccount(target)) {
                    player.sendMessage(mWarning + "<target> must be a valid Minecraft Username, and have joined at least once!");
                    return;
                }
                if (!(args[2].equalsIgnoreCase("max"))) {
                    try {
                        amount = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(mNormal + "<amount> must be a positive, non-negative integer!");
                        return;
                    }
                } else {
                    amount = eco.getWallet(player);
                    if (args[3] == null) {
                        player.sendMessage(String.format("%s Are you sure you want to transfer %s to %s?",
                                Lang.TITLE, eco.format(amount), args[1]));
                        player.sendMessage(String.format("To confirm, do /eco pay %s %s confirm", args[1], args[2]));
                        return;
                    } else if (!(args[3].equalsIgnoreCase("confirm"))) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cInvalid Confirmation Statement, Cancelling Transfer!"));
                        return;
                    }
                }

                // transferring amounts to players
                boolean result = eco.sendMoney(player, target, amount);
                String formatted = eco.format(amount);
                if (result) {
                    player.sendMessage(String.format(Lang.TITLE + "Payment was Successful, sent %s to %s", formatted, target.getName()));
                    target.sendMessage(String.format(Lang.TITLE + "Received %s from %s", formatted, player.getName()));
                } else {
                    player.sendMessage(mWarning + "Payment could not be done, cancelling transaction!");
                }
            } else {
                player.sendMessage(String.valueOf(Lang.INVALID_ARGS));
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length == 2) {
            List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
            for (Player value : playerList) {
                arguments.add(value.getName());
            }
            return arguments;
        }
        if (args.length == 3) {
            arguments.add("<amount>");
            arguments.add("max");
            return arguments;
        }
        return null;
    }
}
