package omen44.omens_economy.utils;

import omen44.omens_economy.datamanager.MySQL;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUtils {
    MySQL mySQL = new MySQL();
    final Connection connection = mySQL.getConnection();

    /**
     * Sets the data declared into the MySQL database.
     * @param value The data that needs to be set
     * @param columnID The name of the column changed
     * @param equalsID The name of the item compared to
     * @param column The column of the data inserted
     * @param tableName The name of the table that the data is being set to
     */
    public void setData(String value, String columnID, String equalsID, String column, String tableName) {
        try {

            PreparedStatement ps = connection.prepareStatement("UPDATE " + tableName + " SET " + column + "=? WHERE " + columnID + "=?");
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
     * Gets a variable of type 'String' from the MySQL database.
     * @param columnName The name of the column searched
     * @param columnID The name of the column compared to
     * @param equalsID The name of the item compared to
     * @param tableName The name of the database table checked
     * @return The value of data type 'String'
     */
    public String getDBString(String columnName, String columnID, String equalsID, String tableName) {
        try {
            String info;
            PreparedStatement ps = connection.prepareStatement("SELECT " + columnName + " FROM " + tableName + " WHERE " + columnID + "=?");
            ps.setString(1, equalsID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                info = rs.getString(columnName);
                return info;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Gets a variable of type 'int' from the MySQL database
     * @param columnName The name of the column searched.
     * @param columnID The name of the column compared to.
     * @param equalsID The name of the item compared to.
     * @param tableName The name of the database table checked.
     * @return The value of data type 'int'.
     */
    public int getDBInt(String columnName, String columnID, String equalsID, String tableName) {
        try {
            int info;
            PreparedStatement ps = connection.prepareStatement("SELECT " + columnName + " FROM " + tableName + " WHERE " + columnID + "=?");
            ps.setString(1, equalsID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                info = rs.getInt(columnName);
                return info;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Gets a variable of type 'float' from the MySQL database.
     * @param columnName The name of the column searched
     * @param columnID The name of the column compared to
     * @param equalsID The name of the item compared to
     * @param tableName The name of the database table checked
     * @return The value of data type 'float'
     */
    public float getDBFloat(String columnName, String columnID, String equalsID, String tableName) {
        try {
            float info;
            PreparedStatement ps = connection.prepareStatement("SELECT " + columnName + " FROM " + tableName + " WHERE " + columnID + "=?");
            ps.setString(1, equalsID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                info = rs.getFloat(columnName);
                return info;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Gets a variable of type 'float' from the MySQL database.
     * @param columnName The name of the column searched
     * @param columnID The name of the column compared to
     * @param equalsID The name of the item compared to
     * @param tableName The name of the database table checked
     * @return The value of data type 'float'
     */
    public double getDBDouble(String columnName, String columnID, String equalsID, String tableName) {
        try {
            double info;
            PreparedStatement ps = connection.prepareStatement("SELECT " + columnName + " FROM " + tableName + " WHERE " + columnID + "=?");
            ps.setString(1, equalsID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                info = rs.getDouble(columnName);
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
     * @param name The name of the Table
     * @param columnID The name of the first column of the database (usually "accountID")
     */

    public void createDBTable(String name, String columnID) {
        try {
            System.out.println(connection);
            PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + name + " (" + columnID + " VARCHAR(100), PRIMARY KEY" + " (" + columnID + "));");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new column of string 'columnName' if it doesn't exist.
     * Appends the column to the table.
     * @param columnName The name of the column added
     * @param dataType The data type the column uses (i.e. VarChar(100))
     * @param tableName The name of the table the column is getting added to (must exist)
     */
    public void createDBColumn(String columnName, String dataType, String tableName) {
        try {
            PreparedStatement ps = connection.prepareStatement("ALTER TABLE " + tableName + " ADD COLUMN IF NOT EXISTS " + columnName + " " + dataType + ";");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new row of string 'rowName' and adds an ID to the first column.
     * Note: this does not check if the row exists, handled by {@see rowExists}
     * @param columnID The name of the first column (usually accountID)
     * @param accountID The ID of the account added (should already be handled)
     * @param tableName The name of the table the column is getting added to (must exist)
     */
    public void createRow(String columnID, String accountID, String tableName) {
        if (!rowExists(columnID, accountID, tableName)) { // checks if the row does not already exist
            try {
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + tableName);
                ResultSet results = ps.executeQuery();
                results.next();
                PreparedStatement ps2 = connection.prepareStatement("INSERT IGNORE INTO " + tableName + " (" + columnID + ") VALUE (?)");
                ps2.setString(1, accountID);
                ps2.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if a row already exists. (usually to test for if an ID already exists)
     * @param columnID The first column of the row (usually accountID)
     * @param comparedValue The value that it's comparing to
     * @param tableName The table that the program is checking
     * @return true if exists, false if it doesn't
     */
    public boolean rowExists(String columnID, String comparedValue, String tableName) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE " + columnID + "=?");
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
     * Sets the data type of the column.
     * @param column The name of the column altered
     * @param dataType The data type converted to
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
     * @param columnID The name of the column to compare to
     * @param equalsID The name of the item compared to
     * @param tableName The name of the table to remove from
     */
    public void remove(String columnID, String equalsID, String tableName) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM " + tableName + " WHERE " + columnID + "=?");
            ps.setString(1, equalsID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A calculation to determine what data type a number is.
     * @param type The data type it's comparing to.
     * @param num The string of the number it's comparing to.
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
     * Creates the accountID row if it does not exist.
     */

    public void createIDTable(String tableName, String firstColumn, String dataType) {
        try {
            PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName
                    + " (accountID MEDIUMINT NOT NULL AUTO_INCREMENT,"
                    + firstColumn + " " + dataType
                    + " PRIMARY KEY (accountID))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
