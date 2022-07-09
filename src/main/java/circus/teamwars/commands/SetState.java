package circus.teamwars.commands;

import circus.teamwars.GameStateController;
import circus.teamwars.TeamController;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SetState implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
                             @NotNull String title, @NotNull String[] args)
    {
        if (args.length != 1) {
            return false;
        }

        GameStateController.GameState state;
        try {
            state = GameStateController.GameState.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage("Unknown state");
            return false;
        }

        GameStateController.setState(state);

        return true;
    }
}
