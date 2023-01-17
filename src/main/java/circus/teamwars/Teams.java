package circus.teamwars;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.*;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Teams {
    private static Scoreboard scoreboard = null;
    private static final HashMap<String, TeamWrapper> teamsByName = new HashMap<>();
    private static final HashMap<String, TeamWrapper> teamsByPlayer = new HashMap<>();

    public static void initScoreboard() {
        if (scoreboard == null) {
            scoreboard = ScoreboardController.getScoreboard();
        }
    }


    public static void updateScoreboard() {
        ArrayList<String> strings = new ArrayList<>(
                Collections.singleton("State: " + GameState.state().toString()));

        for (TeamWrapper team : teamsByName.values()) {
            strings.add("");
            strings.addAll(team.scoreboardData());
        }

        ScoreboardController.setSidebar("Status", strings);
    }


    public static void createTeam(String name, ChatColor color) {
        initScoreboard();

        if (teamsByName.containsKey(name)) {
            throw new IllegalArgumentException("Team already exists");
        }

        Team team = scoreboard.registerNewTeam(name);

        teamsByName.put(name, new TeamWrapper(team, color));

        updateScoreboard();
    }

    public static void deleteTeam(String name) {
        initScoreboard();

        TeamWrapper team = teamsByName.get(name);

        if (team == null) {
            throw new IllegalArgumentException("This team does not exist");
        }

        teamsByName.remove(name);

        ArrayList<String> toRemove = new ArrayList<>();
        for (var entry : teamsByPlayer.entrySet()) {
            if (entry.getValue().team().getName().equals(name)) {
                toRemove.add(entry.getKey());
            }
        }
        for (String player : toRemove) {
            teamsByPlayer.remove(player);
        }

        team.team().unregister();

        updateScoreboard();
    }

    public static void addMember(String teamName, String player) {
        initScoreboard();

        TeamWrapper team = teamsByName.get(teamName);

        if (team == null) {
            throw new IllegalArgumentException("This team does not exist");
        }

        TeamWrapper oldTeam = teamsByPlayer.get(player);

        if (oldTeam != null) {
            oldTeam.team().removeEntry(player);
        }

        team.addMember(player);
        teamsByPlayer.put(player, team);

        updateScoreboard();
    }

    public static void removeMember(String teamName, String player) {
        initScoreboard();

        TeamWrapper team = teamsByName.get(teamName);

        if (team == null) {
            throw new IllegalArgumentException("This team does not exist");
        }

        team.removeMember(player);

        teamsByPlayer.remove(player);

        updateScoreboard();
    }

    public static TeamWrapper getTeam(@NotNull Player player) {
        return getTeam(player.getDisplayName());
    }

    public static TeamWrapper getTeam(@NotNull String player) {
        return teamsByPlayer.get(player);
    }

    public static void setConfig(YamlConfiguration config) {
        initScoreboard();

        for (Team team : scoreboard.getTeams()) {
            team.unregister();
        }
        teamsByName.clear();
        teamsByPlayer.clear();

        for (String name : config.getKeys(false)) {
            ConfigurationSection teamConfig = config.getConfigurationSection(name);
            ChatColor color = ChatColor.valueOf(Objects.requireNonNull(teamConfig.getString("color")));
            List<String> members = teamConfig.getStringList("members");

            createTeam(name, color);
            for (String member : members) {
                addMember(name, member);
            }
        }
    }

    public static YamlConfiguration getConfig() {
        initScoreboard();

        YamlConfiguration config = new YamlConfiguration();

        for (Team team : scoreboard.getTeams()) {
            String name = team.getName();
            String color = teamsByName.get(name).color().name();
            ConfigurationSection teamConfig = config.createSection(name);
            teamConfig.set("color", color);
            List<String> members = new ArrayList<>();
            members.addAll(team.getEntries());
            teamConfig.set("members", members);
        }

        return config;
    }

    public static String getPlayerTeam(String player) {
        var team = teamsByPlayer.get(player);

        if (team == null) {
            return null;
        }

        return team.team().getName();
    }

    public static String getPlayerTeam(Player player) {
        return getPlayerTeam(player.getDisplayName());
    }
}
