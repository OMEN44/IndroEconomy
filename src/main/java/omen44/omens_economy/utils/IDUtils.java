package omen44.omens_economy.utils;

import omen44.omens_economy.datamanager.MySQL;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IDUtils {
    private int accountID;
    MySQL mySQL = new MySQL();

    public int generateID() {
        try {
            PreparedStatement ps = mySQL.getConnection().prepareStatement("SELECT accountID FROM accounts WHERE minecraftIGN, discordIGN IS NOT NULL");
            ResultSet rs = ps.executeQuery();
            if (rs != null){

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
