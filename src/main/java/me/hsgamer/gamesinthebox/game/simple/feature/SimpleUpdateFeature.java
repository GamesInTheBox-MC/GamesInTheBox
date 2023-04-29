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
package me.hsgamer.gamesinthebox.game.simple.feature;

import me.hsgamer.gamesinthebox.game.feature.TopFeature;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.gamesinthebox.planner.feature.PluginFeature;
import me.hsgamer.hscore.bukkit.scheduler.Scheduler;
import me.hsgamer.hscore.bukkit.scheduler.Task;
import me.hsgamer.minigamecore.base.Feature;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

/**
 * The {@link Feature} that updates the arena
 */
public class SimpleUpdateFeature implements Feature {
    private final Plugin plugin;
    private final SimpleGameArena arena;
    private Task task;

    /**
     * Create a new {@link SimpleUpdateFeature}
     *
     * @param arena the arena
     */
    public SimpleUpdateFeature(@NotNull SimpleGameArena arena) {
        this.plugin = arena.getFeature(PluginFeature.class).getPlugin();
        this.arena = arena;
    }

    /**
     * Initialize the state
     */
    public void initState() {
        arena.getFeature(DescriptiveHologramFeature.class).initHologram();
        task = Scheduler.plugin(plugin).async().runTaskTimer(this::updateState, 0L, 20L);
    }

    /**
     * Update the state
     */
    private void updateState() {
        arena.getFeature(TopFeature.class).setTop(arena.getFeature(SimplePointFeature.class).getTopAsStringPair());
        arena.getFeature(DescriptiveHologramFeature.class).updateHologram();
    }

    /**
     * Clear the state
     */
    public void clearState() {
        if (task != null) {
            task.cancel();
        }
        arena.getFeature(SimplePointFeature.class).clearPoints();
        arena.getFeature(TopFeature.class).setTop(Collections.emptyList());
        arena.getFeature(DescriptiveHologramFeature.class).clearHologram();
    }

    @Override
    public void clear() {
        if (task != null) {
            task.cancel();
        }
    }
}
