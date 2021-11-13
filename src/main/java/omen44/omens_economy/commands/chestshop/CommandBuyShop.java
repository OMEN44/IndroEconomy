package omen44.omens_economy.commands.chestshop;

import omen44.omens_economy.Main;
import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.listeners.ChestShopPlace;
import omen44.omens_economy.utils.EconomyUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandBuyShop implements TabExecutor {
    public Main main;
    public CommandBuyShop(Main main) {this.main = main;}
    public EconomyUtils economyUtils;

    FileConfiguration config = ConfigTools.getFileConfig("config.yml");
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (label.equalsIgnoreCase("buyshop")) {
                ChestShopPlace csp = new ChestShopPlace(main);
                int shopPrice = config.getInt("shop.shopPrice");
                int playerWallet = economyUtils.getMoney(p, "wallet");

                if (playerWallet >= shopPrice && csp.isShopPlacing()) {
                    playerWallet -= shopPrice;
                    economyUtils.setWallet(p, playerWallet);
                    csp.setShopPlacing(true);
                }
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
