package omen44.omens_economy.datamanager;

import omen44.omens_economy.utils.SQLUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    FileConfiguration config = ConfigTools.getFileConfig("config.yml");

    private final String host = config.getString("database.host");
    private final String port = config.getString("database.port");
    private final String database = config.getString("database.database");
    private final String username = config.getString("database.user");
    String pass = config.getString("database.password");
    private String password = pass;

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