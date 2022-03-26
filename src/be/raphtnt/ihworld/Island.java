package be.raphtnt.ihworld;

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
import java.util.ArrayList;
import java.util.HashMap;

public class Island {

    private String name;
    private World world;
    private HashMap<Player, String> playerList = new HashMap<>();
    private ArrayList<IslandPermission> rankList = new ArrayList<>();

    SlimePlugin plugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
    SlimeLoader sqlLoader = plugin.getLoader("mysql");

    public Island(World worldName) {
        this.name = worldName.getName();
    }

    public Island(String name) {
        this.name = name;
        Main.getInstance().storageIsland.add(this);
        rankList.add(new IslandPermission("owner", true));
    }

    public Island(String name, Player player) {
        this.name = name;

        Main.getInstance().storageIsland.add(this);
        rankList.add(new IslandPermission("owner", true));
        // Load bdd player

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

    public HashMap<Player, String> getPlayerList() {
        return playerList;
    }

    public ArrayList<IslandPermission> getRankList() {
        return rankList;
    }

    public void addPlayer(Player player, String rank) {
        // ADD BDD
        playerList.put(player, rank);
    }

    public IslandPermission getRank(Player player) {
        for (IslandPermission islandPermission : rankList) {
            if(islandPermission.getRankName().equals(playerList.get(player))) {
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
            Main.getInstance().storageIsland.remove(this);
            rankList.remove(new IslandPermission("owner", true));
            System.out.println(Main.getInstance().storageIsland.size());
            return true;
        }
        return false;
    }


}
