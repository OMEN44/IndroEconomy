package io.github.omen44.indroEconomy.commands.tp;

import io.github.omen44.indroEconomy.datamanager.ConfigTools;
import io.github.omen44.indroEconomy.utils.EconomyUtils;
import io.github.omen44.indroEconomy.utils.YamlUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static io.github.omen44.indroEconomy.utils.ShortcutsUtils.mNormal;
import static io.github.omen44.indroEconomy.utils.ShortcutsUtils.mWarning;

public class CommandBack implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("The console cannot die.");
        } else {
            EconomyUtils eco = new EconomyUtils();
            ConfigTools configTools = new ConfigTools();
            FileConfiguration config = configTools.getConfig("config");
            boolean backAllowed = config.getBoolean("back.backAllowed");
            String symbol = config.getString("money.moneySymbol");

            YamlUtils yamlUtils = new YamlUtils("backLocation");
            FileConfiguration deathSaves = yamlUtils.getConfig();

            if (label.equalsIgnoreCase("back")) {
                Location lastLocation = (Location) deathSaves.get(player.getUniqueId().toString());

                if (backAllowed || player.hasPermission("indroEconomy.back")) {
                    final int playerWallet = eco.getWallet(player);
                    if (playerWallet >= config.getInt("back.backCost")) {
                        if (lastLocation == null) {
                            player.sendMessage(mWarning + "You have already used /back before!");
                            return true;
                        }
                        final int backCost = config.getInt("back.backCost");
                        eco.minusWallet(player, backCost);
                        player.sendMessage(mNormal + "Deducted " + symbol + backCost + " from your wallet");
                        player.sendMessage(mNormal + "Warping to your last death point!");

                        player.teleport(lastLocation);
                        player.sendMessage(mNormal + "Warped!");
                        deathSaves.set(player.getUniqueId().toString(), null);
                        yamlUtils.saveFile(deathSaves);
                    } else {
                        player.sendMessage(mWarning + "Sorry, no death saves for the poor.");
                    }
                    return true;
                } else {
                    player.sendMessage(mWarning + "/back is not allowed");
                }
            }
        }
        return true;
    }
}