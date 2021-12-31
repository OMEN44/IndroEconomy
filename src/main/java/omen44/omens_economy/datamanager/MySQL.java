package omen44.omens_economy.datamanager;

import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    ConfigTools configTools = new ConfigTools();
    FileConfiguration config = configTools.getConfig("config.yml");

    private final String host = config.getString("database.host");
    private final String port = config.getString("database.port");
    private final String database = config.getString("database.database");
    private final String username = config.getString("database.username");
    private final String password = config.getString("database.password");

    Connection connection;
    public Connection getConnection() {
        connection = null;
        String pass = "";
        if (!password.equalsIgnoreCase("blank")) {
            pass = password;
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://"
                            + host + ":" + port + "/" + database + "?useSSL=false"
                    , username, pass);
        } catch (SQLException e) {
            printSQLException(e);
        }
        return connection;
    }

    public static void closeConnection(Connection connArg) {
        System.out.println("Closing the Database...");
        try {
            connArg.close();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException)e).getSQLState());
                System.err.println("Error Code: " + ((SQLException)e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}