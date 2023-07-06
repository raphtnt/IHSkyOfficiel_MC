package be.raphtnt.ihworld;isPlaceBlock

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class Test implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        SlimePlugin plugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        SlimeLoader sqlLoader = plugin.getLoader("mysql");
        try {
/*            sqlLoader.listWorlds().forEach((list) -> {
                System.out.println(list);
            });*/
            sqlLoader.listWorlds().forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
