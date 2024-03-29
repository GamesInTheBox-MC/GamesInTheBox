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
package me.hsgamer.gamesinthebox.picker;

import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.hscore.common.StringReplacer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * The game picker that picks a {@link GameArena}
 */
public interface GamePicker extends StringReplacer {
    /**
     * The game picker that does nothing
     */
    GamePicker EMPTY = new GamePicker() {
        @Override
        public String replace(@NotNull String input) {
            return null;
        }

        @Override
        public void setup(@NotNull Map<String, GameArena> arenaMap) {
            // EMPTY
        }

        @Override
        public GameArena pick() {
            return null;
        }

        @Override
        public boolean canPick() {
            return false;
        }
    };

    /**
     * Set up the game picker
     *
     * @param arenaMap the map of arena name and arena
     */
    void setup(@NotNull Map<@NotNull String, @NotNull GameArena> arenaMap);

    /**
     * Pick a {@link GameArena}
     *
     * @return the picked {@link GameArena} or null if it cannot pick
     */
    @Nullable
    GameArena pick();

    /**
     * Check if the game picker can pick
     *
     * @return true if the game picker can pick
     */
    boolean canPick();

    /**
     * Force the game picker to pick
     *
     * @return true if the game picker can force pick
     */
    default boolean forcePick() {
        return false;
    }
}
