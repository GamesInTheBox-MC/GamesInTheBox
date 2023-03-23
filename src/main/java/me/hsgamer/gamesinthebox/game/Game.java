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
package me.hsgamer.gamesinthebox.game;

import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.minigamecore.base.ArenaManager;
import org.jetbrains.annotations.ApiStatus;

/**
 * The game.
 * This class is used to create new arenas for {@link Planner}.
 * This is also an {@link ArenaManager} to handle the registration of arenas, and include the {@link me.hsgamer.minigamecore.base.Feature} and {@link me.hsgamer.minigamecore.base.GameState} specialized in the game.
 */
public abstract class Game extends ArenaManager {
    /**
     * Create a new arena
     *
     * @param name    the name of the arena
     * @param planner the planner that the arena belongs to
     * @return the new arena
     */
    protected abstract GameArena newArena(String name, Planner planner);

    /**
     * Get the display name of the game
     *
     * @return the display name
     */
    public abstract String getDisplayName();

    /**
     * Get the {@link GameEditor} of the game.
     * Override this method to provide the {@link GameEditor} for the game that the admin can use to edit the game.
     *
     * @return the {@link GameEditor}
     */
    public GameEditor getEditor() {
        return GameEditor.EMPTY;
    }

    /**
     * Create a new arena.
     * This method will be called by the {@link Planner} when creating a new arena.
     *
     * @param name    the name of the arena
     * @param planner the planner that the arena belongs to
     * @return the new arena
     */
    @ApiStatus.Internal
    public final GameArena createArena(String name, Planner planner) {
        GameArena arena = newArena(name, planner);
        if (!addArena(arena)) {
            throw new IllegalArgumentException("Arena " + name + " already exists");
        }
        arena.postInit();
        return arena;
    }
}
