package omen44.omens_economy.utils;

import omen44.omens_economy.datamanager.MySQL;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IDUtils {
    private int accountID;
    MySQL mySQL = new MySQL();
    SQLUtils sqlUtils = new SQLUtils();

    public int generateID(String discordIGN, String minecraftIGN) {
        int availableID = 0;
        try {
            PreparedStatement ps = mySQL.getConnection().prepareStatement("SELECT accountID FROM accounts WHERE minecraftIGN, discordIGN IS NULL");
            ResultSet rs = ps.executeQuery();
            if (rs != null){
                availableID = rs.getInt("accountID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (availableID == 0) {
            int lastInt = 0;
            try {
                PreparedStatement ps = mySQL.getConnection().prepareStatement("SELECT accountID FROM accounts ORDER BY accountID LIMIT 1;");
                ResultSet rs = ps.executeQuery();
                lastInt = rs.getInt("accountID");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            lastInt += 1;
            try {
                PreparedStatement ps = mySQL.getConnection().prepareStatement("INSERT INTO accounts (discordIGN, minecraftIGN) VALUES (" + discordIGN + "," + minecraftIGN + ")");
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            availableID = lastInt;
        }
        sqlUtils.createRow("accountID", String.valueOf(availableID), "economy");
        sqlUtils.createRow("accountID", String.valueOf(availableID), "shops");
        return availableID;
    }

    public String getMinecraftID(int id) {
        String minecraftIGN = "";
        try {
            PreparedStatement ps = mySQL.getConnection().prepareStatement("SELECT minecraftID FROM accounts WHERE accountID=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            minecraftIGN = rs.getString("minecraftID");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return minecraftIGN;
    }

    public String getDiscordID(int id) {
        String discordIGN = "";
        try {
            PreparedStatement ps = mySQL.getConnection().prepareStatement("SELECT discordID FROM accounts WHERE accountID=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            discordIGN = rs.getString("discordID");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discordIGN;
    }


    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }
}
