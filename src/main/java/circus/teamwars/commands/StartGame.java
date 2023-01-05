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
        if (args.length != 0) {
            return false;
        }

        try {
            GameState.startGame();
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage(e.getMessage());
            return true;
        }

        return true;
    }
}
