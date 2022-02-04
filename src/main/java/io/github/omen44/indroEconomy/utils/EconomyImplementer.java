package io.github.omen44.indroEconomy.utils;

import io.github.omen44.indroEconomy.IndroEconomy;
import io.github.omen44.indroEconomy.datamanager.ConfigTools;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class EconomyImplementer implements Economy {
    EconomyUtils eco = new EconomyUtils();

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return IndroEconomy.getInstance().getName();
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public String format(double value) {
        int money = ((int) value);
        return String.valueOf(money);
    }

    @Override
    public String currencyNamePlural() {
        ConfigTools configTools = new ConfigTools();
        FileConfiguration config = configTools.getConfig("config.yml");
        return config.getString("money.moneySymbol");
    }

    @Override
    public String currencyNameSingular() {
        return currencyNamePlural();
    }

    @Override
    public boolean hasAccount(String playerName) {
        OfflinePlayer player = Bukkit.getPlayer(playerName);
        if (player != null) {
            return hasAccount(player);
        }
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        if (offlinePlayer.getPlayer() != null) {
            return eco.hasAccount(offlinePlayer.getPlayer());
        } else {
            return false;
        }
    }

    @Override
    public boolean hasAccount(String playerName, String world) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String world) {
        return hasAccount(offlinePlayer);
    }

    @Override
    public double getBalance(String playerName) {
        OfflinePlayer player = Bukkit.getPlayer(playerName);
        if (player != null) {
            getBalance(player);
        }
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        if (offlinePlayer.getPlayer() != null) {
            return eco.getWallet(offlinePlayer.getPlayer());
        }
        return 0;
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String playerName) {
        return getBalance(offlinePlayer);
    }

    @Override
    public boolean has(String playerName, double value) {
        OfflinePlayer player = Bukkit.getPlayer(playerName);
        if (player != null) {
            return has(player, value);
        }
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double value) {
        if (offlinePlayer.getPlayer() != null) {
            Player player = offlinePlayer.getPlayer();
            int wallet = eco.getWallet(player);
            return ((int) value) <= wallet;
        }
        return false;
    }

    @Override
    public boolean has(String playerName, String world, double value) {
        return has(playerName, value);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String playerName, double value) {
        return has(offlinePlayer, value);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double value) {
        OfflinePlayer player = Bukkit.getPlayer(playerName);
        if (player != null) {
            return withdrawPlayer(player, value);
        }
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double value) {
        if (offlinePlayer.getPlayer() != null) {
            Player player = offlinePlayer.getPlayer();
            eco.minusWallet(player, (int) value);
            int wallet = eco.getWallet(player);

            return new EconomyResponse(value, wallet, EconomyResponse.ResponseType.SUCCESS, "Unknown Error!");
        }
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String world, double value) {
        return withdrawPlayer(playerName, value);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String world, double value) {
        return withdrawPlayer(offlinePlayer, value);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double value) {
        OfflinePlayer player = Bukkit.getPlayer(playerName);
        if (player != null) {
            return depositPlayer(player, value);
        }
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double value) {
        if (offlinePlayer.getPlayer() != null) {
            Player player = offlinePlayer.getPlayer();
            eco.addWallet(player, (int) value);
            int wallet = eco.getWallet(player);

            return new EconomyResponse(value, wallet, EconomyResponse.ResponseType.SUCCESS, "Unknown Error!");
        }
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String world, double value) {
        return depositPlayer(playerName, value);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String playerName, double value) {
        return depositPlayer(offlinePlayer, value);
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        OfflinePlayer player = Bukkit.getPlayer(playerName);
        if (player != null) {
            return createPlayerAccount(player);
        }
        return false;
    }
    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        if (offlinePlayer.getPlayer() != null) {
            return eco.createAccount(offlinePlayer.getPlayer());
        }
        return false;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String world) {
        return createPlayerAccount(playerName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String world) {
        return createPlayerAccount(offlinePlayer);
    }

    // ignore for the moment
    @Override
    public EconomyResponse createBank(String playerName, String world) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String playerName, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String playerName) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String playerName) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String playerName, double value) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String playerName, double value) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String playerName, double value) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String playerName, String world) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String playerName, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String playerName, String world) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String playerName, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }
}
