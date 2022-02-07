package io.github.omen44.indroEconomy.utils;

import io.github.omen44.indroEconomy.IndroEconomy;
import io.github.omen44.indroEconomy.datamanager.ConfigTools;
import io.github.omen44.indroEconomy.models.PlayerEconomyModel;
import io.github.omen44.indroEconomy.storage.EconomyStorageUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class EconomyUtils {
    int defaultMoney;
    int debtLimit;
    YamlUtils yamlUtils;

    public EconomyUtils() {
        ConfigTools configTools = new ConfigTools();
        FileConfiguration config = configTools.getConfig("config.yml");
        this.defaultMoney = config.getInt("money.defaultAmount");
        this.debtLimit = config.getInt("money.minimum");
        this.yamlUtils = new YamlUtils("banks");
    }

    public boolean createAccount(Player player) {
        UUID playerUUID = player.getUniqueId();
        PlayerEconomyModel account = EconomyStorageUtil.createAccount(playerUUID, 0, defaultMoney);
        String id = account.getId();

        return (EconomyStorageUtil.findAccount(id) != null);
    }

    public void setBank(Player player, int amount) {
        PlayerEconomyModel account = EconomyStorageUtil.findAccount(player.getUniqueId());
        if (account != null) {
            String accountID = account.getId();
            account.setBank(amount);
            EconomyStorageUtil.updateAccount(accountID, account);
        } else {
            createAccount(player);
        }
    }

    public void setWallet(Player player, int amount) {
        PlayerEconomyModel account = EconomyStorageUtil.findAccount(player.getUniqueId());
        if (account != null) {
            String accountID = account.getId();
            account.setWallet(amount);
            EconomyStorageUtil.updateAccount(accountID, account);
        } else {
            createAccount(player);
        }
        if (getWallet(player) <= debtLimit) {
            player.setMetadata("deathCausePoverty", new FixedMetadataValue(IndroEconomy.getInstance(), true));
            player.setHealth(0);
        }
    }

    public Integer getWallet(Player player) {
        PlayerEconomyModel account = EconomyStorageUtil.findAccount(player.getUniqueId());
        if (account != null) {
            return account.getWallet();
        }
        return null;
    }

    public Integer getBank(Player player) {
        PlayerEconomyModel account = EconomyStorageUtil.findAccount(player.getUniqueId());
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
        PlayerEconomyModel account = EconomyStorageUtil.findAccount(player.getUniqueId());
        return (account != null);
    }

    public void addWallet(Player player, int value) {
        setWallet(player, getWallet(player) + value);
    }

    public void minusWallet(Player player, int value) {
        setWallet(player, getWallet(player) - value);
        if (getWallet(player) <= debtLimit) {
            player.setMetadata("deathCausePoverty", new FixedMetadataValue(IndroEconomy.getInstance(), true));
            player.setHealth(0);
        }
    }


    // ignore for the moment
    
    public void createBank(String bankName, OfflinePlayer bankOwner) {
        FileConfiguration banks = yamlUtils.getConfig();
        banks.createSection(bankName);
        banks.set(bankName + ".balance", 0);
        banks.set(bankName + ".owner", bankOwner.getUniqueId());
        banks.createSection(bankName + ".members");
        yamlUtils.saveFile(banks);
    }

    
    public void deleteBank(String bankName) {
        FileConfiguration banks = yamlUtils.getConfig();
        banks.set(bankName, null);
        yamlUtils.saveFile(banks);
    }

    
    public Integer bankBalance(String bankName) {
        FileConfiguration banks = yamlUtils.getConfig();
        return banks.getInt(bankName + ".balance");
    }
    
    public boolean bankHas(String bankName, int value) {
        FileConfiguration banks = yamlUtils.getConfig();
        int bankBalance = banks.getInt(bankName + ".balance");
        return bankBalance >= value;
    }
    
    public int bankWithdraw(String bankName, int value) {
        FileConfiguration banks = yamlUtils.getConfig();
        int bankBalance = banks.getInt(bankName + ".balance");
        bankBalance -= value;
        banks.set(bankName + ".balance", bankBalance);
        return bankBalance;
    }
    
    public int bankDeposit(String bankName, int value) {
        FileConfiguration banks = yamlUtils.getConfig();
        int bankBalance = banks.getInt(bankName + ".balance");
        bankBalance += value;
        banks.set(bankName + ".balance", bankBalance);
        return bankBalance;
    }
    
    public boolean isBankOwner(String bankName, String playerName) {
        OfflinePlayer offlinePlayer = Bukkit.getServer().getPlayer(playerName);
        return isBankOwner(bankName, offlinePlayer);
    }
    
    public boolean isBankOwner(String bankName, OfflinePlayer bankOwner) {
        FileConfiguration banks = yamlUtils.getConfig();
        UUID banksString = (UUID) banks.get(bankName + ".owner");
        return banksString != null && banksString.equals(bankOwner.getUniqueId());
    }
    
    public boolean isBankMember(String bankName, String playerName) {
        OfflinePlayer offlinePlayer = Bukkit.getServer().getPlayer(playerName);
        return isBankMember(bankName, offlinePlayer);
    }
    
    public boolean isBankMember(String bankName, OfflinePlayer bankMember) {
        FileConfiguration banks = yamlUtils.getConfig();
        List<?> banksString = banks.getList(bankName + ".members");
        return banksString != null && banksString.contains(bankMember.getUniqueId().toString());
    }

    public List<String> getBanks() {
        FileConfiguration banks = yamlUtils.getConfig();
        List<String> bankList = new ArrayList<>();
        ConfigurationSection section = banks.getConfigurationSection("");
        if (section != null) {
            Map<String, Object> sectionValues = section.getValues(false);

            for (Object value : sectionValues.values()) {
                bankList.add(value.toString());
            }
        }
        return bankList;
    }
}
