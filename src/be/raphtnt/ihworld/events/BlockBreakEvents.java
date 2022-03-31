package be.raphtnt.ihworld.events;

import be.raphtnt.ihworld.Island;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakEvents implements Listener {

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent event) {
        Player player = (Player) event.getPlayer();
//        if(!(Island.inLocation(player, event.getBlock().getLocation()))) {
        if(!(Island.inLocation(player, event.getBlock().getLocation(), "isBreakBlock"))) {
            event.setCancelled(true);
            player.sendMessage("IHSky >> Vous n'avez pas la permission de casser des blocks !");
        }
    }
}
