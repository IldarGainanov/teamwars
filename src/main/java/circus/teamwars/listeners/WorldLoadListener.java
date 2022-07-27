package circus.teamwars.listeners;

import circus.teamwars.PluginManager;
import circus.teamwars.ScoreboardController;
import circus.teamwars.Teams;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;

public class WorldLoadListener implements Listener {

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        ScoreboardController.refresh();
    }

    @EventHandler
    public void onWorldSave(WorldSaveEvent event) {
        PluginManager.instance().saveData();
    }

}
