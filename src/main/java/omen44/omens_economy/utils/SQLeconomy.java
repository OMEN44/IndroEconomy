package omen44.omens_economy.utils;

import omen44.omens_economy.datamanager.ConfigTools;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SQLeconomy {
    SQLUtils sqlUtils;

    public SQLeconomy() {
        ConfigTools configTools = new ConfigTools();
        FileConfiguration config = configTools.getConfig("config.yml");

        final String host = config.getString("database.host");
        final String port = config.getString("database.port");
        final String database = config.getString("database.database");
        final String username = config.getString("database.username");
        final String password = config.getString("database.password");

        this.sqlUtils = new SQLUtils(database, host, port, username, password);
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
        return sqlUtils.getInt("wallet", "UUID", uuid, "economy");
    }

    public int getBank(Player player) {
        String uuid = player.getUniqueId().toString();
        return sqlUtils.getInt("bank", "UUID", uuid, "economy");
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

    public boolean transferMoney(Player source, String type, int value) {
        if (type.equalsIgnoreCase("wallet")) { // from wallet to bank
            if (value >= getWallet(source)) {
                int wallet = getWallet(source);
                int bank = getBank(source);

                wallet -= value;
                bank += value;

                setWallet(source, wallet);
                setBank(source, bank);
                return true;
            } else {
                return false;
            }
        } else if (type.equalsIgnoreCase("bank")) { // from bank to wallet
            if (value >= getBank(source)) {
                int wallet = getWallet(source);
                int bank = getBank(source);

                bank -= value;
                wallet += value;

                setWallet(source, wallet);
                setBank(source, bank);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void addWallet(Player player, int value) {
        setWallet(player, getWallet(player) + value);
    }

    public void minusWallet(Player player, int value) {
        setWallet(player, getWallet(player) - value);
    }

    public void addBank(Player player, int value) {
        setBank(player, getBank(player) + value);
    }

    public void minusBank(Player player, int value) {
        setBank(player, getBank(player) - value);
    }
}
