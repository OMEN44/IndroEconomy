package omen44.omens_economy.datamanager;

import omen44.omens_economy.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class MySQL {
    FileConfiguration config = ConfigTools.getFileConfig("config.yml");

    private final String host = config.getString("database.host");
    private final String port = config.getString("database.port");
    private final String database = config.getString("database.database");
    private final String username = config.getString("database.user");
    private final String password = config.getString("database.password");
    protected String pass;

    private Connection connection;

    public void connectDB() throws ClassNotFoundException, SQLException {
        if (password.equals("blank")){
            pass = "";
        }

        if (!isConnected()) {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSl=false", username, pass);
        }
    }

    public void disconnectDB() {
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
    public boolean isConnected() {
        return (connection != null);
    }
}

