package omen44.omens_economy.utils;

import omen44.omens_economy.datamanager.MySQL;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.Locale;

public class EconomyUtils {


    /* TODO: 13/10/2021 add functions:
    *   - transfer
    */
    MySQL mySQL = new MySQL();
    Connection connection = mySQL.getConnection();
    SQLUtils sqlUtils = new SQLUtils(connection);
    ShortcutsUtils s = new ShortcutsUtils();

    public void setBank(Player player, int amount) {
        String uuid = player.getUniqueId().toString();
        String value = Integer.valueOf(amount).toString();
        sqlUtils.setData(value, "UUID", uuid, "bank", "economy");
    }

    public void setWallet(Player player, int amount) {
        String uuid = player.getUniqueId().toString();
        String value = Integer.valueOf(amount).toString();
        sqlUtils.setData(value, "UUID", uuid, "wallet", "economy");
    }

    public int getMoney(Player player, String column) {
        int amount = 0;
        String uuid = player.getUniqueId().toString();
        if (column.equalsIgnoreCase("bank")) {
            amount = sqlUtils.getDBInt("bank", "UUID", uuid, "economy");
        } else if (column.equalsIgnoreCase("wallet")) {
            amount = sqlUtils.getDBInt("wallet", "UUID", uuid, "economy");
        } else {
            System.out.println("Error: Invalid Column Input");
        }
        return amount;
    }

    public String sendMoney(Player from, Player to, int amount) {
        String sender = from.getUniqueId().toString();
        try {
            to.getUniqueId();
        } catch (Exception e) {
            return "Target not found";
        }
        String receiver = to.getUniqueId().toString();

        int senderWal = sqlUtils.getDBInt("wallet", "UUID", sender, "economy");
        int receiverWal = sqlUtils.getDBInt("wallet", "UUID", receiver, "economy");

        if (senderWal >= amount) {
            receiverWal += amount;
            senderWal -= amount;
            sqlUtils.setData(Integer.valueOf(receiverWal).toString(),
                    "UUID", receiver, "wallet", "economy");
            sqlUtils.setData(Integer.valueOf(senderWal).toString(),
                    "UUID", sender, "wallet", "economy");
            return "Successful";
        }
        return "Unsuccessful";
    }

    /**
     * Note: does not handle messages
     * @param player - The player who called the transfer
     * @param to - Where is the money going to?
     * @param amount - How much money is getting transferred?
     * @return true if the transfer was successful, false if it wasn't
     */
    public boolean transferMoney(Player player, String to, int amount) {
        String target = player.getUniqueId().toString();
        int wallet = sqlUtils.getDBInt("wallet", "UUID", target, "economy");
        int bank = sqlUtils.getDBInt("bank", "UUID", target, "economy");

        switch (to.toLowerCase(Locale.ROOT)) {
            case "wallet" -> {
                if (wallet >= amount) {
                    bank += amount;
                    wallet -= amount;
                    sqlUtils.setData(Integer.valueOf(wallet).toString(), "UUID", target, "wallet", "economy");
                    sqlUtils.setData(Integer.valueOf(bank).toString(), "UUID", target, "wallet", "economy");
                    return true;
                } else {
                    return false;
                }
            }
            case "bank" -> {
                if (bank >= amount) {
                    wallet += amount; // adds the wallet from the amount
                    bank -= amount; // takes the bank from the amount
                    sqlUtils.setData(Integer.valueOf(wallet).toString(), "UUID", target, "wallet", "economy");
                    sqlUtils.setData(Integer.valueOf(bank).toString(), "UUID", target, "wallet", "economy");
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
}
