package omen44.omens_economy.utils;

import omen44.omens_economy.Main;
import org.bukkit.entity.Player;

import java.util.Locale;

public class EconomyUtils {
    
    /* TODO: 13/10/2021 add functions:
    *   - transfer
    */

    public Main main;
    public EconomyUtils(Main main) {this.main = main;}
    ShortcutsUtils s = new ShortcutsUtils();

    public void setBank(Player player, int amount) {
        String uuid = player.getUniqueId().toString();
        String value = Integer.valueOf(amount).toString();
        main.sqlUtils.setData(value, "UUID", uuid, "bank", "players");
    }

    public void setWallet(Player player, int amount) {
        String uuid = player.getUniqueId().toString();
        String value = Integer.valueOf(amount).toString();
        main.sqlUtils.setData(value, "UUID", uuid, "wallet", "players");
    }

    public int getMoney(Player player, String column) {
        int amount = 0;
        String uuid = player.getUniqueId().toString();
        if (column.equalsIgnoreCase("bank")) {
            amount = main.sqlUtils.getInt("Bank", "UUID", uuid, "players");
        } else if (column.equalsIgnoreCase("wallet")) {
            amount = main.sqlUtils.getInt("Wallet", "UUID", uuid, "players");
        } else {
            System.out.println("Error: Invalid Column Input");
        }
        return amount;
    }

    public String sendMoney(Player from, Player target, int amount) {
        String sender = from.getUniqueId().toString();
        try {
            target.getUniqueId();
        } catch (Exception e) {
            return "Target not found";
        }
        String receiver = target.getUniqueId().toString();

        int senderWal = main.sqlUtils.getInt("Wallet", "UUID", sender, "players");
        int receiverWal = main.sqlUtils.getInt("Wallet", "UUID", receiver, "players");

        if (senderWal >= amount) {
            receiverWal += amount;
            senderWal -= amount;
            main.sqlUtils.setData(Integer.valueOf(receiverWal).toString(), "UUID", receiver, "Wallet", "players");
            main.sqlUtils.setData(Integer.valueOf(senderWal).toString(), "UUID", sender, "Wallet", "players");
            return "Successful";
        }
        return "Unsuccessful";
    }

    /**
     * @param player - The player who called the transfer
     * @param to - Where is the money going to?
     * @param amount - How much money is getting transferred?
     * @return true if the transfer was successful, false if the transfer was unsuccessful.
     */
    public void transferMoney(Player player, String to, int amount) {
        String target = player.getUniqueId().toString();
        int wallet = main.sqlUtils.getInt("Wallet", "UUID", target, "players");
        int bank = main.sqlUtils.getInt("Bank", "UUID", target, "players");

        switch (to.toLowerCase(Locale.ROOT)) {
            case "wallet" -> {
                if (wallet >= amount) {
                    bank += amount;
                    wallet -= amount;
                    main.sqlUtils.setData(Integer.valueOf(wallet).toString(), "UUID", target, "Wallet", "players");
                    main.sqlUtils.setData(Integer.valueOf(bank).toString(), "UUID", target, "Wallet", "players");
                    player.sendMessage(s.prefix + s.iMessage + "Transferred " + amount + " to wallet");
                } else {
                    player.sendMessage(s.prefix + s.error + "Error: Not Enough Money in Wallet to Transfer");
                }
            }
            case "bank" -> {
                if (bank >= amount) {
                    wallet += amount; // adds the wallet from the amount
                    bank -= amount; // takes the bank from the amount
                    main.sqlUtils.setData(Integer.valueOf(wallet).toString(), "UUID", target, "Wallet", "players");
                    main.sqlUtils.setData(Integer.valueOf(bank).toString(), "UUID", target, "Wallet", "players");
                    player.sendMessage(s.prefix + s.iMessage + "Transferred " + amount + " to bank");
                } else {
                    player.sendMessage(s.prefix + s.error + "Error: Not Enough Money in Bank to Transfer");
                }
            }
            default -> {
                player.sendMessage(s.prefix + s.error + "Error: Incorrect Money Type");
            }
        }
    }
}
