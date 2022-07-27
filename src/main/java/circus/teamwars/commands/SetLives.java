package circus.teamwars.commands;

import circus.teamwars.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SetLives implements CommandExecutor {
    // TODO
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
                             @NotNull String title, @NotNull String[] args) {
        if (args.length < 2) {
            return false;
        }

        GameState.State state;
        try {
            state = GameState.State.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage("Unknown state");
            return false;
        }

        GameState.setState(state);

        return true;
    }
}