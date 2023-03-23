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
import me.hsgamer.gamesinthebox.picker.GamePicker;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.gamesinthebox.planner.feature.PlannerConfigFeature;
import me.hsgamer.gamesinthebox.util.TimeUtil;

import java.util.Optional;

/**
 * The {@link GamePicker} with the delay between picks.
 * The delay can be configured in the {@link PlannerConfigFeature} with the key "pick-delay".
 * This also provides the replacement query "time_left" to get the time left before the next pick.
 */
public abstract class DelayedGamePicker implements GamePicker {
    protected final Planner planner;
    protected final long delay;
    private boolean isPicked = false;
    private long nextPickTime;

    /**
     * Create a new game picker
     *
     * @param planner the {@link Planner}
     */
    protected DelayedGamePicker(Planner planner) {
        this.planner = planner;
        this.delay = Optional.ofNullable(planner.getFeature(PlannerConfigFeature.class))
                .map(feature -> feature.getString("pick-delay", "0"))
                .map(TimeUtil::parseMillis)
                .orElse(0L);
        updateNextPickTime();
    }

    private void updateNextPickTime(long currentTime) {
        nextPickTime = currentTime + delay;
    }

    private void updateNextPickTime() {
        updateNextPickTime(System.currentTimeMillis());
    }

    /**
     * Pick an arena
     *
     * @return the picked arena
     */
    protected abstract GameArena pickArena();

    @Override
    public final GameArena pick() {
        isPicked = true;
        return pickArena();
    }

    @Override
    public boolean canPick() {
        long currentTime = System.currentTimeMillis();
        if (isPicked) {
            isPicked = false;
            updateNextPickTime(currentTime);
            return false;
        }
        return currentTime >= nextPickTime;
    }

    @Override
    public boolean forcePick() {
        if (isPicked) {
            return false;
        } else {
            nextPickTime = System.currentTimeMillis();
            return true;
        }
    }

    @Override
    public String replace(String input) {
        if (input.equalsIgnoreCase("time_left")) {
            return TimeUtil.formatStandardTime(Math.max(0, nextPickTime - System.currentTimeMillis()));
        }
        return null;
    }
}
