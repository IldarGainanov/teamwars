package circus.teamwars;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.World;

import java.util.*;

public class WorldBorder {
    int index;
    private static List<Integer> borderSizes;
    private static List<Integer> borderTimes;
    private static List<Integer> borderShrinkTimes;

    public WorldBorder() {
        index = 0;
        borderSizes = PluginManager.borderSizes();
        borderTimes = PluginManager.borderTimes();
        borderShrinkTimes = PluginManager.borderShrinkTimes();
    }

    public void start() {
        PluginManager.server().getWorlds().forEach(world -> world.getWorldBorder().setSize(borderSizes.get(0)));
        run();
    }

    private void run() {
        index++;
        PluginManager.server().broadcast(Component.text(String.format("Border will start to shrink to %dx%d in %d seconds!",
                        borderSizes.get(index),
                        borderSizes.get(index),
                        borderTimes.get(index - 1)))
                .color(TextColor.color(255, 0, 0)));

        PluginManager.server().getScheduler().runTaskLater(PluginManager.instance(), () -> {
                    PluginManager.server().getWorlds()
                            .stream()
                            .forEach(world -> world.getWorldBorder()
                                    .setSize(borderSizes.get(index), borderShrinkTimes.get(index - 1)));

                    PluginManager.server().broadcast(Component.text(String.format("Border will shrink to %dx%d in %d seconds!",
                                    borderSizes.get(index),
                                    borderSizes.get(index),
                                    borderShrinkTimes.get(index - 1)))
                            .color(TextColor.color(255, 0, 0)));

                    if (index == borderTimes.size()) {
                        return;
                    }

                    PluginManager.server().getScheduler().runTaskLater(PluginManager.instance(), this::run,
                            20 * borderShrinkTimes.get(index - 1));
                },
                20 * borderTimes.get(index - 1));
    }
}
