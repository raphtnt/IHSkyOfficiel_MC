package be.raphtnt.ihworld;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class JoinEvent implements Listener {

    @EventHandler
    public void JE(PlayerJoinEvent event) {
        Player player = event.getPlayer();
//        ArrayList<String> storageISPlayerS = new ArrayList<String>();
//        storageISPlayerS.add("test");

//        Players players = new Players(player.getName(), 500, storageISPlayerS);
//        Main.getInstance().storagePlayer.put(player, players);

    }

}
