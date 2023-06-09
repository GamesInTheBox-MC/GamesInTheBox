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
package me.hsgamer.gamesinthebox.planner.state;

import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.planner.feature.GamePickerFeature;
import me.hsgamer.gamesinthebox.planner.feature.GameRunnerFeature;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.bukkit.extra.ColoredDisplayName;
import org.jetbrains.annotations.NotNull;

/**
 * The {@link GameState} when the planner is idling
 */
public class IdlingState implements GameState, ColoredDisplayName {
    private final GamesInTheBox plugin;

    /**
     * Create a new {@link GameState}
     *
     * @param plugin the plugin
     */
    public IdlingState(@NotNull GamesInTheBox plugin) {
        this.plugin = plugin;
    }

    @Override
    public void update(Arena arena) {
        if (arena.getFeature(GamePickerFeature.class).canPick()) {
            arena.setNextState(ListeningState.class);
        }
    }

    @Override
    public void end(Arena arena) {
        GameArena gameArena = arena.getFeature(GamePickerFeature.class).getNextGame();
        arena.getFeature(GameRunnerFeature.class).setCurrentGameArena(gameArena);
    }

    @Override
    public String getDisplayName() {
        return plugin.getMessageConfig().getPlannerStateIdling();
    }
}
