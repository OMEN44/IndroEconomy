package omen44.omens_economy.utils;

import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.datamanager.MySQL;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.Connection;

public class EconomyUtils {
    MySQL mySQL = new MySQL();
    Connection connection = mySQL.getConnection();
    SQLUtils sqlUtils = new SQLUtils(connection);
    ConfigTools configTools = new ConfigTools();
    FileConfiguration config = configTools.getConfig("config.yml");

    public void createPlayerAccount(Player player) {
        sqlUtils.createRow("UUID", player.getUniqueId().toString(), "economy");
        setWallet(player, 0);
        setBank(player, config.getInt("defaultAmount"));
    }

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

    public int getWallet(Player player) {
        String uuid = player.getUniqueId().toString();
        return sqlUtils.getDBInt("wallet", "UUID", uuid, "economy");
    }

    public int getBank(Player player) {
        String uuid = player.getUniqueId().toString();
        return sqlUtils.getDBInt("bank", "UUID", uuid, "economy");
    }

    public boolean sendMoney(Player sender, Player receiver, int amount) {
        int senderWal = getWallet(sender);
        int receiverWal = getWallet(receiver);

        if (senderWal >= amount) {
            receiverWal += amount;
            senderWal -= amount;
            setWallet(sender, senderWal);
            setWallet(receiver, receiverWal);
            return true;
        }
        return false;
    }

    public boolean depositPlayer(Player source, int amount) {
        int wallet = getWallet(source);
        int bank = getBank(source);

        if (wallet >= amount) {
            wallet -= amount;
            bank += amount;
            setWallet(source, wallet);
            setBank(source, bank);
            return true;
        }
        return false;
    }
    public boolean withdrawPlayer(Player source, int amount) {
        int wallet = getWallet(source);
        int bank = getBank(source);

        if (bank >= amount) {
            wallet += amount;
            bank -= amount;
            setWallet(source, wallet);
            setBank(source, bank);
            return true;
        }
        return false;
    }
}
