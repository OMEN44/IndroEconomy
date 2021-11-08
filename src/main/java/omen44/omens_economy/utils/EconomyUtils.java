package omen44.omens_economy.utils;

import omen44.omens_economy.Main;
import org.bukkit.entity.Player;

public class EconomyUtils {
    
    /* TODO: 13/10/2021 add functions:
    *   - transfer
    */

    public Main main;
    public EconomyUtils(Main main) {this.main = main;}

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
            amount += receiverWal;
            main.sqlUtils.setData(Integer.valueOf(amount).toString(), "UUID", receiver, "Wallet", "players");
            return "Successful";
        }

        return "Unsuccessful";
    }
}
