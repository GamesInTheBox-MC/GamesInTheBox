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
package me.hsgamer.gamesinthebox.manager;

import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.hscore.bukkit.expansion.BukkitConfigExpansionDescriptionLoader;
import me.hsgamer.hscore.bukkit.utils.BukkitUtils;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.MapUtils;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.expansion.common.ExpansionClassLoader;
import me.hsgamer.hscore.expansion.common.ExpansionManager;
import me.hsgamer.hscore.expansion.common.ExpansionState;
import me.hsgamer.hscore.expansion.common.exception.InvalidExpansionDescriptionException;
import me.hsgamer.hscore.expansion.extra.manager.DependableExpansionSortAndFilter;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The manager that {@link me.hsgamer.hscore.expansion.common.Expansion}.
 * It will load all expansions in the folder "expansions" in the plugin's data folder.
 * The expansion jar should have the "expansion.yml" in the root folder that includes:
 * <ul>
 *     <li>{@code name}: the name of the expansion</li>
 *     <li>{@code version}: the version of the expansion</li>
 *     <li>{@code main}: the main class of the expansion</li>
 *     <li>{@code depend}: the list of expansions that the expansion depends on (optional)</li>
 *     <li>{@code softdepend}: the list of expansions that the expansion soft depends on (optional)</li>
 *     <li>{@code plugin-depend}: the list of plugins that the expansion depends on</li>
 *     <li>{@code authors}: the list of authors of the expansion (optional)</li>
 *     <li>{@code description}: the description of the expansion (optional)</li>
 * </ul>
 */
public class PluginExpansionManager extends ExpansionManager {
    private final GamesInTheBox plugin;

    public PluginExpansionManager(GamesInTheBox plugin) {
        super(new File(plugin.getDataFolder(), "expansions"), new BukkitConfigExpansionDescriptionLoader("expansion.yml"), plugin.getClass().getClassLoader());
        this.plugin = plugin;

        setSortAndFilterFunction(new DependableExpansionSortAndFilter() {
            @Override
            public List<String> getDependencies(ExpansionClassLoader loader) {
                return CollectionUtils.createStringListFromObject(MapUtils.getIfFound(loader.getDescription().getData(), "depend", "depends", "dependencies"));
            }

            @Override
            public List<String> getSoftDependencies(ExpansionClassLoader loader) {
                return CollectionUtils.createStringListFromObject(MapUtils.getIfFound(loader.getDescription().getData(), "softdepend", "softdepends", "soft-dependencies"));
            }
        });
        addStateListener((loader, state) -> {
            if (state == ExpansionState.LOADING) {
                checkPluginDepends(loader);
            }
        });
    }

    /**
     * Get the authors of the loader
     *
     * @param loader the loader
     * @return the authors
     */
    public static List<String> getAuthors(ExpansionClassLoader loader) {
        Object value = MapUtils.getIfFound(loader.getDescription().getData(), "authors", "author");
        return CollectionUtils.createStringListFromObject(value, true);
    }

    /**
     * Get the description of the loader
     *
     * @param loader the loader
     * @return the description
     */
    public static String getDescription(ExpansionClassLoader loader) {
        Object value = loader.getDescription().getData().get("description");
        return Objects.toString(value, "");
    }

    private static List<String> getPluginDepends(ExpansionClassLoader loader) {
        Object value = MapUtils.getIfFound(loader.getDescription().getData(), "plugin-depend", "plugin", "plugin-depends", "plugins");
        return CollectionUtils.createStringListFromObject(value, true);
    }

    private void checkPluginDepends(ExpansionClassLoader loader) {
        List<String> requiredPlugins = getPluginDepends(loader);
        if (Validate.isNullOrEmpty(requiredPlugins)) return;

        List<String> missing = BukkitUtils.getMissingDepends(requiredPlugins);
        if (!missing.isEmpty()) {
            throw new InvalidExpansionDescriptionException("Missing plugin dependency for " + loader.getDescription().getName() + ": " + Arrays.toString(missing.toArray()));
        }
    }

    public GamesInTheBox getPlugin() {
        return plugin;
    }
}
