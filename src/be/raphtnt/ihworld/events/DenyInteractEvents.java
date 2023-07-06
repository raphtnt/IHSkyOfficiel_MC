package be.raphtnt.ihworld.events;

import org.bukkit.Material;

public enum DenyInteractEvents {

    CHEST(Material.CHEST, "interact.chest"),
    CARTOGRAPHY_TABLE(Material.CARTOGRAPHY_TABLE, "interact.cartography_table");


    Material material;
    String permssions;

    DenyInteractEvents(Material material, String permssions) {
        this.material = material;
        this.permssions = permssions;
    }

    public Material getMaterial() {
        return material;
    }

    public String getPermssions() {
        return permssions;
    }

}
