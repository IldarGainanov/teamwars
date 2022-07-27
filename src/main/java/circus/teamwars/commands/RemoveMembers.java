package circus.teamwars.commands;

import circus.teamwars.Teams;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RemoveMembers implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
                             @NotNull String title, @NotNull String[] args)
    {
        if (args.length < 2) {
            return false;
        }

        int lastSuccess = 0;

        try {
            for (int i = 1; i < args.length; i++) {
                Teams.removeMember(args[0], args[i]);
                lastSuccess = i;
            }
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage(e.getMessage());

            for (int i = 1; i <= lastSuccess; i++) {
                Teams.addMember(args[0], args[i]);
            }

            return true;
        }

        return true;
    }
}
