package circus.teamwars;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

public class GameState {
    private static State state = State.INACTIVE;
    public static State state() {
        return state;
    }

    public enum State {
        INACTIVE, PREPARATION, NOPVP, PVP
    }

    public static void setState(State state) {
        GameState.state = state;

        World world = null;
        if (state == State.PREPARATION) {

            for (Player player : PluginManager.server().getOnlinePlayers()) {
                if (world == null) {
                    world = player.getWorld();
                }
                player.setGameMode(GameMode.SURVIVAL);
                player.setVelocity(player.getVelocity().add(new Vector(0, 0.2, 0)));
            }

            if (world != null) {
                world.setTime(0);
                world.setGameRule(GameRule.DO_INSOMNIA, false);
                world.setGameRule(GameRule.MOB_GRIEFING, false);
                world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            }

        } else {

            for (Player player : PluginManager.server().getOnlinePlayers()) {
                world = player.getWorld();
                break;
            }

            if (world != null) {
                world.setGameRule(GameRule.DO_INSOMNIA, true);
                world.setGameRule(GameRule.MOB_GRIEFING, true);
                world.setGameRule(GameRule.DO_MOB_SPAWNING, true);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
            }
        }

        ScoreboardController.refresh();

    }

    public static void startGameWithDelayedPVP(int seconds) {


        if (state == State.PREPARATION) {

            for (Player player : PluginManager.server().getOnlinePlayers()) {
                player.setStatistic(Statistic.TIME_SINCE_REST, 0);
                player.setHealth(20);
                player.setFoodLevel(20);
                player.setExhaustion(0);
                player.setSaturation(5);
                player.getInventory().clear();
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    player.removePotionEffect(effect.getType());
                }
            }

            setState(State.NOPVP);

            PluginManager.server().broadcast(
                    Component.text(String.format("PVP will start in %d seconds!", seconds))
                            .color(TextColor.color(255, 0 ,0)));

            PluginManager.server().getScheduler().runTaskLater(PluginManager.instance(), () -> {
                setState(State.PVP);

                PluginManager.server().broadcast(
                        Component.text("PVP is now allowed!")
                                .color(TextColor.color(255, 0 ,0)));
            }, 20 * seconds);
        } else {
            throw new IllegalArgumentException("Game must be in preparation state");
        }

    }

    public static void setConfig(YamlConfiguration config) {
        setState(State.valueOf(config.getString("state")));
    }

    public static YamlConfiguration getConfig() {
        YamlConfiguration config = new YamlConfiguration();
        config.set("state", state().toString());
        return config;
    }
}
