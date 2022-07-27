package circus.teamwars;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Lives {
    private static final HashMap<String, Integer> lives = new HashMap<>();

    public static int getLives(String player) {
        if (!lives.containsKey(player)) {
            lives.put(player, PluginManager.defaultLives());
        }
        return lives.get(player);
    }

    public static int getLives(Player player) {
        return getLives(player.getDisplayName()); // TODO Use non-deprecated methods
    }

    public static void setLives(String player, int newLives) {
        lives.put(player, Math.max(0, getLives(player) - 1));
    }

    public static void setLives(Player player, int newLives) {
        setLives(player.getDisplayName(), newLives);
    }

    public static void decLives(String player) {
        setLives(player, getLives(player) - 1);
    }

    public static void decLives(Player player) {
        decLives(player.getDisplayName());
    }

    public static void updateScoreboard() {
        for (Player player : PluginManager.server().getOnlinePlayers()) {
            getLives(player);
        }

        ScoreboardController.setPlayerList(lives);
    }

    public static void setConfig(YamlConfiguration config) {
        for (var p : config.getValues(false).entrySet()) {
            lives.put(p.getKey(), (Integer) p.getValue());
        }
    }

    public static YamlConfiguration getConfig() {
        YamlConfiguration config = new YamlConfiguration();
        for (var p : lives.entrySet()){
            config.set(p.getKey(), p.getValue());
        }
        return config;
    }
}
