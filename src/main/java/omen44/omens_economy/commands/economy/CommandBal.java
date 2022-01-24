package omen44.omens_economy.commands.economy;

import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.utils.EconomyUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static omen44.omens_economy.utils.ShortcutsUtils.mNormal;

/*this class handles
- /bal (wallet/bank)
 */

public class CommandBal implements TabExecutor {
    ConfigTools configTools = new ConfigTools();
    FileConfiguration config = configTools.getConfig("config.yml");
    String symbol = config.getString("money.moneySymbol");
    EconomyUtils eco = new EconomyUtils();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (label.equalsIgnoreCase("bal")) {
                final int wallet = eco.getWallet(p);
                final int bank = eco.getBank(p);
                String type;

                try {
                    type = args[0];
                } catch (ArrayIndexOutOfBoundsException e) {
                    type = "";
                }

                switch (type) {
                    case "wallet": p.sendMessage(mNormal + "Wallet Balance: " + symbol + wallet);
                    case "bank": p.sendMessage(mNormal + "Bank Balance: " + symbol + bank);
                    default: p.sendMessage(mNormal + "Total Balance: " + symbol + wallet+bank);
                }
            }
            return false;
        } else {
            Bukkit.getLogger().warning("Warning: Only player executable");
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> args1 = new ArrayList<>();
            args1.add(ChatColor.YELLOW + "bank");
            args1.add(ChatColor.YELLOW + "wallet");
            return args1;
        }
        return null;
    }
}