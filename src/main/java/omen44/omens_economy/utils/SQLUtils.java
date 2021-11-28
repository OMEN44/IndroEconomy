package omen44.omens_economy.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUtils {
    private final Connection connection;

    public SQLUtils(Connection connection) {
        this.connection = connection;
    }

    /**
     * Sets the data declared into the database.
     *
     * @param value          The data that needs to be set
     * @param searchedColumn The name of the column changed
     * @param equalsID       The name of the item compared to
     * @param column         The column of the data inserted
     * @param tableName      The name of the table that the data is being set to
     */
    public void setData(String value, String searchedColumn, String equalsID, String column, String tableName) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE " + tableName + " SET " + column + "=? WHERE " + searchedColumn + "=?");
            if (isNumType("int", value)) {
                int valNum = Integer.parseInt(value);
                ps.setInt(1, valNum);
                ps.setString(2, equalsID);
                Bukkit.getLogger().warning("int");
            } else if (isNumType("float", value)) {
                float valNum = Float.parseFloat(value);
                ps.setFloat(1, valNum);
                ps.setString(2, equalsID);
                Bukkit.getLogger().warning("float");
            } else if (isNumType("double", value)) {
                double valNum = Double.parseDouble(value);
                ps.setDouble(1, valNum);
                ps.setString(2, equalsID);
                Bukkit.getLogger().warning("double");
            } else {
                ps.setString(1, value);
                ps.setString(2, equalsID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a variable of type 'String' from the mySQL.getConnection() database.
     *
     * @param selectedColumn The name of the column searched
     * @param searchedColumn The name of the column compared to
     * @param equalsID       The name of the item compared to
     * @param tableName      The name of the database table checked
     * @return The value of data type 'String'
     */
    public String getDBString(String selectedColumn, String searchedColumn, String equalsID, String tableName) {
        try {
            String info;
            PreparedStatement ps = connection.prepareStatement("SELECT " + selectedColumn + " FROM " + tableName + " WHERE " + searchedColumn + "=?");
            ps.setString(1, equalsID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                info = rs.getString(selectedColumn);
                return info;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Gets a variable of type 'int' from the mySQL.getConnection() database
     *
     * @param selectedColumn The name of the column searched.
     * @param searchedColumn The name of the column compared to.
     * @param equalsID       The name of the item compared to.
     * @param tableName      The name of the database table checked.
     * @return The value of data type 'int'.
     */
    public int getDBInt(String selectedColumn, String searchedColumn, String equalsID, String tableName) {
        try {
            int info;
            PreparedStatement ps = connection.prepareStatement("SELECT " + selectedColumn + " FROM " + tableName + " WHERE " + searchedColumn + "=?");
            ps.setString(1, equalsID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                info = rs.getInt(selectedColumn);
                return info;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Gets a variable of type 'float' from the mySQL.getConnection() database.
     *
     * @param selectedColumn The name of the column searched
     * @param searchedColumn The name of the column compared to
     * @param equalsID       The name of the item compared to
     * @param tableName      The name of the database table checked
     * @return The value of data type 'float'
     */
    public float getDBFloat(String selectedColumn, String searchedColumn, String equalsID, String tableName) {
        try {
            float info;
            PreparedStatement ps = connection.prepareStatement("SELECT " + selectedColumn + " FROM " + tableName + " WHERE " + searchedColumn + "=?");
            ps.setString(1, equalsID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                info = rs.getFloat(selectedColumn);
                return info;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Gets a variable of type 'float' from the mySQL.getConnection() database.
     *
     * @param selectedColumn The name of the column searched
     * @param searchedColumn The name of the column compared to
     * @param equalsID       The name of the item compared to
     * @param tableName      The name of the database table checked
     * @return The value of data type 'float'
     */
    public double getDBDouble(String selectedColumn, String searchedColumn, String equalsID, String tableName) {
        try {
            double info;
            PreparedStatement ps = connection.prepareStatement("SELECT " + selectedColumn + " FROM " + tableName + " WHERE " + searchedColumn + "=?");
            ps.setString(1, equalsID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                info = rs.getDouble(selectedColumn);
                return info;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }


    // past here is where the creation is done

    /**
     * Creates the table of the String 'name' if it does not exist.
     *
     * @param name           The name of the Table
     * @param searchedColumn The name of the first column of the database (usually "comparedValue")
     */
    public void createDBTable(String name, String searchedColumn) {
        try {
            PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + name + " (" + searchedColumn + " VARCHAR(100), PRIMARY KEY" + " (" + searchedColumn + "));");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new column of string 'selectedColumn' if it doesn't exist.
     * Appends the column to the table.
     *
     * @param selectedColumn The name of the column added
     * @param dataType       The data type the column uses (i.e. VarChar(100))
     * @param tableName      The name of the table the column is getting added to (must exist)
     */
    public void createDBColumn(String selectedColumn, String dataType, String tableName) {
        try {
            PreparedStatement ps = connection.prepareStatement("ALTER TABLE " + tableName + " ADD COLUMN IF NOT EXISTS " + selectedColumn + " " + dataType + ";");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a row already exists. (usually to test for if an ID already exists)
     *
     * @param searchedColumn The first column of the row (usually comparedValue)
     * @param comparedValue  The value that it's comparing to
     * @param tableName      The table that the program is checking
     * @return true if exists, false if it doesn't
     */
    public boolean rowExists(String searchedColumn, String comparedValue, String tableName) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE " + searchedColumn + "=?");
            ps.setString(1, comparedValue);
            ResultSet results = ps.executeQuery();
            //row is found
            return results.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Creates a new row of string 'rowName' and adds an ID to the first column.
     * Note: this does not check if the row exists, handled by {@see rowExists}
     *
     * @param searchedColumn The name of the first column (usually comparedValue)
     * @param comparedValue  The ID of the account added (should already be handled)
     * @param tableName      The name of the table the column is getting added to (must exist)
     */
    public void createRow(String searchedColumn, String comparedValue, String tableName) {
        if (!rowExists(searchedColumn, comparedValue, tableName)) { // checks if the row does not already exist
            try {
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + tableName);
                ResultSet results = ps.executeQuery();
                results.next();
                PreparedStatement ps2 = connection.prepareStatement("INSERT IGNORE INTO " + tableName + " (" + searchedColumn + ") VALUE (?)");
                ps2.setString(1, comparedValue);
                ps2.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets the data type of the column.
     *
     * @param column    The name of the column altered
     * @param dataType  The data type converted to
     * @param tableName The name of the table adjusting
     */
    public void setDataType(String column, String dataType, String tableName) {
        try {
            PreparedStatement ps = connection.prepareStatement("ALTER TABLE " + tableName + " MODIFY " + column + " " + dataType);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a row from the table.
     *
     * @param searchedColumn The name of the column to compare to
     * @param equalsID       The name of the item compared to
     * @param tableName      The name of the table to remove from
     */
    public void remove(String searchedColumn, String equalsID, String tableName) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM " + tableName + " WHERE " + searchedColumn + "=?");
            ps.setString(1, equalsID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A calculation to determine what data type a number is.
     *
     * @param type The data type it's comparing to.
     * @param num  The string of the number it's comparing to.
     * @return true if it's a valid number, false if it is anything else.
     */
    public boolean isNumType(String type, String num) {
        try {
            if (type.equalsIgnoreCase("int")) {
                int i = Integer.parseInt(num);
                return num.equals(String.valueOf(i));
            } else if (type.equalsIgnoreCase("float")) {
                float i = Float.parseFloat(num);
                return num.equals(String.valueOf(i));
            } else if (type.equalsIgnoreCase("double")) {
                double i = Double.parseDouble(num);
                return num.equals(String.valueOf(i));
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /**
     * Creates the account table.
     */
    public void createAccountTable() {
        try {
            PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS accounts (comparedValue MEDIUMINT NOT NULL AUTO_INCREMENT, PRIMARY KEY (comparedValue));");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPlayer(Player playerUsername) {
        String uuid = playerUsername.getUniqueId().toString();
        System.out.println(uuid);
        createRow("UUID", uuid, "economy");
        createRow("UUID", uuid, "shops");
    }
}


