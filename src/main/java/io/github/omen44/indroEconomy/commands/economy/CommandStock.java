package io.github.omen44.indroEconomy.commands.economy;

import io.github.omen44.indroEconomy.tasks.TaskStockUpdate;
import io.github.omen44.indroEconomy.utils.EconomyUtils;
import io.github.omen44.indroEconomy.utils.Lang;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CommandStock extends SubCommand {
    @Override
    public String getName() {
        return "stock";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Trade diamonds on the diamond floor...";
    }

    @Override
    public String getSyntax() {
        return "/eco stock <buy/sell> (amount)";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(String.valueOf(Lang.PLAYER_ONLY));
        } else if (player.hasPermission("indroEconomy.stock")) {
            EconomyUtils eco = new EconomyUtils();

            int diamondPrice = TaskStockUpdate.getDiamondPrice();
            if (args.length == 1) {
                player.sendMessage("===========",
                        "    Trading at " + eco.format(diamondPrice) + " per diamond",
                        "===========" );
            } else if (args.length == 3) {
                String request = args[1];
                final int amount = Integer.parseInt(args[2]);
                int playerWallet = eco.getWallet(player);
                int cost = amount * diamondPrice;
                final int diamondStock = TaskStockUpdate.getDiamondStock();

                if (request.equalsIgnoreCase("buy")) { // buy diamonds on the trade
                    if (playerWallet >= cost && diamondStock - amount >= 0) {
                        eco.minusWallet(player, cost);
                        player.sendMessage(String.format("%sBuying %s diamonds for %s", Lang.TITLE, amount, eco.format(cost)));
                        player.getInventory().addItem(new ItemStack(Material.DIAMOND, amount));
                        TaskStockUpdate.setDiamondStock(TaskStockUpdate.getDiamondStock()-amount);
                    } else {
                        player.sendMessage(Lang.TITLE.toString() + Lang.NOT_ENOUGH_MONEY);
                    }
                } else if (request.equalsIgnoreCase("sell")) { // sell diamonds on the trade
                    boolean requiredItems = player.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND), amount);
                    if (requiredItems) {
                        player.getInventory().removeItem(new ItemStack(Material.DIAMOND, amount));
                        eco.addWallet(player, cost);
                        player.sendMessage(String.format("%sSelling %s diamonds for %s", Lang.TITLE, amount, eco.format(cost)));
                        TaskStockUpdate.setDiamondStock(TaskStockUpdate.getDiamondStock()+amount);
                    } else {
                        player.sendMessage(Lang.TITLE + "You do not have enough items!");
                    }
                }
            }
        } else {
            sender.sendMessage(Lang.TITLE.toString() + Lang.NO_PERMS);
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length == 2) {
            arguments.add("buy");
            arguments.add("sell");
        } else if (args.length == 3) {
            arguments.add("<amount>");
        }
        return arguments;
    }
}
