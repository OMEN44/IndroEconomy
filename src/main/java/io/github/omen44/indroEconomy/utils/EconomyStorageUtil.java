package io.github.omen44.indroEconomy.utils;

import com.google.gson.Gson;
import io.github.omen44.indroEconomy.IndroEconomy;
import io.github.omen44.indroEconomy.models.EconomyModel;
import org.bukkit.entity.Player;

import javax.annotation.processing.Filer;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class EconomyStorageUtil {

    //CRUD - Create, Read, Update, Delete

    private static ArrayList<EconomyModel> economy = new ArrayList<>();

    public static EconomyModel createAccount(Player p, int wallet, int bank) {

        EconomyModel model = new EconomyModel(p.getUniqueId(), wallet, bank);
        economy.add(model);
        try {
            saveAccounts();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return model;
    }

    public static EconomyModel findAccount(String id) {
        // linear search
        for (EconomyModel model : economy) {
            if (model.getId().equalsIgnoreCase(id)) {
                return model;
            }
        }
        return null;
    }

    public static EconomyModel findAccount(UUID uuid) {
        // linear search
        for (EconomyModel model : economy) {
            if (model.getPlayerUUID().equals(uuid)) {
                return model;
            }
        }
        return null;
    }

    public static EconomyModel updateAccount(String id, EconomyModel newAccount) {
        // linear search
        for (EconomyModel model : economy) {
            if (model.getId().equalsIgnoreCase(id)) {
                model.setBank(newAccount.getBank());
                model.setPlayerUUID(newAccount.getPlayerUUID());
                model.setWallet(newAccount.getWallet());

                try {
                    saveAccounts();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return model;
            }
        }
        return null;
    }

    public static void deleteAccount(String id) {

        // linear search
        for (EconomyModel model : economy) {
            if (model.getId().equalsIgnoreCase(id)) {
                economy.remove(model);
                break;
            }
        }
        try {
            saveAccounts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<EconomyModel> findAllAccounts() {
        return economy;
    }

    public static void saveAccounts() throws IOException {
        Gson gson = new Gson();
        File file = new File(IndroEconomy.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "economy.json");
        file.getParentFile().mkdir();
        file.createNewFile();
        Writer writer = new FileWriter(file, false);
        gson.toJson(economy, writer);
        writer.flush();
        writer.close();
    }

    public static void loadAccounts() throws IOException {

        Gson gson = new Gson();
        File file = new File(IndroEconomy.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "economy.json");
        if (file.exists()) {
            Reader reader = new FileReader(file);
            EconomyModel[] model = gson.fromJson(reader, EconomyModel[].class);
            economy = new ArrayList<>(Arrays.asList(model));
        }
    }
}
