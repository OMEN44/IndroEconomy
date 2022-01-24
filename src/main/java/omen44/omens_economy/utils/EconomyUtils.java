package omen44.omens_economy.utils;

import omen44.omens_economy.datamanager.ConfigTools;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class EconomyUtils {
    ConfigTools configTools = new ConfigTools();
    FileConfiguration config = configTools.getConfig("config.yml");

    private final String host = config.getString("database.host");
    private final String port = config.getString("database.port");
    private final String database = config.getString("database.database");
    private final String username = config.getString("database.username");
    private final String password = config.getString("database.password");

    SQLUtils sqlUtils = new SQLUtils(database, host, port, username, password);

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

    // modifies wallet
    public void addWallet(Player player, int amount) {
        int wallet = getWallet(player);
        wallet += amount;
        setWallet(player, wallet);
    }

    public void minusWallet(Player player, int amount) {
        int wallet = getWallet(player);
        wallet -= amount;
        setWallet(player, wallet);
    }
    
    // modifies bank
    public void addBank(Player player, int amount) {
        int bank = getBank(player);
        bank += amount;
        setBank(player, bank);
    }

    public void minusBank(Player player, int amount) {
        int bank = getBank(player);
        bank -= amount;
        setBank(player, bank);
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
