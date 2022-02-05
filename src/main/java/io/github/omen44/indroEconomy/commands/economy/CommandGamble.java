package io.github.omen44.indroEconomy.commands.economy;

import io.github.omen44.indroEconomy.IndroEconomy;
import io.github.omen44.indroEconomy.menus.MysteryMenu;
import io.github.omen44.indroEconomy.utils.EconomyUtils;
import me.kodysimpson.simpapi.command.SubCommand;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.MenuManager;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

import static io.github.omen44.indroEconomy.utils.ShortcutsUtils.mNormal;
import static io.github.omen44.indroEconomy.utils.ShortcutsUtils.mWarning;

public class CommandGamble extends SubCommand {
    @Override
    public String getName() {
        return "gamble";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Allows players to lose money, lol";
    }

    @Override
    public String getSyntax() {
        return "/eco gamble <type>";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("House Rule: The console cannot play.");
        } else {
            Player player = (Player) sender;
            if (args.length > 1) {
                EconomyUtils eco = new EconomyUtils();
                String format = args[1];

                int playerWallet;
                try {
                    playerWallet = eco.getWallet(player);
                } catch (NullPointerException e) {
                    player.sendMessage("Your account doesn't exist, contact an admin to get it fixed");
                    return;
                }
                if (player.hasPermission("indroEconomy.gamble")) {
                    switch (format) {
                        case "slots" -> player.sendMessage(mWarning + "Sorry, but this is under construction!");
                        case "mystery" -> {
                            if (playerWallet >= 350) {
                                eco.minusWallet(player, 350);
                                player.sendMessage(mNormal + "You have paid $350 to use the Mystery Box!");
                                try {
                                    player.setMetadata("clicksRemaining", new FixedMetadataValue(IndroEconomy.getInstance(), 3));
                                    MenuManager.openMenu(MysteryMenu.class, player);
                                } catch (MenuManagerException | MenuManagerNotSetupException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                player.sendMessage(mWarning + "Sorry, but we don't give these out for free.");
                            }
                        }
                    }
                } else {
                    player.sendMessage(mWarning + "Sorry, but you need to be a bit older to use this.");
                }
            } else {
                player.sendMessage(mWarning + "Syntax Error! \n" + mWarning + "Format: /eco pay <player> <amount>");
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length == 2) {
            arguments.add("slots");
            arguments.add("mystery");
            return arguments;
        }
        return null;
    }
}
