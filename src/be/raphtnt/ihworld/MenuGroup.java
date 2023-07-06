package be.raphtnt.ihworld;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MenuGroup {
    private final Inventory inv;

    public MenuGroup() {
        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
        inv = Bukkit.createInventory(null, 9 * 6, "Island Group");

        // Put the items into the inventory
        initItems();
    }

    public void initItems() {
        inv.addItem(Main.createCustomItem(Material.DIRT, 1, (byte) 0, "Placement des blocks", "Activé"));
        inv.addItem(Main.createCustomItem(Material.DIAMOND_PICKAXE, 1, (byte) 0, "Minage des blocks", "Activé"));
    }

    public void openInventory(Player player) {
        player.openInventory(inv);
    }

}
