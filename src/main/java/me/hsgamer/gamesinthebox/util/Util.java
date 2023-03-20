package me.hsgamer.gamesinthebox.util;

import me.hsgamer.hscore.bukkit.utils.ColorUtils;
import me.hsgamer.hscore.common.CollectionUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class Util {
    private Util() {
        // EMPTY
    }

    public static ConfigurationSection createSection(Map<String, Object> values) {
        Map<String, Object> finalValues = new HashMap<>(values);
        finalValues.replaceAll((key, value) -> {
            if (value instanceof Map) {
                Map<String, Object> valueMap = new HashMap<>();
                ((Map<?, ?>) value).forEach((k, v) -> valueMap.put(k.toString(), v));
                return createSection(valueMap);
            }
            return value;
        });
        MemoryConfiguration section = new MemoryConfiguration();
        section.addDefaults(values);
        return section;
    }

    public static void cancelSafe(BukkitTask task) {
        if (task != null) {
            try {
                task.cancel();
            } catch (Exception ignored) {
                // IGNORED
            }
        }
    }

    public static String getRandomColorizedString(Collection<String> collection, String defaultValue) {
        String s = Optional.ofNullable(CollectionUtils.pickRandom(collection)).orElse(defaultValue);
        return ColorUtils.colorize(s);
    }
}
