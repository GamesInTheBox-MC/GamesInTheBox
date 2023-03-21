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
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.implementation.manager.LoadedArenaManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlannerManager extends LoadedArenaManager {
    private final GamesInTheBox plugin;
    private final Config plannerConfig;

    public PlannerManager(GamesInTheBox plugin) {
        this.plugin = plugin;
        this.plannerConfig = new BukkitConfig(plugin, "planner.yml");
    }

    @Override
    public void init() {
        plannerConfig.setup();
        super.init();
    }

    @Override
    protected List<Arena> loadArenas() {
        return plannerConfig.getKeys(false).stream()
                .map(name -> new Planner(name, this))
                .collect(Collectors.toList());
    }

    @Override
    public void reloadArena() {
        plannerConfig.reload();
        super.reloadArena();
        getAllArenas().forEach(Arena::postInit);
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
                new GlobalPlannerConfigFeature(plannerConfig),
                new PluginFeature(plugin)
        );
    }
}
