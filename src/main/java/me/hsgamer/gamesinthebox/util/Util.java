/*
   Copyright 2023-2023 Huynh Tien

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
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

/**
 * The common utilities
 */
public final class Util {
    private Util() {
        // EMPTY
    }

    /**
     * Create a section from a map
     *
     * @param values the map
     * @return the section
     */
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

    /**
     * Cancel a task safely
     *
     * @param task the task
     */
    public static void cancelSafe(BukkitTask task) {
        if (task != null) {
            try {
                task.cancel();
            } catch (Exception ignored) {
                // IGNORED
            }
        }
    }

    /**
     * Get a random colorized string from a collection
     *
     * @param collection   the collection
     * @param defaultValue the default value
     * @return the random colorized string
     */
    public static String getRandomColorizedString(Collection<String> collection, String defaultValue) {
        String s = Optional.ofNullable(CollectionUtils.pickRandom(collection)).orElse(defaultValue);
        return ColorUtils.colorize(s);
    }
}
