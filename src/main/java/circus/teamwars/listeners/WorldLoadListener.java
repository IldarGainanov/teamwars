package circus.teamwars.listeners;

import circus.teamwars.Teams;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldLoadListener implements Listener {

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        Teams.initScoreboard();
    }

}
