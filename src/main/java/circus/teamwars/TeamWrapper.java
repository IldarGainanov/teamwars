package circus.teamwars;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Set;

public class TeamWrapper {
    // TODO Rewrite (this can be implemented using without this class)
    private final ChatColor color;
    private final Team team;
    public TeamWrapper(@NotNull Team team, @NotNull ChatColor color) {
        this.team = team;
        this.color = color;

        this.team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.FOR_OTHER_TEAMS);
        this.team.setAllowFriendlyFire(false);
        this.team.setColor(color);
    }

    LinkedList<String> scoreboardData() {
        LinkedList<String> strings = new LinkedList<>();

        strings.add(color.toString() + team.getName());

        int count = 0;
        Set<String> members = team.getEntries();
        for (Player player : PluginManager.server().getOnlinePlayers()) {
            if (members.contains(player.getDisplayName()) && player.getGameMode() != GameMode.SPECTATOR) {
                count++;
            }
        }

        strings.add("Members: " + count);
        strings.add("Lives: " + Lives.getLives(team.getName()));

        return strings;
    }

    public Team team() {
        return team;
    }

    public ChatColor color() {
        return color;
    }

    public void addMember(@NotNull String name) {
        if (team.getEntries().contains(name)) {
            throw new IllegalArgumentException(String.format("Player %s is already in the specified team", name));
        }

        team().addEntry(name);
    }

    public void removeMember(@NotNull String name) {
        if (!team().removeEntry(name)) {
            throw new IllegalArgumentException("Player is not in the specified team");
        }
    }
}
