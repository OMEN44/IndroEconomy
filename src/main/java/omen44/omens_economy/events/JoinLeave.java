package omen44.omens_economy.events;

import omen44.omens_economy.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeave implements Listener {

    public Main main;
    public JoinLeave(Main main) {this.main = main;}

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        main.sqlUtils.createRow("UUID", uuid, "players");


    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {

    }
}
