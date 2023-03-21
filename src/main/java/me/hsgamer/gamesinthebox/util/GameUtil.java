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

import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.extra.DisplayName;
import me.hsgamer.minigamecore.bukkit.extra.ColoredDisplayName;

public final class GameUtil {
    private GameUtil() {
        // EMPTY
    }

    public static String getState(Arena arena) {
        return arena.getCurrentStateInstance().map(gameState -> {
            if (gameState instanceof ColoredDisplayName) {
                return ((ColoredDisplayName) gameState).getColoredDisplayName();
            } else if (gameState instanceof DisplayName) {
                return ((DisplayName) gameState).getDisplayName();
            } else {
                return gameState.getClass().getSimpleName();
            }
        }).orElse("");
    }

    public static String replaceName(String input, Arena arena) {
        if (arena instanceof Planner) {
            return input.replace("{planner}", arena.getName());
        } else if (arena instanceof GameArena) {
            return input
                    .replace("{planner}", ((GameArena) arena).getPlanner().getName())
                    .replace("{arena}", ((GameArena) arena).getLocalName());
        } else {
            return input.replace("{arena}", arena.getName());
        }
    }
}
