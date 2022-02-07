package io.github.omen44.indroEconomy.models;

import java.util.UUID;

public class PlayerEconomyModel {

    private final String id;

    private UUID playerUUID;
    private int wallet;
    private int bank;

    public PlayerEconomyModel(UUID playerUUID, int wallet, int bank) {
        this.playerUUID = playerUUID;
        this.wallet = wallet;
        this.bank = bank;

        this.id = UUID.randomUUID().toString();
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }

    public int getBank() {
        return bank;
    }

    public void setBank(int bank) {
        this.bank = bank;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public String getId() {return id;}
}
