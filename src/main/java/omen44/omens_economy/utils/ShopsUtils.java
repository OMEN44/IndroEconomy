package omen44.omens_economy.utils;

import omen44.omens_economy.datamanager.MySQL;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShopsUtils {
    MySQL mySQL = new MySQL();
    Connection conn = mySQL.getConnection();

    /**
     * Gets the shopID based on a string of the shopOwner's UUID.
     * @param shopOwner The player object of the shopOwner.
     * @param shopName The name that the player has given the shop
     * @return -1 if the shopOwner doesn't exist, else the number of the shopID.
     */
    public int getShopID(Player shopOwner, String shopName) {
        String ownerUUID = shopOwner.getUniqueId().toString();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT shopID FROM shops WHERE shopOwner =?, shopName =? ORDER BY shopID DESC;");
            ps.setString(1, ownerUUID);
            ps.setString(2, shopName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("shopID");
            }
        } catch (SQLException ex) {
            MySQL.printSQLException(ex);
        }
        return -1;
    }

    public int getShopPrice(Player shopOwner, String shopName) {
        String ownerUUID = shopOwner.getUniqueId().toString();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT shopPrice FROM shops WHERE shopOwner =?, shopName =?;");
            ps.setString(1, ownerUUID);
            ps.setString(2, shopName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("shopPrice");
            }
        } catch (SQLException ex) {
            MySQL.printSQLException(ex);
        }
        return -1;
    }


    /**
     * Gets a list of all the shop names.
     *
     * @param shopOwner The shop's owner.
     * @return a list of all the shops the owner has, null if there is none.
     */
    public List<String> getShopName(Player shopOwner) {
        List<String> results = new ArrayList<>();
        String ownerUUID = shopOwner.getUniqueId().toString();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT shopName FROM shops WHERE shopOwner =? ORDER BY shopID DESC;");
            ps.setString(1, ownerUUID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(rs.getString("shopName"));
            }
            return results;
        } catch (SQLException ex) {
            MySQL.printSQLException(ex);
        }
        return null;
    }

    /**
     * Checks if the shop's name exists, that is, the shop's owner has already named a shop that name.
     * Note that this only checks if it exists, not how many shops has its name.
     *
     * @param shopOwner The shop's owner, that is, the player which owns the shop
     * @param shopName The name of the shop checked
     * @return true if it exists, false if it doesn't
     */

    public boolean checkShopName(Player shopOwner, String shopName) {
        String ownerUUID = shopOwner.getUniqueId().toString();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT shopName FROM shops WHERE shopOwner =?, shopName =?;");
            ps.setString(1, ownerUUID);
            ps.setString(2, shopName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            MySQL.printSQLException(ex);
        }
        return false;
    }

    /**
     * Sets the shop name using its shopID.
     * @param shopOwner The player which owns the shop
     * @param shopName The new name of the shop
     * @param shopID The ID of the shop that's getting adjusted
     */
    public void setShopName(Player shopOwner, int shopID, String shopName) {
        String ownerUUID = shopOwner.getUniqueId().toString();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM shops WHERE shopOwner=?, shopID=?");
            ps.setString(1, ownerUUID);
            ps.setInt(2, shopID);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                return;
            }
            ps = conn.prepareStatement("INSERT INTO shops (shopName) VALUES (?);");
            ps.setString(1, shopName);
            ps.executeUpdate();
        } catch (SQLException ex) {
            MySQL.printSQLException(ex);
        }
    }

    /**
     * Sets the shop name using its shopID.
     * @param shopOwner The player which owns the shop
     * @param shopPrice The new price of the shop
     * @param shopID The ID of the shop that's getting adjusted
     */
    public void setShopPrice(Player shopOwner, int shopID, int shopPrice) {
        String ownerUUID = shopOwner.getUniqueId().toString();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM shops WHERE shopOwner=?, shopID=?");
            ps.setString(1, ownerUUID);
            ps.setInt(2, shopID);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                return;
            }
            ps = conn.prepareStatement("INSERT INTO shops (shopName) VALUES (?);");
            ps.setInt(1, shopPrice);
            ps.executeUpdate();
        } catch (SQLException ex) {
            MySQL.printSQLException(ex);
        }
    }

    /**
     * Creates a new shop, with the next shopID
     * @param shopOwner The player which owns the shop
     * @param shopPrice The new price of the shop
     * @param shopName The ID of the shop that's getting adjusted
     * @return -1 if creating the shop id is invalid, or the shopID that it generated with the uuid
     */
    public int createShop(Player shopOwner, String shopName, int shopPrice) {
        String ownerUUID = shopOwner.getUniqueId().toString();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM shops WHERE shopOwner=?, shopID=?");
            ps.setString(1, ownerUUID);
            ps.setString(2, shopName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rs.close();
                return -1;
            }

            ps = conn.prepareStatement("INSERT INTO shops (shopOwner, shopName, shopPrice) VALUES (?, ?, ?);");
            ps.setString(1, ownerUUID);
            ps.setString(2, shopName);
            ps.setInt(3, shopPrice);
            ps.executeUpdate();

            ps = conn.prepareStatement("SELECT shopID FROM shops WHERE shopOwner=?, shopID=?");
            ps.setString(1, ownerUUID);
            ps.setString(2, shopName);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("shopID");
            }
        } catch (SQLException ex) {
            MySQL.printSQLException(ex);
        }
        return -1;
    }
}