package io.github.omen44.indroEconomy.commands.economy;

import io.github.omen44.indroEconomy.datamanager.ConfigTools;
import io.github.omen44.indroEconomy.utils.EconomyUtils;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

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
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This is a player only command!");
        } else {
            if (args.length > 3) {
                ConfigTools configTools = new ConfigTools();
                FileConfiguration config = configTools.getConfig("config.yml");
                String symbol = config.getString("money.moneySymbol");

                EconomyUtils eco = new EconomyUtils();

                // initialise values
                Player player = (Player) commandSender;
                String type = args[1];
                int amount = Integer.parseInt(args[2]);

                if (type == null) {
                    commandSender.sendMessage("This is a player only command!");
                    return;
                }

                boolean result = eco.transferMoney(player, type, amount);
                if (result) {
                    final int wallet = eco.getWallet(player);
                    final int bank = eco.getBank(player);
                    player.sendMessage(ColorTranslator.translateColorCodes("&aTransfer was successful!"));
                    player.sendMessage(mNormal + "Current Wallet Balance: " + symbol + wallet);
                    player.sendMessage(mNormal + "Current Bank Balance: " + symbol + bank);
                } else {
                    player.sendMessage(mWarning + "Payment could not be done, cancelling transaction!");
                }
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] strings) {
        return null;
    }
}
