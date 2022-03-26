package be.raphtnt.ihworld;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.*;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;

public class ISCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        SlimePlugin plugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        SlimeLoader sqlLoader = plugin.getLoader("mysql");

        SlimePropertyMap slimePropertyMap = new SlimePropertyMap();
        slimePropertyMap.setBoolean(SlimeProperties.ALLOW_ANIMALS, true);
        slimePropertyMap.setBoolean(SlimeProperties.ALLOW_MONSTERS, true);
        slimePropertyMap.setString(SlimeProperties.DIFFICULTY, Difficulty.EASY.toString());
        slimePropertyMap.setBoolean(SlimeProperties.PVP, true);
        slimePropertyMap.setInt(SlimeProperties.SPAWN_X, 0);
        slimePropertyMap.setInt(SlimeProperties.SPAWN_Y, 63);
        slimePropertyMap.setInt(SlimeProperties.SPAWN_Z, 0);

        if(args[0].equalsIgnoreCase("normal")) {

            try {
                System.out.println(sqlLoader.isWorldLocked("raphtnt_creepy"));
                sqlLoader.unlockWorld("raphtnt_creepy");
            } catch (UnknownWorldException | IOException e) {
                e.printStackTrace();
            }

            /*            World w = Bukkit.getWorld("raphtnt_creepy");
            Location spawnLocation = w.getSpawnLocation();
            player.teleport(spawnLocation);
            player.sendMessage("TP?!");*/

            return true;
        }else if(args[0].equalsIgnoreCase("nether")) {
            World w = Bukkit.getWorld("raphtnt_creepy");
            Location spawnLocation = w.getSpawnLocation();
            player.teleport(spawnLocation);
            return true;
        }else if(args[0].equalsIgnoreCase("creepy")) {
//                world = plugin.loadWorld(sqlLoader, "sky03", false, slimePropertyMap);

            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {

                try {
                    SlimeWorld slimeWorld = plugin.loadWorld(sqlLoader, "sky03", false, slimePropertyMap).clone(player.getName() + "_creepy", sqlLoader);
                    Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                        try {
                            plugin.generateWorld(slimeWorld);
                            sqlLoader.unlockWorld("sky03");
                        } catch (IllegalArgumentException ex) {
                            System.out.println("--> " + ex);
                            return;
                        } catch (UnknownWorldException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("YESSS MAN");
                    });
                } catch (CorruptedWorldException | NewerFormatException | WorldInUseException | UnknownWorldException | IOException | WorldAlreadyExistsException e) {
                    e.printStackTrace();
                }
            });

            return true;

        }else if(args[0].equalsIgnoreCase("list")) {
            Main.getInstance().storageIsland.forEach((t) -> {
                player.sendMessage("--> " + t.getName());
            });
/*            try {
                sqlLoader.listWorlds().forEach(sender::sendMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            return true;
        }else if(args[0].equalsIgnoreCase("tp")) {

            new Island(args[1], player);

/*
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                try {
//                    SlimeWorld slimeWorld = plugin.loadWorld(sqlLoader, "raphtnt_creepy", false, slimePropertyMap);
                    SlimeWorld slimeWorld = plugin.loadWorld(sqlLoader, args[1], false, slimePropertyMap);
                    Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                        try {
                            plugin.generateWorld(slimeWorld);
//                            new Island("raphtnt_creepy");
                            new Island(args[1]);
                        } catch (IllegalArgumentException ex) {
                            System.out.println("--> " + ex);
                            return;
                        }
                        System.out.println("YESSS MAN");
//                        World w = Bukkit.getWorld("raphtnt_creepy");
                        World w = Bukkit.getWorld(args[1]);
                        Location spawnLocation = w.getSpawnLocation();
                        player.teleport(spawnLocation);
                    });

                } catch (CorruptedWorldException | NewerFormatException | UnknownWorldException | IOException e) {
                    e.printStackTrace();
                } catch (WorldInUseException e) {
                    World w = Bukkit.getWorld(args[1]);
//                    World w = Bukkit.getWorld("raphtnt_creepy");
                    Location spawnLocation = w.getSpawnLocation();
                    player.teleport(spawnLocation);
                    player.sendMessage("TP?!");
                }
            });
*/

            return true;

        }else if(args[0].equalsIgnoreCase("cr")) {
/*            ArrayList<String> storageISPlayer = new ArrayList<String>();
            storageISPlayer.add(args[1]);       */
//            ArrayList storageISPlayer = Main.getInstance().storagePlayer.get(player).getListIsland();
//            storageISPlayer.add(args[1]);
            player.sendMessage("Add in the list");
//            ArrayList one = Main.getInstance().storagePlayer.get(player).getListIsland();
//            one.add(args[1]);

//            Main.getInstance().storageIsland.get(0).addPlayer(player, "owner");
            World world = player.getWorld();
            BlockPlaceEvents.getIsland(world.getName()).addPlayer(player, "owner");


//            Main.getInstance().storagePlayer.get(player).setListIsland(storageISPlayer);
            return true;
        }else if(args[0].equalsIgnoreCase("unlock")) {
            try {
                sqlLoader.unlockWorld(args[1]);
                player.sendMessage("Unlock world : " + args[1]);
            } catch (UnknownWorldException | IOException e) {
                player.sendMessage("Not existed World!");
                e.printStackTrace();
            }
            return true;
        }else if(args[0].equalsIgnoreCase("debug")) {
            Main.getInstance().storageIsland.forEach(island -> {
                World world = Bukkit.getWorld(island.getName());
                System.out.println(world);
                System.out.println("----------");
                System.out.println(world.getName());
            });
            return true;
        }else {

            player.sendMessage("Please write : /is <normal/nether/creepy/list)");
            return true;
        }
    }
}
