package io.github.omen44.indroEconomy.commands.economy;

import io.github.omen44.indroEconomy.datamanager.ConfigTools;
import io.github.omen44.indroEconomy.utils.EconomyUtils;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

import static io.github.omen44.indroEconomy.utils.ShortcutsUtils.*;

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
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This is a player only command!");
        }
        if (commandSender instanceof Player) {
            ConfigTools configTools = new ConfigTools();
            FileConfiguration config = configTools.getConfig("config.yml");
            String symbol = config.getString("money.moneySymbol");

            Player player = (Player) commandSender;
            EconomyUtils eco = new EconomyUtils();

            // /eco pay <player> <amount>
            if (args.length > 3) {
                Player target = Bukkit.getPlayer(args[1]);
                int amount = Integer.parseInt(args[2]);

                // error checkers
                if (amount <= 0 || amount >= 100000000) {
                    player.sendMessage(mNormal + "<amount> must be a positive, non-negative integer!");
                    return;
                }

                if (target == null || !eco.hasAccount(target)) {
                    player.sendMessage(mWarning + "<target> must be a valid Minecraft Username, and have joined at least once!");
                    return;
                }

                // transferring amounts to players
                boolean result = eco.sendMoney(player, target, amount);
                if (result) {
                    player.sendMessage(mNormal + "Payment was Successful, sent " + symbol + amount + " to " + target.getName());
                    target.sendMessage(mNormal + "Received " + symbol + amount + " from " + player.getName());
                } else {
                    player.sendMessage(mWarning + "Payment could not be done, cancelling transaction!");
                }

            } else {
                player.sendMessage(mWarning + "Syntax Error! \n" + mWarning + "Format: /eco pay <player> <amount>");
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
