package omen44.omens_economy.events;

import omen44.omens_economy.Main;
import omen44.omens_economy.datamanager.MySQL;
import omen44.omens_economy.utils.IDUtils;
import omen44.omens_economy.utils.SQLUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

public final class WhitelistRegister {
    private int playerID;
    MySQL mySQL = new MySQL();
    Connection con;
    SQLUtils sqlUtils;
    IDUtils id;

    public int register(String discordIGN, String minecraftIGN) {
        con = mySQL.getConnection();
        sqlUtils = new SQLUtils(con);
        id = new IDUtils();

        if (con == null) {
            return 5; // means that the SQL database was incorrectly initialised
        }
        String discordName = sqlUtils.getDBString("discordIGN", "discordIGN", discordIGN, "accounts");
        if (discordName == null) {
            return 1; // means that the discord username has already been registered.
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
            return 2; // means that there's something wrong with the mc name servers
        }
        if (!minecraftStatus) {
            return 3; // means that the minecraft username is either not a premium account or the authentication servers are down
        }
        String minecraftName = sqlUtils.getDBString("minecraftIGN", "discordIGN", minecraftIGN, "accounts");
        if (minecraftName != null) {
            return 4; // means that the minecraft username has already been registered.
        }
        playerID = id.generateID(discordIGN, minecraftIGN);
        Player p = Bukkit.getServer().getPlayer(minecraftIGN);
        if (p != null) {
            p.setWhitelisted(true);
        }
        return 0;
    }

    public int getPlayerID() {
        return playerID;
    }
}
