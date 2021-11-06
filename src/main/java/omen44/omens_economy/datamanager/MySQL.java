package omen44.omens_economy.datamanager;

import omen44.omens_economy.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class MySQL {
    private final Main main;

    FileConfiguration config = ConfigTools.getFileConfig("config.yml");

    private final String host = config.getString("database.host");
    private final String port = config.getString("database.port");
    private final String database = config.getString("database.database");
    private final String username = config.getString("database.user");
    String pass = config.getString("database.password");
    private String password = pass;

    private Connection connection;

    public MySQL(Main main) {this.main = main;}

    public boolean isConnected() {
        return (connection != null);
    }

    public void connect() {
        if (Objects.equals(pass, ";")){
            password = "";
        }
        if (!isConnected()) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + username + "&password=" + password + "&useSSl=false");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}

