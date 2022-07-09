package circus.teamwars.listeners;

import circus.teamwars.*;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        VIPTeam team = TeamController.getTeam(player);

        if (team == null) {
            return;
        }

        team.killPlayer(player);

        if (team.VIPDead()) {
            player.setGameMode(GameMode.SPECTATOR);
            PluginManager.server().broadcast(event.deathMessage());

            for (ItemStack itemStack : player.getInventory().getContents()) {
                if (itemStack != null) {
                    player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                }
            }

            player.getInventory().clear();

            event.setCancelled(true);
        }

        TeamController.updateScoreboard();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        TeamController.updateScoreboard();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        TeamController.updateScoreboard();
    }

    private static boolean isInside(Location loc, Location bound1, Location bound2) {
        if (loc.getX() <= bound1.getX() || bound2.getX() <= loc.getX()) {
            return false;
        }

        if (loc.getY() <= bound1.getY() || bound2.getY() <= loc.getY()) {
            return false;
        }

        if (loc.getZ() <= bound1.getZ() || bound2.getZ() <= loc.getZ()) {
            return false;
        }

        return true;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (GameStateController.state() == GameStateController.GameState.PREPARATION) {
            Player player = event.getPlayer();
            World world = player.getWorld();
            Location l1 = world.getSpawnLocation().clone().subtract(8, 256, 8);
            Location l2 = world.getSpawnLocation().clone().add(8, 256, 8);

            if (!isInside(event.getFrom(), l1, l2)) {
                player.teleport(world.getSpawnLocation());
                return;
            }

            if (!isInside(event.getTo(), l1, l2)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (GameStateController.state() == GameStateController.GameState.PREPARATION) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (GameStateController.state() == GameStateController.GameState.PREPARATION &&
                event.getEntity() instanceof Player) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (GameStateController.state() == GameStateController.GameState.PREPARATION) {
            event.setCancelled(true);
            return;
        }

        if (GameStateController.state() == GameStateController.GameState.NOPVP) {
            if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
                event.setCancelled(true);
            }
        }
    }
}
