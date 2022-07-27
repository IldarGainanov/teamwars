package circus.teamwars;

import circus.teamwars.PluginManager;
import circus.teamwars.Teams;
import net.kyori.adventure.text.Component;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreboardController {
    private static Scoreboard scoreboard = null;
    private static Objective sidebar = null;
    private static Objective playerList = null;

    public static void initScoreboard() {
        if (scoreboard != null) {
            return;
        }

        ScoreboardManager manager = PluginManager.server().getScoreboardManager();
        if (manager == null) {
            PluginManager.server().getLogger().warning(
                    "[TeamWars] Tried to get the scoreboard manager without a loaded world");
            return;
        }
        scoreboard = manager.getMainScoreboard();
    }

    private static String makeUnique(String s, List<String> strings) {
        while (strings.contains(s)) {
            s += "§r";
        }

        return s;
    }

    public static void setSidebar(@NotNull String title, @NotNull List<String> strings) {
        initScoreboard();

        if (sidebar == null) {
            sidebar = scoreboard.getObjective("sidebar");
        }

        if (sidebar != null) {
            sidebar.unregister();
        }

        sidebar = scoreboard.registerNewObjective("sidebar", "dummy", Component.text(title));
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        sidebar.setRenderType(RenderType.INTEGER);

        strings.replaceAll(s -> makeUnique(s, strings));

        int dummyScore = strings.size();
        for (String s : strings) {
            Score score = sidebar.getScore(s);
            score.setScore(dummyScore--);
        }
    }

    public static void setPlayerList(@NotNull Map<String, Integer> playerLives) {
        initScoreboard();

        if (playerList == null) {
            playerList = scoreboard.getObjective("playerlist");
        }

        if (playerList != null) {
            playerList.unregister();
        }

        playerList = scoreboard.registerNewObjective("playerlist", "dummy", Component.text("Lives"));
        playerList.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        playerList.setRenderType(RenderType.INTEGER);

        for (var p : playerLives.entrySet()) {
            Score score = playerList.getScore(p.getKey());
            score.setScore(p.getValue());
        }
    }

    public static Scoreboard getScoreboard() {
        initScoreboard();

        return scoreboard;
    }

    public static void refresh() {
        Teams.updateScoreboard();
        Lives.updateScoreboard();
    }

}
