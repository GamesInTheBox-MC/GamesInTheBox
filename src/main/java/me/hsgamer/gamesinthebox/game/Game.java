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

public abstract class Game extends ArenaManager {
    protected abstract GameArena newArena(String name, Planner planner);

    public abstract String getDisplayName();

    public final GameArena createArena(String name, Planner planner) {
        GameArena arena = newArena(name, planner);
        if (!addArena(arena)) {
            throw new IllegalArgumentException("Arena " + name + " already exists");
        }
        arena.postInit();
        return arena;
    }
}
