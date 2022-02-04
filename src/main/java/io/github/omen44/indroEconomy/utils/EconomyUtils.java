package io.github.omen44.indroEconomy.utils;

import io.github.omen44.indroEconomy.IndroEconomy;
import io.github.omen44.indroEconomy.datamanager.ConfigTools;
import io.github.omen44.indroEconomy.models.EconomyModel;
import io.github.omen44.indroEconomy.storage.EconomyStorageUtil;
import io.github.omen44.indroEconomy.utils.sqlite.Database;
import io.github.omen44.indroEconomy.utils.sqlite.SQLite;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class EconomyUtils {
    SQLUtils sqlUtils;
    Database db;
    String dataType;
    int defaultMoney;

    public EconomyUtils() {
        ConfigTools configTools = new ConfigTools();
        FileConfiguration config = configTools.getConfig("config.yml");

        this.defaultMoney = config.getInt("money.defaultAmount");
        this.dataType = config.getString("saveData");

        if (dataType == null) {
            Bukkit.getLogger().severe("Data Type not Specified, Assuming JSON!");
            this.dataType = "json";
        } else if (compare(dataType, "mysql")) {
            final String host = config.getString("database.host");
            final String port = config.getString("database.port");
            final String database = config.getString("database.database");
            final String username = config.getString("database.username");
            final String password = config.getString("database.password");
            this.sqlUtils = new SQLUtils(database, host, port, username, password);
        } else if (compare(dataType, "sqlite")) {
            this.db = new SQLite(IndroEconomy.getInstance());
            this.db.load();
        }
    }

    public boolean createAccount(Player player) {
        String playerUUID = player.getUniqueId().toString();
        if (compare(this.dataType, "mysql")) {
            sqlUtils.createRow("UUID", playerUUID, "economy");
            setWallet(player, defaultMoney);
            setBank(player, 0);

            return sqlUtils.getString("UUID", "UUID", player.getUniqueId().toString(), "economy") != null;
        } else if (compare(this.dataType, "json")) {
            EconomyModel account = EconomyStorageUtil.createAccount(player, 0, defaultMoney);
            String id = account.getId();

            return (EconomyStorageUtil.findAccount(id) != null);
        } else if (compare(dataType, "sqlite")) {
            this.db.setAccount(player, defaultMoney, 0);
            return (this.db.getBank(playerUUID) != null);
        }
        return false;
    }

    public void setBank(Player player, int amount) {
        String uuid = player.getUniqueId().toString();
        String value = Integer.valueOf(amount).toString();
        if (compare(this.dataType, "mysql")) {
            sqlUtils.setData(value, "UUID", uuid, "bank", "economy");
        } else if (compare(this.dataType, "json")) {
            EconomyModel account = EconomyStorageUtil.findAccount(player.getUniqueId());
            if (account != null) {
                account.setBank(amount);
            } else {
                createAccount(player);
            }
        } else if (compare(dataType, "sqlite")) {
            int count = this.db.getWallet(player.getUniqueId().toString());
            this.db.setAccount(player, count, amount);
        }
    }

    public void setWallet(Player player, int amount) {
        String uuid = player.getUniqueId().toString();
        String value = Integer.valueOf(amount).toString();
        if (compare(this.dataType, "mysql")) {
            sqlUtils.setData(value, "UUID", uuid, "wallet", "economy");
        } else if (compare(this.dataType, "json")) {
            EconomyModel account = EconomyStorageUtil.findAccount(player.getUniqueId());
            if (account != null) {
                account.setWallet(amount);
            } else {
                createAccount(player);
            }
        } else if (compare(dataType, "sqlite")) {
            int count = this.db.getBank(player.getUniqueId().toString());
            this.db.setAccount(player, amount, count);
        }
    }

    public Integer getWallet(Player player) {
        String uuid = player.getUniqueId().toString();
        if (compare(this.dataType, "mysql")) {
            return sqlUtils.getInt("wallet", "UUID", uuid, "economy");
        } else if (compare(this.dataType, "json")){
            EconomyModel account = EconomyStorageUtil.findAccount(player.getUniqueId());
            if (account != null) {
                return account.getWallet();
            } else {
                createAccount(player);
            }
        } else if (compare(dataType, "sqlite")) {
            return this.db.getWallet(player.getUniqueId().toString());
        }
        return null;
    }

    public Integer getBank(Player player) {
        String uuid = player.getUniqueId().toString();
        if (compare(this.dataType, "mysql")) {
            return sqlUtils.getInt("bank", "UUID", uuid, "economy");
        } else if (compare(this.dataType, "json")){
            EconomyModel account = EconomyStorageUtil.findAccount(player.getUniqueId());
            if (account != null) {
                return account.getBank();
            } else {
                createAccount(player);
            }
        } else if (compare(dataType, "sqlite")) {
            return this.db.getBank(player.getUniqueId().toString());
        }
        return null;
    }

    public boolean sendMoney(Player sender, Player receiver, int amount) {
        int senderWal;
        int receiverWal;
        try {
            senderWal = getWallet(sender);
            receiverWal = getWallet(receiver);
        } catch (NullPointerException e) {
            return false;
        }
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
            if (value <= getWallet(source)) {
                int wallet;
                int bank;
                try {
                    wallet = getWallet(source);
                    bank = getBank(source);
                } catch (NullPointerException e) {
                    source.sendMessage("Your account doesn't exist, contact an admin to get it fixed");
                    return false;
                }
                wallet -= value;
                bank += value;

                setWallet(source, wallet);
                setBank(source, bank);
                return true;
            } else {
                return false;
            }
        } else if (type.equalsIgnoreCase("bank")) { // from bank to wallet
            if (value <= getBank(source)) {
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


    public boolean hasAccount(Player player) {
        String uuid = player.getUniqueId().toString();
        if (compare(this.dataType, "mysql")) {
            return sqlUtils.rowExists("wallet", uuid, "economy");
        } else if (compare(this.dataType, "json")){
            EconomyModel account = EconomyStorageUtil.findAccount(player.getUniqueId());
            return (account != null);
        } else if (compare(dataType, "sqlite")) {
            return (this.db.getWallet(player.getUniqueId().toString()) != null);
        }
        return false;
    }

    public List<String> getAccountList(String filter) {
        ArrayList<String> list = new ArrayList<>(sqlUtils.getEntireColumn(filter, "economy"));
        if (!(filter.equalsIgnoreCase("playernames"))) {
            return list;
        } else {
            ArrayList<String> playerNames = new ArrayList<>();
            for (String item : list) {
                UUID uuid = UUID.fromString(item);
                playerNames.add(Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName());
            }
            return playerNames;
        }
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
    
    private boolean compare(String compare, String compared) {
        return compare.equalsIgnoreCase(compared);
    }
}
