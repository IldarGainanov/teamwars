package circus.teamwars.commands;

import circus.teamwars.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class StartGame implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
                             @NotNull String title, @NotNull String[] args)
    {
        if (args.length != 1) {
            return false;
        }

        int seconds;
        try {
            seconds = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            commandSender.sendMessage("Could not parse time");
            return true;
        }

        if (seconds < 0) {
            commandSender.sendMessage("Could not parse time");
            return true;
        }

        try {
            GameState.startGameWithDelayedPVP(seconds);
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage(e.getMessage());
            return true;
        }

        return true;
    }
}
