package omen44.omens_economy.commands.chestshop;

import omen44.omens_economy.Main;
import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.listeners.ChestShopPlace;
import omen44.omens_economy.utils.EconomyUtils;
import omen44.omens_economy.utils.ShortcutsUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandCancelShop implements CommandExecutor {
    public Main main;

    public CommandCancelShop(Main main) {
        this.main = main;
        Bukkit.getPluginCommand("cancelshop").setExecutor(this);
    }

    public EconomyUtils economyUtils;

    ShortcutsUtils s;
    FileConfiguration config = ConfigTools.getFileConfig("config.yml");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (label.equalsIgnoreCase("cancelshop")) {
                ChestShopPlace csp = new ChestShopPlace(main);
                int shopPrice = config.getInt("shop.shopPrice");
                int playerWallet = economyUtils.getMoney(p, "wallet");

                if (playerWallet >= shopPrice && csp.isShopPlacing()) {
                    playerWallet += shopPrice;
                    p.sendMessage(s.prefix + s.nMessage + "Canceled shop creation! Refunding money!");
                    economyUtils.setWallet(p, playerWallet);
                    csp.setShopPlacing(false);
                }
                return true;
            }
        }
        return true;
    }
}

