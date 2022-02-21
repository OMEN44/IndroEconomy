package io.github.omen44.indroEconomy.events;

import io.github.omen44.indroEconomy.IndroEconomy;
import io.github.omen44.indroEconomy.utils.EconomyUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EventOnEntityKill implements Listener {
    @EventHandler
    public void onEntityKill(EntityDeathEvent event) {
        boolean enabled = IndroEconomy.getInstance().getConfig().getBoolean("");
        EconomyUtils eco = new EconomyUtils();
        if (enabled) {
            LivingEntity entity = event.getEntity();
            if (entity.getType() != EntityType.PLAYER && entity.getKiller() != null && entity.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
                final int monetaryEarns = (int) entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                Player player = entity.getKiller();

                eco.addWallet(player, monetaryEarns);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.BLUE + "+" + eco.format(monetaryEarns)));
            }
        }
    }
}
