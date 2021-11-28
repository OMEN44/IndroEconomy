package omen44.omens_economy.events;

import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.datamanager.MySQL;
import omen44.omens_economy.utils.EconomyUtils;
import omen44.omens_economy.utils.SQLUtils;
import omen44.omens_economy.utils.ShortcutsUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;

public class EventOnPlayerJoinLeave implements Listener {
    EconomyUtils eco = new EconomyUtils();
    ShortcutsUtils s = new ShortcutsUtils();
    MySQL mySQL = new MySQL();
    Connection conn = mySQL.getConnection();
    SQLUtils sqlUtils = new SQLUtils(conn);

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = ConfigTools.getFileConfig("config.yml");

        sqlUtils.createPlayer(player);
        event.setJoinMessage(ChatColor.YELLOW + "Welcome to IndroCraft!");
        if (!player.hasPlayedBefore()) {
            eco.setWallet(player, 0);
            eco.setBank(player, config.getInt("defaultAmount"));
            player.sendMessage(s.prefix + "You start with " + config.getString("money.moneySymbol") + config.getInt("money.defaultAmount"));
        }
    }


}
