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
package me.hsgamer.gamesinthebox.game.feature;

import me.hsgamer.minigamecore.base.Feature;

/**
 * The {@link Feature} to handle ticks
 */
public abstract class TickFeature implements Feature {
    private long delayPerTicks;
    private long lastTick;

    /**
     * Create the delay per ticks
     *
     * @return the delay per ticks in milliseconds
     */
    protected abstract long createDelayPerTicks();

    @Override
    public void postInit() {
        this.delayPerTicks = createDelayPerTicks();
        this.lastTick = System.currentTimeMillis();
    }

    /**
     * Get the delay per ticks
     *
     * @return the delay per ticks in milliseconds
     */
    public long getDelayPerTicks() {
        return delayPerTicks;
    }

    /**
     * Get the passed ticks.
     * This will get the passed ticks since the last call of this method.
     *
     * @return the passed ticks
     */
    public int getPassedTicks() {
        long current = System.currentTimeMillis();
        long difference = current - lastTick;
        int passed = (int) (difference / delayPerTicks);
        long remaining = difference % delayPerTicks;
        lastTick = current - remaining;
        return passed;
    }

    /**
     * Reset the tick
     */
    public void reset() {
        this.lastTick = System.currentTimeMillis();
    }
}
