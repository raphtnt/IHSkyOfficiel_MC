package be.raphtnt.ihworld.events;

import be.raphtnt.ihworld.Island;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceEvents implements Listener {


    @EventHandler
    public void placeEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        /*
        Si il est dans la liste on passe a la prochaine condition sinon MESSAGE ERROR
        Prochaine condition = Si il est dans la zone upgradable alors il peut construire sinon MESSAGE ERROR

         */
//        System.out.println(BlockPlaceEvents.getIsland(playerGETWorld.getName()).getPlayerList().get(player));

        if(!(Island.inLocation(player, event.getBlock().getLocation(), "isPlaceBlock"))) {
            event.setCancelled(true);
            player.sendMessage("IHSky >> Vous n'avez pas la permission de placer des blocks !");
        }


/*        if(world == getWorld) {
            player.sendMessage("Tu es dans le monde de raphtnt !");
        }else {
            player.sendMessage("Tu es dans le monde : " + world);
        }*/

    }

/*    public boolean test(Player player, Location location) {
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
        if(getIsland(world.getName()).getRank(player) != null && getIsland(world.getName()).getRank(player).isPlaceBlock()) {
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
    }*/

}
