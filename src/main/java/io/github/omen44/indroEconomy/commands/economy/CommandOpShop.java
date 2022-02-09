package io.github.omen44.indroEconomy.commands.economy;

import io.github.omen44.indroEconomy.menus.OpShopMenu;
import io.github.omen44.indroEconomy.utils.Lang;
import me.kodysimpson.simpapi.command.SubCommand;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.MenuManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;

public class CommandOpShop extends SubCommand {
    @Override
    public String getName() {
        return "opshop";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "The place to purchase slightly OP enchants";
    }

    @Override
    public String getSyntax() {
        return "/eco opshop";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(Lang.TITLE.toString() + Lang.PLAYER_ONLY);
        } else {
            try {
                MenuManager.openMenu(OpShopMenu.class, player);
            } catch (MenuManagerException | MenuManagerNotSetupException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] strings) {
        return null;
    }
}
