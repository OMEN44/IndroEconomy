package io.github.omen44.indroEconomy.commands.economy;

import io.github.omen44.indroEconomy.IndroEconomy;
import io.github.omen44.indroEconomy.utils.Lang;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandReload extends SubCommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Reload config files";
    }

    @Override
    public String getSyntax() {
        return "/eco reload";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (sender.hasPermission("indroEconomy.admin.reload")) {
            sender.sendMessage(ChatColor.BLUE + "Reloading the server");
            FileConfiguration config = IndroEconomy.getInstance().getConfig();
            IndroEconomy.setSavedConfig(config);
        } else {
            sender.sendMessage(Lang.TITLE.toString() + Lang.NO_PERMS);
        }

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
