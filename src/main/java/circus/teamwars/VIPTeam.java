package circus.teamwars;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;

public class VIPTeam {
    final ChatColor color;
    final Team team;
    String VIP = null;
    boolean deadVIP = false;

    public VIPTeam(@NotNull Team team, @NotNull ChatColor color) {
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

        String VIPStatus;

        if (deadVIP) {
            VIPStatus = "DEAD";
        } else {
            VIPStatus = Objects.requireNonNullElse(VIP, "???");
        }
        strings.add("VIP: " + VIPStatus);

        return strings;
    }

    public Team team() {
        return team;
    }

    public void addMember(@NotNull String name) {
        if (team.getEntries().contains(name)) {
            throw new IllegalArgumentException("Player is already in the specified team");
        }

        team().addEntry(name);
    }

    public void removeMember(@NotNull String name) {
        if (!team().removeEntry(name)) {
            throw new IllegalArgumentException("Player is not in the specified team");
        }
    }

    public void setVIP(@NotNull String name) {
        team.addEntry(name);
        deadVIP = false;
        VIP = name;
    }

    public void killPlayer(@NotNull Player player) {
        killPlayer(player.getDisplayName());
    }

    public void killPlayer(@NotNull String player) {
        if (player.equals(VIP)) {
            deadVIP = true;
            TeamController.updateScoreboard();
        }
    }

    public boolean VIPDead() {
        return deadVIP;
    }

}
