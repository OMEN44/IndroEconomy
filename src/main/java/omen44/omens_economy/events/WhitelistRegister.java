package omen44.omens_economy.events;

import omen44.omens_economy.utils.IDUtils;
import omen44.omens_economy.utils.SQLUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class WhitelistRegister {
    SQLUtils sqlUtils = new SQLUtils();
    IDUtils id = new IDUtils();
    private int playerID = 0;

    public String register(String discordIGN, String minecraftIGN) {
        String discordName = sqlUtils.getDBString("discordIGN", "discordIGN", discordIGN, "accounts");
        if (discordName != null) {
            return "E-DNAR"; // means that the discord username has already been registered.
        }
        boolean minecraftStatus;
        try {
            URL url= new URL("https://sessionserver.mojang.com/session/minecraft/hasJoined?username=" + minecraftIGN + "&serverId=");
            Bukkit.getLogger().info("Starting HTTP GET request to https://sessionserver.mojang.com/session/minecraft/hasJoined?username=" + minecraftIGN + "&serverId=");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                minecraftStatus = true;
            } else {
                Bukkit.getLogger().info("Error: HTTP/" + connection.getResponseCode() + ", " + connection.getResponseMessage());
                minecraftStatus = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "E-MCSD"; // means that there's something wrong with the mc name servers
        }
        if (!minecraftStatus) {
            return "E-MCNP"; // means that the minecraft username is either not a premium account or the authentication servers are down
        }
        String minecraftName = sqlUtils.getDBString("minecraftIGN", "discordIGN", minecraftIGN, "accounts");
        if (minecraftName != null) {
            return "E-MCAR"; // means that the minecraft username has already been registered.
        }
        playerID = id.generateID(discordIGN, minecraftIGN);
        Player p = Bukkit.getServer().getPlayer(minecraftIGN);
        if (p != null) {
            p.setWhitelisted(true);
        }
        return "valid";
    }

    public int getPlayerID() {
        return playerID;
    }
}
