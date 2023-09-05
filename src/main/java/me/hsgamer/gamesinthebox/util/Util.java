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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
    @NotNull
    public static ConfigurationSection createSection(@NotNull Map<String, Object> values) {
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
     * Handle a section and return the values of it
     *
     * @param consumer the consumer
     * @return the values of the section
     */
    public static Map<String, Object> handleSection(Consumer<ConfigurationSection> consumer) {
        ConfigurationSection section = new MemoryConfiguration();
        consumer.accept(section);
        return section.getValues(false);
    }

    /**
     * Get a random colorized string from a collection
     *
     * @param collection   the collection
     * @param defaultValue the default value
     * @return the random colorized string
     */
    @NotNull
    public static String getRandomColorizedString(@NotNull Collection<@NotNull String> collection, @NotNull String defaultValue) {
        String s = Optional.ofNullable(CollectionUtils.pickRandom(collection)).orElse(defaultValue);
        return ColorUtils.colorize(s);
    }

    /**
     * Get a filtered list from a list
     *
     * @param list      the list
     * @param filter    the filter
     * @param predicate the predicate with 2 parameters: the element and the filter
     * @param <T>       the type of the element in the list
     * @return the filtered list
     */
    @NotNull
    public static <T> List<T> getFilteredList(@NotNull List<T> list, @Nullable T filter, @NotNull BiPredicate<T, T> predicate) {
        return filter == null
                ? Collections.unmodifiableList(list)
                : list.stream().filter(element -> predicate.test(element, filter)).collect(Collectors.toList());
    }

    /**
     * Get a filtered string list from a list
     *
     * @param list      the list
     * @param filter    the filter
     * @param predicate the predicate with 2 parameters: the element and the filter
     * @return the filtered list
     */
    @NotNull
    public static List<String> getFilteredList(@NotNull List<String> list, @Nullable String filter, @NotNull BiPredicate<String, String> predicate) {
        return filter == null || filter.isEmpty()
                ? Collections.unmodifiableList(list)
                : list.stream().filter(element -> predicate.test(element, filter)).collect(Collectors.toList());
    }

    /**
     * Get a filtered string list from a list.
     * The filter is case-insensitive.
     *
     * @param list   the list
     * @param filter the filter
     * @return the filtered list
     */
    @NotNull
    public static List<String> getFilteredList(@NotNull List<String> list, @Nullable String filter) {
        return getFilteredList(list, filter, (s, s2) -> s.toLowerCase(Locale.ROOT).contains(s2.toLowerCase(Locale.ROOT)));
    }
}
