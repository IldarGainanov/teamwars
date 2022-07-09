package circus.teamwars;

import circus.teamwars.commands.*;
import circus.teamwars.listeners.PlayerLogin;
import circus.teamwars.listeners.PlayerListener;
import circus.teamwars.listeners.WorldLoadListener;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class PluginManager extends JavaPlugin {

    private static PluginManager instance;
    private static Server server;

    @Override
    public void onEnable() {
        instance = this;
        server = getServer();
        org.bukkit.plugin.PluginManager pm = getServer().getPluginManager();

        Objects.requireNonNull(getCommand("addteam")).setExecutor(new AddTeam());
        Objects.requireNonNull(getCommand("remteam")).setExecutor(new RemoveTeam());
        Objects.requireNonNull(getCommand("addmember")).setExecutor(new AddMember());
        Objects.requireNonNull(getCommand("remmember")).setExecutor(new RemoveMember());
        Objects.requireNonNull(getCommand("setvip")).setExecutor(new SetVIP());
        Objects.requireNonNull(getCommand("setstate")).setExecutor(new SetState());
        Objects.requireNonNull(getCommand("startgame")).setExecutor(new StartGame());

        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new WorldLoadListener(), this);
        pm.registerEvents(new PlayerLogin(), this);

        TeamController.initScoreboard();
    }

    public static PluginManager instance() {
        return instance;
    }

    public static Server server() {
        return server;
    }

}
