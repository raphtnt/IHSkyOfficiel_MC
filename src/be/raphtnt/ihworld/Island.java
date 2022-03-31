package be.raphtnt.ihworld;

import be.raphtnt.ihworld.database.SQLRequete;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

public class Island {

    private String name;
    private World world;
    private HashMap<String, String> playerList = new HashMap<String, String>();
    private ArrayList<IslandPermission> rankList = new ArrayList<>();
    private ArrayList<String> perms = new ArrayList<>();

    SlimePlugin plugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
    SlimeLoader sqlLoader = plugin.getLoader("mysql");

    public void addPerms() {
        perms.add("isPlaceBlock");
        perms.add("isBreakBlock");
        perms.add("interact.chest");
    }

    public Island(String name) {
        addPerms();
        this.name = name;
        Main.getInstance().storageIsland.add(this);
        rankList.add(new IslandPermission("owner", perms));
        initPlayer();
    }

    public Island(String name, Player player) {
        addPerms();
        this.name = name;
        Main.getInstance().storageIsland.add(this);
        rankList.add(new IslandPermission("owner", perms));
        // Load bdd player
        initPlayer();
        // Load World

        // Load properties
        SlimePropertyMap slimePropertyMap = new SlimePropertyMap();
        slimePropertyMap.setBoolean(SlimeProperties.ALLOW_ANIMALS, true);
        slimePropertyMap.setBoolean(SlimeProperties.ALLOW_MONSTERS, true);
        slimePropertyMap.setString(SlimeProperties.DIFFICULTY, Difficulty.NORMAL.toString());
        slimePropertyMap.setBoolean(SlimeProperties.PVP, true);
        slimePropertyMap.setInt(SlimeProperties.SPAWN_X, 0);
        slimePropertyMap.setInt(SlimeProperties.SPAWN_Y, 63);
        slimePropertyMap.setInt(SlimeProperties.SPAWN_Z, 0);

        try {
            if(sqlLoader.isWorldLocked(name)) {
                if(Bukkit.getWorld(name) == null) {
                    sqlLoader.unlockWorld(name);
                    loadWorld(slimePropertyMap, player);
                }else {
                    player.teleport(new Location(Bukkit.getWorld(name), 0, 63, 0));
                }
            }else {
                loadWorld(slimePropertyMap, player);
            }
        } catch (UnknownWorldException | IOException e) {
            player.sendMessage("IHSky >> Veuillez contactez un membre du staff! #I79");
            e.printStackTrace();
        }


    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
    }

    public HashMap<String, String> getPlayerList() {
        return playerList;
    }

    public ArrayList<IslandPermission> getRankList() {
        return rankList;
    }

    public void addPlayer(String player, String rank) {
        // ADD BDD
        if(playerList.get(player).equals(player)) {
            playerList.remove(player);
        }
        playerList.put(player, rank);
    }

    public void initPlayer() {
        SQLRequete sqlRequete = new SQLRequete();
        Main.getInstance().gson.fromJson(sqlRequete.getIslandsPlayer(this.getName()), List.class).forEach(t -> {
            playerList.put(t.toString().split(":")[0], t.toString().split(":")[1]);
        });
    }

    public IslandPermission getRank(Player player) {
        for (IslandPermission islandPermission : rankList) {
            if(islandPermission.getRankName().equals(playerList.get(player.getName()))) {
                return islandPermission;
            }
        }
        return null;
    }

    private void loadWorld(SlimePropertyMap slimePropertyMap, Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            try {
//                    SlimeWorld slimeWorld = plugin.loadWorld(sqlLoader, "raphtnt_creepy", false, slimePropertyMap);
                SlimeWorld slimeWorld = plugin.loadWorld(sqlLoader, name, false, slimePropertyMap);
                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    try {
                        plugin.generateWorld(slimeWorld);
                        player.teleport(new Location(Bukkit.getWorld(name), 0, 63, 0));
                    } catch (IllegalArgumentException ex) {
                        System.out.println("--> " + ex);
                        return;
                    }
                });

            } catch (CorruptedWorldException | NewerFormatException | UnknownWorldException | IOException | WorldInUseException e) {
                player.sendMessage("IHSky >> Veuillez contactez un membre du staff! #I73");
                e.printStackTrace();
            }
        });
    }

    public boolean unloadWorld(World world) {
        System.out.println("Avant : " + Main.getInstance().storageIsland.size());
        if(Bukkit.unloadWorld(world, true)) {
//            world.save();
/*            final ArrayList<String> players = new ArrayList<>();
            Set set = playerList.entrySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()) {
                Map.Entry mentry = (Map.Entry) iterator.next();
                players.add(mentry.getKey() + ":" + mentry.getValue());
            }*/

//            String pList = Main.getInstance().gson.toJson(players, List.class);

            SQLRequete sqlRequete = new SQLRequete();
//            sqlRequete.updateIslandsPlayer(players, world.getName());
            sqlRequete.updateIslandsPlayer(world.getName());

            Main.getInstance().storageIsland.remove(this);
//            rankList.remove(new IslandPermission("owner", true));
            System.out.println(Main.getInstance().storageIsland.size());
            return true;
        }
        return false;
    }



    public static boolean inLocation(Player player, Location location, String perms) {
//        World world = Bukkit.getWorld("raphtnt_creepy");

        World world = player.getWorld();
        double x = 0.0;
        double y = 100.0;
        double z = 0.0;
        int level = 10; // Upgrade * 10
        Location location1 = new Location(world,x - level,y - 100,z - level);
        Location location2 = new Location(world,x + level,y + 155,z + level);
        Cuboid cuboid = new Cuboid(location1, location2);

        if(getIsland(world.getName()) == null) return false;
//        if(getIsland(world.getName()).getRank(player) != null && getIsland(world.getName()).getRank(player).isPlaceBlock()) {

//        if(getIsland(world.getName()).getRank(player) != null && getIsland(player.getWorld().getName()).getRank(player).isPlaceBlock()) {
        if(getIsland(world.getName()).getRank(player) != null && getIsland(player.getWorld().getName()).getRank(player).getPerms().contains(perms)) {
            return cuboid.isIn(location);
        }

        return false;
    }


    public static Island getIsland(String worldName) {
        for (Island island : Main.getInstance().storageIsland) {
            if (island.getName().equals(worldName)) {
                return island;
            }
        }
        return null;
    }

}
