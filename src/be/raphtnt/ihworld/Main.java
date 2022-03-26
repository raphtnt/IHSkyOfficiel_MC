package be.raphtnt.ihworld;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JavaPlugin {

    public static Main instance;
    ArrayList<Island> storageIsland = new ArrayList<>();
    HashMap<Player, Players> storagePlayer = new HashMap<Player, Players>();

    int i = 0;

    // Objet ile avec une liste de tout les joueurs si il est dedans ok sinon non

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        // UNLOAD ALL MAP

//        new Island("world");

        this.getServer().getPluginManager().registerEvents(new BlockPlaceEvents(), this);
        this.getServer().getPluginManager().registerEvents(new JoinEvent(), this);

        getCommand("test").setExecutor(new Test());
        getCommand("is").setExecutor(new ISCommand());

/*
        BukkitRunnable runnable = (new BukkitRunnable() {
            @Override
            public void run() {
                if(storageIsland.size() != 0) {
                    if(i == 15) {
                        System.out.println("i = 15");
                        storageIsland.forEach(island -> {
                            World world = Bukkit.getWorld(island.getName());
                            if(world.getPlayers().isEmpty()) {
                                System.out.println("Unload World " + world.getName());
                                Bukkit.unloadWorld(world, true);
                            }else {
                                System.out.println("Save World " + world.getName());
                                world.save();
                            }
                        });
                        i = 0;
                    }
                    i++;
                    System.out.println(i);
                }
            }
        });

        runnable.runTaskTimerAsynchronously(this, 20L, 20L);*/

        BukkitRunnable runnable = (new BukkitRunnable() {
            @Override
            public void run() {
                if (storageIsland.size() != 0) {
                    System.out.println(storageIsland.size());
                    storageIsland.forEach(island -> {
                        World world = Bukkit.getWorld(island.getName());
                        System.out.println(island.getName());
                        if (world.getPlayers().isEmpty()) {
                            System.out.println("Unload World " + world.getName());
                            Island island1 = new Island(world);
                            if(!island1.unloadWorld(world)) {
                                System.out.println("Erreur durant le unload du World : " + world.getName());
                            }
//                            Bukkit.unloadWorld(world, true);
                        } else {
                            System.out.println("Save World " + world.getName());
                            world.save();
                        }
                    });
                }
            }
        });

        runnable.runTaskTimer(this, 200L, 200L);

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

}
