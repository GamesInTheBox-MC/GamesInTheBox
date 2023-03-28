package me.hsgamer.gamesinthebox.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The utility for tasks
 */
public final class TaskUtil {
    private TaskUtil() {
        // EMPTY
    }

    /**
     * Cancel a task safely
     *
     * @param task the task
     */
    public static void cancelSafe(@Nullable BukkitTask task) {
        if (task != null) {
            try {
                task.cancel();
            } catch (Exception ignored) {
                // IGNORED
            }
        }
    }

    /**
     * Run a task synchronously
     *
     * @param runnable the runnable
     */
    public static void runSync(@NotNull Runnable runnable) {
        if (Bukkit.isPrimaryThread()) {
            runnable.run();
        } else {
            Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(Util.class), runnable);
        }
    }
}
