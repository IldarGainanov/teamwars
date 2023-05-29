package circus.teamwars;

import circus.teamwars.commands.*;
import circus.teamwars.listeners.PlayerListener;
import circus.teamwars.listeners.WorldLoadListener;
import org.bukkit.Server;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PluginManager extends JavaPlugin {

    private static PluginManager instance;
    private static Server server;
    private static int defaultLives;
    private static int timeUntilPvp;
    private static List<Integer> borderSizes;
    private static List<Integer> borderTimes;
    private static List<Integer> borderShrinkTimes;

    @Override
    public void onEnable() {
        instance = this;
        server = getServer();
        org.bukkit.plugin.PluginManager pm = getServer().getPluginManager();

        Objects.requireNonNull(getCommand("addteams")).setExecutor(new AddTeams());
        Objects.requireNonNull(getCommand("remteams")).setExecutor(new RemoveTeams());
        Objects.requireNonNull(getCommand("addmembers")).setExecutor(new AddMembers());
        Objects.requireNonNull(getCommand("remmembers")).setExecutor(new RemoveMembers());
        Objects.requireNonNull(getCommand("setlives")).setExecutor(new SetLives());
        Objects.requireNonNull(getCommand("setstate")).setExecutor(new SetState());
        Objects.requireNonNull(getCommand("startgame")).setExecutor(new StartGame());

        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new WorldLoadListener(), this);

        File configFile = getConfigFile("config.yml");
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        defaultLives = config.getInt("default_lives");
        timeUntilPvp = config.getInt("time_until_pvp");
        borderSizes = (List<Integer>) config.getList("borders");
        borderTimes = (List<Integer>) config.getList("border_time");
        borderShrinkTimes = (List<Integer>) config.getList("border_shrink_time");

        configFile = getConfigFile("state.yml");
        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        GameState.setConfig(config);

        configFile = getConfigFile("teams.yml");
        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        Teams.setConfig(config);

        configFile = getConfigFile("lives.yml");
        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        Lives.setConfig(config);

        ScoreboardController.refresh();
    }

    @Override
    public void onDisable() {
        saveData();
    }

    public static PluginManager instance() {
        return instance;
    }

    public static Server server() {
        return server;
    }

    private File getConfigFile(String filename) {
        File file = new File(getDataFolder(), filename);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            saveResource(filename, false);
        }
        return file;
    }

    public static int defaultLives() {
        return defaultLives;
    }

    public static int timeUntilPvp() {
        return timeUntilPvp;
    }

    public static List<Integer>  borderSizes() {
        return borderSizes;
    }

    public static List<Integer>  borderTimes() {
        return borderTimes;
    }

    public static List<Integer>  borderShrinkTimes() {
        return borderShrinkTimes;
    }

    public void saveData() {
        File stateFile = getConfigFile("state.yml");
        File livesFile = getConfigFile("lives.yml");
        File teamsFile = getConfigFile("teams.yml");

        try {
            GameState.getConfig().save(stateFile);
            Lives.getConfig().save(livesFile);
            Teams.getConfig().save(teamsFile);
        } catch (IOException e) {

        }
    }

}
