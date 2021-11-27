package omen44.omens_economy.events;

import omen44.omens_economy.datamanager.MySQL;
import omen44.omens_economy.utils.SQLUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;

public final class WhitelistUtils {
    private int playerID;
    MySQL mySQL = new MySQL();
    Connection con;
    SQLUtils sqlUtils;

    public int register(String discordIGN, String minecraftIGN) {
        con = mySQL.getConnection();
        sqlUtils = new SQLUtils(con);

        if (con == null) {
            return 1; // means that the SQL database was incorrectly initialised
        }

        String discordName = sqlUtils.getDBString("discordIGN", "discordIGN", discordIGN, "accounts");
        if (!discordName.equals("")) {
            return 2; // means that the discord username has already been registered.
        }

        String minecraftName = sqlUtils.getDBString("minecraftIGN", "minecraftIGN", minecraftIGN, "accounts");
        if (!minecraftName.equals("")) {
            return 3; // means that the minecraft username has already been registered.
        }

        sqlUtils.createRow("minecraftIGN", minecraftIGN, "accounts");
        sqlUtils.createRow("discordIGN", discordIGN, "accounts");
        return 4; // means that the system successfully registered your username
    }

    public int unregister(String discordIGN) {
        con = mySQL.getConnection();
        sqlUtils = new SQLUtils(con);

        if (con == null) {
            return 1; // means that the SQL database was incorrectly initialised
        }

        String discordName = sqlUtils.getDBString("discordIGN", "discordIGN", discordIGN, "accounts");
        if (discordName.equals("")) {
            return 2; // means that the discord username does not exist.
        }

        String minecraftName = sqlUtils.getDBString("minecraftIGN", "discordIGN", discordIGN, "accounts");
        if (minecraftName.equals("")) {
            return 3; // means that the minecraft username does not exist.
        }
        Player p = Bukkit.getPlayer(minecraftName);
        if (p == null) {
            return 3; // means the same as above
        }

        sqlUtils.remove("minecraftIGN", discordIGN, "accounts");
        sqlUtils.remove("discordIGN", discordIGN, "accounts");

        p.setWhitelisted(false);
        return 4; // means that the system successfully registered your username
    }
}
