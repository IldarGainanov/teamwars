package circus.teamwars.commands;

import circus.teamwars.Teams;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class AddTeam implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
                             @NotNull String title, @NotNull String[] args)
    {
        if (args.length != 2) {
            return false;
        }

        ChatColor color;
        try {
            color = ChatColor.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage("Unknown color");
            return true;
        }

        try {

            Teams.createTeam(args[0], color);
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage(e.getMessage());
            return true;
        }


        return true;
    }
}
