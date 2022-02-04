package io.github.omen44.indroEconomy.commands.economy;

import io.github.omen44.indroEconomy.utils.EconomyUtils;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandCreateAccount extends SubCommand {
    @Override
    public String getName() {
        return "create";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "allows you to create an account (Warning: resets your balance)";
    }

    @Override
    public String getSyntax() {
        return "/eco create";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This is a player only command!");
        } else {
            EconomyUtils eco = new EconomyUtils();
            Player player = (Player) sender;
            if (args.length == 2) {
                eco.createAccount(player);
            } else {
                player.sendMessage("Are you sure? To confirm, type /eco create confirm");
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
