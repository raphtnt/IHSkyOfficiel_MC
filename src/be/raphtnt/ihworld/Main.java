package be.raphtnt.ihworld;

import be.raphtnt.ihworld.database.DatabaseAccess;
import be.raphtnt.ihworld.database.DatabaseCredentials;
import be.raphtnt.ihworld.events.BlockBreakEvents;
import be.raphtnt.ihworld.events.BlockPlaceEvents;
import be.raphtnt.ihworld.events.InteractEvents;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JavaPlugin {

    public static Main instance;
    public ArrayList<Island> storageIsland = new ArrayList<>();
    HashMap<Player, Players> storagePlayer = new HashMap<Player, Players>();

    public final GsonBuilder builder = new GsonBuilder();
    public final Gson gson = builder.create();


    // Objet ile avec une liste de tout les joueurs si il est dedans ok sinon non

    public DatabaseAccess databaseAccess;
    public DatabaseCredentials databaseCredentials = new DatabaseCredentials(getConfig().getString("host"), getConfig().getString("user"), getConfig().getString("password"), getConfig().getString("dbname"), getConfig().getInt("port"));

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;

        loadConfig();

        if(getConfig().getBoolean("database")) {
            System.out.println("[IHDev] - Chargement de la base de donnee");
            databaseAccess = new DatabaseAccess(databaseCredentials);
            databaseAccess.initPool();
            System.out.println("[IHDev] - Chargement de la base de donnee fini");
        }else {
            System.out.println("[IHDev] - Base de donnee non active | Config = database: false");
        }

        // UNLOAD ALL MAP

//        new Island("world");

        this.getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        this.getServer().getPluginManager().registerEvents(new BlockPlaceEvents(), this);
        this.getServer().getPluginManager().registerEvents(new BlockBreakEvents(), this);
        this.getServer().getPluginManager().registerEvents(new InteractEvents(), this);

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
                if (!(storageIsland.isEmpty())) {
                    System.out.println(storageIsland.size());
                    ArrayList<Island> t = (ArrayList<Island>) storageIsland.clone();
                    for (Island island : t) {
                        World world = Bukkit.getWorld(island.getName());
                        if (world.getPlayers().isEmpty()) {
                            System.out.println("Unload World " + world.getName());
                            if (!island.unloadWorld(world)) {
                                System.out.println("Erreur durant le unload du World : " + world.getName());
                            }
                        } else {
                            System.out.println("Save World " + world.getName());
                            world.save();
                        }
                    }
                }
            }
        });

        runnable.runTaskTimer(this, 6000L, 6000L);

    }

    @Override
    public void onDisable() {
        super.onDisable();
        if(getConfig().getBoolean("database")) {
            databaseAccess.closePool();
        }
    }

    public void loadConfig(){
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public static Main getInstance() {
        return instance;
    }

}
