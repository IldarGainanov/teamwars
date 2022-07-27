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

        Lives.decLives(player);

        if (Lives.getLives(player) == 0) {
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

        ScoreboardController.refresh();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ScoreboardController.refresh();

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        ScoreboardController.refresh();

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
        if (GameState.state() == GameState.State.PREPARATION) {
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
        if (GameState.state() == GameState.State.PREPARATION) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (GameState.state() == GameState.State.PREPARATION &&
                event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (GameState.state() == GameState.State.PREPARATION) {
            event.setCancelled(true);
            return;
        }

        if (GameState.state() == GameState.State.NOPVP) {
            if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
                event.setCancelled(true);
            }
        }
    }
}
