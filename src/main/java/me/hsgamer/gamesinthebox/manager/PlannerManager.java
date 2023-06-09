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
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.gamesinthebox.planner.feature.GlobalPlannerConfigFeature;
import me.hsgamer.gamesinthebox.planner.feature.PluginFeature;
import me.hsgamer.gamesinthebox.planner.state.IdlingState;
import me.hsgamer.gamesinthebox.planner.state.ListeningState;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.implementation.manager.LoadedArenaManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The manager for all {@link Planner}
 */
public class PlannerManager extends LoadedArenaManager {
    private final GamesInTheBox plugin;
    private final File plannerFolder;

    /**
     * Create a new manager
     *
     * @param plugin the plugin
     */
    public PlannerManager(@NotNull GamesInTheBox plugin) {
        this.plugin = plugin;
        this.plannerFolder = new File(plugin.getDataFolder(), "planners");
    }

    @Override
    public void init() {
        if (!plannerFolder.exists()) {
            plannerFolder.mkdirs();
        }
        super.init();
    }

    @Override
    protected List<Arena> loadArenas() {
        return getFeature(GlobalPlannerConfigFeature.class).getPlannerNames().stream()
                .map(name -> new Planner(name, this))
                .collect(Collectors.toList());
    }

    @Override
    protected List<GameState> loadGameStates() {
        return Arrays.asList(
                new IdlingState(plugin),
                new ListeningState(plugin)
        );
    }

    @Override
    protected List<Feature> loadFeatures() {
        return Arrays.asList(
                new GlobalPlannerConfigFeature(plannerFolder),
                new PluginFeature(plugin)
        );
    }
}
