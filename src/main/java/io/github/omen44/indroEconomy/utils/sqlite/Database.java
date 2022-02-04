package io.github.omen44.indroEconomy.utils.sqlite;

import io.github.omen44.indroEconomy.IndroEconomy;
import io.github.omen44.indroEconomy.models.EconomyModel;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public abstract class Database {
    IndroEconomy plugin;
    Connection connection;
    // The name of the table we created back in SQLite class.
    public String table = "economy";
    public Database(IndroEconomy instance){
        plugin = instance;
    }

    public abstract Connection getSQLConnection();

    public abstract void load();

    public void initialize(){
        connection = getSQLConnection();
        try{
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE playerUUID = ?");
            ResultSet rs = ps.executeQuery();
            close(ps,rs);

        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "Unable to retreive connection", ex);
        }
    }

    /**
     * Gets the wallet from the Database
     *
     * @param playerUUID The playerUUID to check for
     * @return an amount if an account exists; null if not
     */
    public Integer getWallet(String playerUUID) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE playerUUID = '"+playerUUID+"';");

            rs = ps.executeQuery();
            while(rs.next()){
                if(rs.getString("playerUUID").equalsIgnoreCase(playerUUID.toLowerCase())){ // Tell database to search for the playerUUID you sent into the method. e.g getTokens(sam) It will look for sam.
                    return rs.getInt("wallet"); // Return the player's wallet
                }
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return null;
    }

    /**
     * Gets the bank from the Database
     *
     * @param playerUUID The playerUUID to check for
     * @return an amount if an account exists; null if not
     */
    public Integer getBank(String playerUUID) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player = '"+playerUUID+"';");

            rs = ps.executeQuery();
            while(rs.next()){
                if(rs.getString("playerUUID").equalsIgnoreCase(playerUUID.toLowerCase())){
                    return rs.getInt("bank");
                }
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return null;
    }

    // Now we need methods to save things to the database
    public void setAccount(Player player, Integer wallet, Integer bank) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("REPLACE INTO " + table + " (playerUUID,wallet,bank) VALUES(?,?,?)"); // IMPORTANT. In SQLite class, We made 3 colums. player, Kills, Total.
            ps.setString(1, player.getUniqueId().toString());
            ps.setInt(2, wallet);
            ps.setInt(3, bank);
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }

    public void close(PreparedStatement ps,ResultSet rs){
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            Error.close(plugin, ex);
        }
    }
}
