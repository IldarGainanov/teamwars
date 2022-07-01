package circus.teamwars.listeners;

import circus.teamwars.Manager;
import circus.teamwars.Teams;
import circus.teamwars.VIPTeam;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        VIPTeam team = Teams.getTeam(player);

        if (team == null) {
            return;
        }

        team.killPlayer(player);

        if (team.VIPDead()) {
            player.setGameMode(GameMode.SPECTATOR);
            Manager.server().broadcast(event.deathMessage());
            event.setCancelled(true);
        }

        Teams.updateScoreboard();
    }



}
