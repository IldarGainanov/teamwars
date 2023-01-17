package circus.teamwars;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Lives {
    private static final HashMap<String, Integer> lives = new HashMap<>();

    public static int getLives(String team) {
        if (!lives.containsKey(team)) {
            lives.put(team, PluginManager.defaultLives());
            ScoreboardController.refresh();
        }

        return lives.get(team);
    }

    public static void setLives(String team, int newLives) {
        lives.put(team, Math.max(0, newLives));
        ScoreboardController.refresh();
    }

    public static void decLives(String team) {
        setLives(team, getLives(team) - 1);
    }

    public static void updateScoreboard() {
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
