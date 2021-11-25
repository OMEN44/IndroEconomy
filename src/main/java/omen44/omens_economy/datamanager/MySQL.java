package omen44.omens_economy.datamanager;

import omen44.omens_economy.utils.SQLUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    FileConfiguration config = ConfigTools.getFileConfig("config.yml");

    private String host;
    private String port;
    private String database;
    private String username;
    private String password;

    private Connection connection;

    public boolean isConnected() {return (connection != null);}

    public void connect() throws ClassNotFoundException, SQLException {
        String pass = config.getString("database.password");
        if (pass.equalsIgnoreCase("blank")){
            password = "";
        }

        if (!isConnected()) {
            connection = DriverManager.getConnection("jdbc:mysql://"
                            + host + ":" + port + "/" + database + "?useSSL=false"
                    , username, password);
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