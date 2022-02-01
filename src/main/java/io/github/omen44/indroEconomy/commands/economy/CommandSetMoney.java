package io.github.omen44.indroEconomy.commands.economy;

import io.github.omen44.indroEconomy.models.EconomyModel;
import io.github.omen44.indroEconomy.storage.EconomyStorageUtil;
import io.github.omen44.indroEconomy.utils.EconomyUtils;
import io.github.omen44.indroEconomy.datamanager.ConfigTools;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This is a player only command!");
        } else {
            EconomyUtils eco = new EconomyUtils();
            ConfigTools configTools = new ConfigTools();
            FileConfiguration config = configTools.getConfig("config.yml");

            if (args.length == 4 && commandSender.hasPermission("indroEconomy.admin.setMoney")) {
                // initialising values
                final String symbol = config.getString("money.moneySymbol");
                final String type = args[1];
                final Player target = Bukkit.getPlayer(args[2]);
                final int amount = Integer.parseInt(args[3]);

                if (amount <= 0 || amount >= 100000000) {
                    commandSender.sendMessage(mNormal + "<amount> must be a positive, non-negative integer!");
                    return;
                }

                if (target == null || !eco.hasAccount(target)) {
                    commandSender.sendMessage(mWarning + "<target> must be a valid Minecraft Username, and have joined at least once!");
                    return;
                }

                if (type.equals("wallet")) {
                    eco.setWallet(target, amount);
                    int wallet = eco.getWallet(target);
                    commandSender.sendMessage(ColorTranslator.translateColorCodes("&fSet &a" + target.getName() + "'s &fwallet to " + symbol + wallet));
                } else if (type.equals("bank")) {
                    eco.setBank(target, amount);
                    int bank = eco.getBank(target);
                    commandSender.sendMessage(ColorTranslator.translateColorCodes("&fSet &a" + target.getName() + "'s &fbank to " + symbol + bank));
                }
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
        if (args.length == 3) {
            List<EconomyModel> economyModels = EconomyStorageUtil.findAllAccounts();
            for (EconomyModel model : economyModels) {
                arguments.add(Objects.requireNonNull(Bukkit.getPlayer(model.getPlayerUUID())).getName());
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
