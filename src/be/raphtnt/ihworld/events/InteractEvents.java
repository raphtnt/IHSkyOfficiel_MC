package be.raphtnt.ihworld.events;

import be.raphtnt.ihworld.DenyInteractEvents;
import be.raphtnt.ihworld.Island;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractEvents implements Listener {

    @EventHandler
    public void interactEvents(PlayerInteractEvent event) {
        Player player = event.getPlayer();
//        if(event.getClickedBlock().getLocation() != null) {
            for (DenyInteractEvents denyInteractEvents : DenyInteractEvents.values()) {
                if(event.getClickedBlock().getType() == denyInteractEvents.getMaterial()) {
//                    if(!(Island.inLocation(player, event.getClickedBlock().getLocation(), "interact"))) {
                    if(!(Island.inLocation(player, event.getClickedBlock().getLocation(), denyInteractEvents.getPermssions()))) {
                        event.setCancelled(true);
                        player.sendMessage("IHSky >> Vous n'avez pas la permission de d'interagir avec " + denyInteractEvents + " !");
                        break;
                    }
                }
            }


//        }

    }
}
