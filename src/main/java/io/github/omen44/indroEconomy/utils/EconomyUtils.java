package io.github.omen44.indroEconomy.utils;

import io.github.omen44.indroEconomy.IndroEconomy;
import io.github.omen44.indroEconomy.datamanager.ConfigTools;
import io.github.omen44.indroEconomy.models.EconomyModel;
import io.github.omen44.indroEconomy.storage.EconomyStorageUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class EconomyUtils {
    int defaultMoney;

    public EconomyUtils() {
        ConfigTools configTools = new ConfigTools();
        FileConfiguration config = configTools.getConfig("config.yml");
        this.defaultMoney = config.getInt("money.defaultAmount");
    }

    public boolean createAccount(Player player) {
        EconomyModel account = EconomyStorageUtil.createAccount(player, 0, defaultMoney);
        String id = account.getId();

        return (EconomyStorageUtil.findAccount(id) != null);
    }

    public void setBank(Player player, int amount) {
        EconomyModel account = EconomyStorageUtil.findAccount(player.getUniqueId());
        if (account != null) {
            String accountID = account.getId();
            account.setBank(amount);
            EconomyStorageUtil.updateAccount(accountID, account);
        } else {
            createAccount(player);
        }
    }

    public void setWallet(Player player, int amount) {
        EconomyModel account = EconomyStorageUtil.findAccount(player.getUniqueId());
        if (account != null) {
            String accountID = account.getId();
            account.setWallet(amount);
            EconomyStorageUtil.updateAccount(accountID, account);
        } else {
            createAccount(player);
        }
        if (getWallet(player) <= -400) {
            player.setMetadata("deathCausePoverty", new FixedMetadataValue(IndroEconomy.getInstance(), true));
            player.setHealth(0);
        }
    }

    public Integer getWallet(Player player) {
        EconomyModel account = EconomyStorageUtil.findAccount(player.getUniqueId());
        if (account != null) {
            return account.getWallet();
        }
        return null;
    }

    public Integer getBank(Player player) {
        EconomyModel account = EconomyStorageUtil.findAccount(player.getUniqueId());
        if (account != null) {
            return account.getBank();
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
        if (type.equalsIgnoreCase("deposit")) { // from wallet to bank
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
        } else if (type.equalsIgnoreCase("withdraw")) { // from bank to wallet
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
        EconomyModel account = EconomyStorageUtil.findAccount(player.getUniqueId());
        return (account != null);
    }

    public void addWallet(Player player, int value) {
        setWallet(player, getWallet(player) + value);
    }

    public void minusWallet(Player player, int value) {
        setWallet(player, getWallet(player) - value);
        if (getWallet(player) <= -2140000) {
            player.setMetadata("deathCausePoverty", new FixedMetadataValue(IndroEconomy.getInstance(), true));
            player.setHealth(0);
        }
    }
}
