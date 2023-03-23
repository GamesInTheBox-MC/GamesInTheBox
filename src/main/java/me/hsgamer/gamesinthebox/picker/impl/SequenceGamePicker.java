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
import me.hsgamer.gamesinthebox.planner.feature.PlannerConfigFeature;
import me.hsgamer.hscore.common.CollectionUtils;

import java.util.*;

/**
 * The game picker that picks the game in a sequence.
 * The sequence is defined in {@link PlannerConfigFeature} with the key {@code pick-sequence}.
 * The value is a list of comma-separated arena names:
 * <pre>
 *     pick-sequence:
 *     - "arena1,arena2,arena3"
 *     - "arena4,arena5"
 *     - "arena6"
 * </pre>
 * For each line of the list, the game will pick one arena randomly from the list.
 */
public class SequenceGamePicker extends DelayedGamePicker {
    private final List<String> sequence;
    private final List<List<GameArena>> arenaListSequence = new ArrayList<>();
    private int index = 0;

    public SequenceGamePicker(Planner planner) {
        super(planner);
        this.sequence = Optional.ofNullable(planner.getFeature(PlannerConfigFeature.class))
                .map(feature -> feature.get("pick-sequence"))
                .map(CollectionUtils::createStringListFromObject)
                .orElseGet(Collections::emptyList);
    }

    @Override
    public void setup(Map<String, GameArena> arenaMap) {
        sequence.forEach(subSequence -> {
            String[] split = subSequence.split(",");
            List<GameArena> arenaList = new ArrayList<>();
            for (String arenaName : split) {
                GameArena gameArena = arenaMap.get(arenaName);
                if (gameArena != null) {
                    arenaList.add(gameArena);
                }
            }
            arenaListSequence.add(arenaList);
        });
    }

    @Override
    protected GameArena pickArena() {
        List<GameArena> arenaList = arenaListSequence.get(index);
        index = (index + 1) % arenaListSequence.size();
        return CollectionUtils.pickRandom(arenaList);
    }

    @Override
    public boolean canPick() {
        return super.canPick() && !arenaListSequence.isEmpty();
    }
}
