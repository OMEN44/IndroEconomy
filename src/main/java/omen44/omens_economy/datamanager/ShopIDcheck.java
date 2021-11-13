package omen44.omens_economy.datamanager;

import omen44.omens_economy.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.block.TileState;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataContainer;

public class ShopIDcheck {
    private int[] shopsID;

    public boolean isShopExisting(int shopID) {
        boolean exists = false;
        for (int j : shopsID) {
            if (j == shopID) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    public void setShopsID(int[] shopsID) {
        this.shopsID = shopsID;
    }
}
