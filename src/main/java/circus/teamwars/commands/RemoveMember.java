package circus.teamwars.commands;

import circus.teamwars.Teams;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RemoveMember implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
                             @NotNull String title, @NotNull String[] args)
    {
        if (args.length != 2) {
            return false;
        }

        try {
            Teams.removeMember(args[0], args[1]);
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage(e.getMessage());
            return true;
        }


        return true;
    }
}
