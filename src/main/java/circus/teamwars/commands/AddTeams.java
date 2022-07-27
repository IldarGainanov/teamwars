package circus.teamwars.commands;

import circus.teamwars.Teams;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class AddTeams implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
                             @NotNull String title, @NotNull String[] args) {
        if (args.length < 2 || args.length % 2 != 0) {
            return false;
        }

        int lastSuccess = 0;

        try {
            for (int i = 0; i < args.length / 2; i++) {
                ChatColor color;
                try {
                    color = ChatColor.valueOf(args[2 * i + 1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Unknown color " + args[2 * i + 1]);
                }

                Teams.createTeam(args[2 * i], color);
                lastSuccess = i;
            }
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage(e.getMessage());
            for (int i = 0; i <= lastSuccess; i++) {
                Teams.deleteTeam(args[2 * i]);
            }
            return true;
        }


        return true;
    }
}
