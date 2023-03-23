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
package me.hsgamer.gamesinthebox.picker.impl;

import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.hscore.common.CollectionUtils;

import java.util.Collections;
import java.util.Map;

/**
 * The {@link me.hsgamer.gamesinthebox.picker.GamePicker} that picks the arena randomly.
 */
public class RandomGamePicker extends DelayedGamePicker {
    private Map<String, GameArena> arenaMap = Collections.emptyMap();

    /**
     * Create a new game picker
     *
     * @param planner the {@link Planner}
     */
    protected RandomGamePicker(Planner planner) {
        super(planner);
    }

    @Override
    public void setup(Map<String, GameArena> arenaMap) {
        this.arenaMap = arenaMap;
    }

    @Override
    protected GameArena pickArena() {
        return CollectionUtils.pickRandom(arenaMap.values());
    }

    @Override
    public boolean canPick() {
        return super.canPick() && !arenaMap.isEmpty();
    }
}
