package circus.teamwars.commands;

import circus.teamwars.Teams;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RemoveTeam implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
                             @NotNull String title, @NotNull String[] args)
    {
        if (args.length != 1) {
            return false;
        }

        try {
            Teams.deleteTeam(args[0]);
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage(e.getMessage());
            return true;
        }


        return true;
    }
}
