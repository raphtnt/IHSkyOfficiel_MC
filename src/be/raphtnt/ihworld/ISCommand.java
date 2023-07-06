package be.raphtnt.ihworld;

import be.raphtnt.ihworld.database.SQLRequete;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.io.IOException;
import java.sql.Array;
import java.util.Iterator;
import java.util.List;

public class ISCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        SQLRequete sqlRequete = new SQLRequete();

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

        }else if(args[0].equalsIgnoreCase("invite")) {


            World world = player.getWorld();
            if (Island.getIsland(world.getName()).getPlayerList().get(args[1]).equals(args[1])) {
                player.sendMessage("Ce joueurs est déjà sur votre ile !");
                return true;
            } else {
                Island.getIsland(world.getName()).addPlayer(args[1], "default");
                player.sendMessage("Ce joueur est désormait sur votre île !");
                return true;
            }

//            Main.getInstance().storagePlayer.get(player).setListIsland(storageISPlayer);
        }else if(args[0].equalsIgnoreCase("promote")) {
            World world = player.getWorld();
            Island.getIsland(world.getName()).addPlayer(args[1], args[2]);
            player.sendMessage("Tu viens de promote " + args[1] + " au rang de " + args[2]);
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
        }else if(args[0].equalsIgnoreCase("inventory")) {

            ItemStack result = null;
            Iterator<Recipe> iter = Bukkit.recipeIterator();

            while (iter.hasNext()) {
                Recipe recipe = iter.next();
                if (!(recipe instanceof FurnaceRecipe)) continue;
                for (ItemStack item : player.getInventory().getContents()) {
                    if (item != null) {
                        if (((FurnaceRecipe) recipe).getInput().getType() != item.getType()) continue;
                        result = recipe.getResult();
                        result.setAmount(item.getAmount());
                        player.getInventory().removeItem(item);
                        player.getInventory().addItem(result);
                    }
                }
            }



/*            for (ItemStack item : player.getInventory().getContents()) {
//                System.out.println(item.getData());
                for (Iterator<Recipe> it = Bukkit.recipeIterator(); it.hasNext(); ) {
                    FurnaceRecipe recipe = (FurnaceRecipe) it.next();
                    if (recipe.getInputChoice().test(item)) {
                        player.getInventory().addItem(recipe.getResult());
                    }
                }
            }*/

            return true;
        }else if(args[0].equalsIgnoreCase("players")) {
            if(args[1].equalsIgnoreCase("get")) {
                String islandsName = args[2];
                player.sendMessage(sqlRequete.getIslandsPlayer(islandsName));

            }else if(args[1].equalsIgnoreCase("update")) {
                player.sendMessage("Update OK !");
            }else if(args[1].equalsIgnoreCase("world")){
                World world = Bukkit.getWorld(args[2]);
                Island.getIsland(world.getName()).unloadWorld(world);
            }
            return true;
        }else if(args[0].equalsIgnoreCase("group")) {
            MenuGroup menuGroup = new MenuGroup();
            menuGroup.openInventory(player);

            SQLRequete sqlRequetes = new SQLRequete();
            Main.getInstance().gson.fromJson(sqlRequetes.getIslandsOneGroup("fox", "Owner"), List.class).forEach(t -> {
                System.out.println(t.toString());
            });

            return true;
        }else {
            player.sendMessage("Please write : /is <normal/nether/creepy/list)");
            return true;
        }
    }
}
