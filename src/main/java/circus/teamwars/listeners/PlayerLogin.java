package circus.teamwars.listeners;

import circus.teamwars.Teams;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLogin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Teams.updateScoreboard();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Teams.updateScoreboard();
    }

}
