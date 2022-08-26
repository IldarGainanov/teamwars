package circus.teamwars.commands;

import circus.teamwars.GameState;
import circus.teamwars.Lives;
import circus.teamwars.Teams;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SetLives implements CommandExecutor {
    // TODO
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
                             @NotNull String title, @NotNull String[] args) {

        if (args.length < 2 || args.length % 2 != 0) {
            return false;
        }

        try {
            for (int i = 0; i < args.length / 2; i++) {
                int lives;
                try {
                    lives = Integer.parseInt(args[2 * i + 1]);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Not a number:" + args[2 * i + 1]);
                }

                Lives.setLives(args[2 * i], lives);
            }
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage(e.getMessage());
            return true;
        }

        return true;
    }
}