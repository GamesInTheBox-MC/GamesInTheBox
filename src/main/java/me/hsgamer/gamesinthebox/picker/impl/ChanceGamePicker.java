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

import com.lewdev.probabilitylib.ProbabilityCollection;
import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.gamesinthebox.planner.feature.PlannerConfigFeature;

import java.util.*;

/**
 * The {@link me.hsgamer.gamesinthebox.picker.GamePicker} that picks the arena by chance.
 * The chance is defined in the {@link PlannerConfigFeature} with the key {@code pick-chance}.
 * The value of the {@code pick-chance} should be like this:
 * <pre>
 *     pick-chance:
 *       arena1: 10
 *       arena2: 20
 *       arena3: 30
 *       arena4: 40
 * </pre>
 * Where the key is the name of the arena and the value is the chance (in numbers).
 * The chance of the arena is calculated by the formula {@code chance / total chance}.
 */
public class ChanceGamePicker extends DelayedGamePicker {
    private final Map<String, Integer> chanceMap;
    private final ProbabilityCollection<GameArena> arenaCollection = new ProbabilityCollection<>();

    /**
     * Create a new game picker
     *
     * @param planner the {@link Planner}
     */
    public ChanceGamePicker(Planner planner) {
        super(planner);
        this.chanceMap = Optional.ofNullable(planner.getFeature(PlannerConfigFeature.class))
                .map(feature -> feature.getValues("pick-chance", false))
                .map(map -> {
                    Map<String, Integer> numberMap = new HashMap<>();
                    map.forEach((key, value) -> {
                        int chance;
                        try {
                            chance = Integer.parseInt(Objects.toString(value));
                        } catch (Exception e) {
                            return;
                        }
                        numberMap.put(key, chance);
                    });
                    return numberMap;
                })
                .orElseGet(Collections::emptyMap);
    }

    @Override
    public void setup(Map<String, GameArena> arenaMap) {
        chanceMap.forEach((key, value) -> {
            GameArena arena = arenaMap.get(key);
            if (arena != null) {
                arenaCollection.add(arena, value);
            }
        });
    }

    @Override
    protected GameArena pickArena() {
        return arenaCollection.get();
    }

    @Override
    public boolean canPick() {
        return super.canPick() && !arenaCollection.isEmpty();
    }
}
