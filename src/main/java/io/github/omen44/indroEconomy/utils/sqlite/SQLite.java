package io.github.omen44.indroEconomy.utils.sqlite;

import io.github.omen44.indroEconomy.IndroEconomy;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class SQLite extends Database {
    String dbname;
    public SQLite(IndroEconomy instance){
        super(instance);
        dbname = plugin.getConfig().getString("SQLite.Filename", "economy"); // Set the table name here e.g player_kills
    }

    public String SQLiteCreateEconomyTable = "CREATE TABLE IF NOT EXISTS economy (" + // make sure to put your table name in here too.
            "`playerUUID` varchar(64) NOT NULL," + // This creates the different colums you will save data too. varchar(32) Is a string, int = integer
            "`wallet` int(11) NOT NULL," +
            "`bank` int(11) NOT NULL," +
            "PRIMARY KEY (`playerUUID`)" +  // This is creating 3 colums Player, Kills, Total. Primary key is what you are going to use as your indexer. Here we want to use player so
            ");"; // we can search by player, and get kills and total. If you some how were searching kills it would provide total and player.


    // SQL creation stuff, You can leave the blow stuff untouched.
    public Connection getSQLConnection() {
        File dataFolder = new File(plugin.getDataFolder(), dbname+".db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: "+dbname+".db");
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(SQLiteCreateEconomyTable);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initialize();
    }
}
