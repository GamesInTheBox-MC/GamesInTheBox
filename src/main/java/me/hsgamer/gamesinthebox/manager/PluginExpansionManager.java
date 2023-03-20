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
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.MapUtils;
import me.hsgamer.hscore.expansion.common.ExpansionClassLoader;
import me.hsgamer.hscore.expansion.common.ExpansionManager;
import me.hsgamer.hscore.expansion.extra.manager.DependableExpansionSortAndFilter;

import java.io.File;
import java.util.List;

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
    }

    public GamesInTheBox getPlugin() {
        return plugin;
    }
}
