package circus.teamwars;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Teams {
    private static ScoreboardManager scoreboardManager = null;
    private static Scoreboard scoreboard;
    private static HashMap<String, VIPTeam> teamsByName = new HashMap<>();
    private static HashMap<String, VIPTeam> teamsByPlayer = new HashMap<>();

    public static void initScoreboard() {
        ScoreboardManager newManager = Manager.server().getScoreboardManager();
        if (scoreboardManager == null && newManager != null) {
            scoreboardManager = Manager.server().getScoreboardManager();
            scoreboard = scoreboardManager.getNewScoreboard();
        } else if (newManager == null) {
            Manager.server().getLogger().warning(
                    "[TeamWars] Tried to get the scoreboard manager without a loaded world");
        }
    }

    public static void showScoreboard() {
        for (Player player : Manager.server().getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
        }
    }

    public static void updateScoreboard() {
        ArrayList<String> strings = new ArrayList<>();
        boolean first = true;

        for (VIPTeam team : teamsByName.values()) {
            if (!first) {
                strings.add("");
            }

            first = false;

            strings.addAll(team.scoreboardData());
        }

        setScoreboardData("Status", strings);

        showScoreboard();
    }

    private static String makeUnique(String s, List<String> strings) {
        while (strings.contains(s)) {
            s += "§r";
        }

        return s;
    }

    private static void setScoreboardData(@NotNull String title, @NotNull ArrayList<String> strings) {
        initScoreboard();

        for (int i = 0; i < strings.size(); i++) {
            strings.set(i, makeUnique(strings.get(i), strings));
        }

        Objective objective = scoreboard.getObjective("custom");

        if (objective != null) {
            objective.unregister();
        }

        objective = scoreboard.registerNewObjective("custom", "dummy", Component.text(title));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setRenderType(RenderType.INTEGER);

        StringBuilder spaceBuilder = new StringBuilder("");
        int dummyScore = strings.size();

        for (String s : strings) {
            Score score;
            score = objective.getScore(s);
            score.setScore(dummyScore--);
        }

        showScoreboard();
    }

    public static void createTeam(String name, ChatColor color) {
        initScoreboard();

        if (teamsByName.containsKey(name)) {
            throw new IllegalArgumentException("Team already exists");
        }

        Team team = scoreboard.registerNewTeam(name);

        teamsByName.put(name, new VIPTeam(team, color));

        updateScoreboard();
    }

    public static void deleteTeam(String name) {
        initScoreboard();

        VIPTeam team =  teamsByName.get(name);

        if (team == null) {
            throw new IllegalArgumentException("This team does not exist");
        }


        team.team().unregister();

        teamsByName.remove(name);

        updateScoreboard();
    }

    public static void addMember(String teamName, String player) {
        initScoreboard();

        VIPTeam team = teamsByName.get(teamName);

        if (team == null) {
            throw new IllegalArgumentException("This team does not exist");
        }

        team.addMember(player);

        teamsByPlayer.put(player, team);

        updateScoreboard();
    }

    public static void removeMember(String teamName, String player) {
        initScoreboard();

        VIPTeam team = teamsByName.get(teamName);

        if (team == null) {
            throw new IllegalArgumentException("This team does not exist");
        }

        team.removeMember(player);

        teamsByPlayer.remove(player);

        updateScoreboard();
    }

    public static void setVIP(@NotNull String teamName, @NotNull String player) {
        initScoreboard();

        VIPTeam team = teamsByName.get(teamName);

        if (team == null) {
            throw new IllegalArgumentException("This team does not exist");
        }

        team.setVIP(player);

        teamsByPlayer.put(player, team);

        updateScoreboard();
    }

    public static VIPTeam getTeam(@NotNull Player player) {
        return getTeam(player.getDisplayName());
    }

    public static VIPTeam getTeam(@NotNull String player) {
        return teamsByPlayer.get(player);
    }
}
