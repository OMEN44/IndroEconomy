package omen44.omens_economy.events;

import omen44.omens_economy.Main;
import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.utils.SQLUtils;
import omen44.omens_economy.utils.ShortcutsUtils;
import org.apache.commons.collections4.list.SetUniqueList;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeave implements Listener {

    public Main main;
    public JoinLeave(Main main) {this.main = main;}
    public void EconomyUtils(Main main) {this.main = main;}

    ConfigTools configTools = new ConfigTools();
    FileConfiguration config = configTools.getFileConfig("config.yml");

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        ShortcutsUtils s = new ShortcutsUtils();
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        SQLUtils sqlUtils = new SQLUtils();

        sqlUtils.createRow("UUID", uuid, "players");
        event.setJoinMessage(ChatColor.YELLOW + "Welcome to IndroCraft!");
        if (!player.hasPlayedBefore()) {
            main.economyUtils.setBank(player, config.getInt("defaultAmount"));
            player.sendMessage(s.prefix + "You start with " + config.getString("money.moneySymbol") + config.getInt("money.defaultAmount"));
        }
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {

    }
}
