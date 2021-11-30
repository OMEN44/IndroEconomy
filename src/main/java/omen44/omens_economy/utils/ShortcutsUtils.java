package omen44.omens_economy.utils;

import org.bukkit.ChatColor;

public class ShortcutsUtils {
    public String prefix;
    public ChatColor error; // sets format for error
    public ChatColor iMessage; // sets format for important message
    public ChatColor nMessage; // sets format for normal message
    public ShortcutsUtils() {
        prefix = ChatColor.BLUE + "[OMEN'S ECONOMY] " + ChatColor.WHITE;
        error = ChatColor.RED;
        iMessage = ChatColor.GOLD;
        nMessage = ChatColor.YELLOW;
    }
}
