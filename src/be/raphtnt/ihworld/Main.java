package be.raphtnt.ihworld;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JavaPlugin {

    public static Main instance;
    ArrayList<Island> storageIsland = new ArrayList<>();
    HashMap<Player, Players> storagePlayer = new HashMap<Player, Players>();


    // Objet ile avec une liste de tout les joueurs si il est dedans ok sinon non

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        // UNLOAD ALL MAP

        new Island("world");

        this.getServer().getPluginManager().registerEvents(new BlockPlaceEvents(), this);
        this.getServer().getPluginManager().registerEvents(new JoinEvent(), this);

        getCommand("test").setExecutor(new Test());
        getCommand("is").setExecutor(new ISCommand());
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }


    public static Main getInstance() {
        return instance;
    }

}
