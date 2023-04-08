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
package me.hsgamer.gamesinthebox.game.template.feature;

import me.hsgamer.gamesinthebox.game.feature.GameConfigFeature;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.gamesinthebox.game.template.state.EndingState;
import me.hsgamer.gamesinthebox.game.template.state.InGameState;
import me.hsgamer.gamesinthebox.game.template.state.WaitingState;
import me.hsgamer.gamesinthebox.util.TimeUtil;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.implementation.feature.TimerFeature;

import java.util.Optional;

/**
 * The {@link Feature} to handle the cooldown time of the template {@link GameState}
 */
public class CooldownFeature implements Feature {
    private final SimpleGameArena arena;
    private long waitingTime = 60000L;
    private long inGameTime = 300000L;
    private long endingTime = 10000L;
    private boolean canStart = false;

    /**
     * Create a new {@link CooldownFeature}
     *
     * @param arena the {@link SimpleGameArena}
     */
    public CooldownFeature(SimpleGameArena arena) {
        this.arena = arena;
    }

    @Override
    public void postInit() {
        GameConfigFeature configFeature = arena.getFeature(GameConfigFeature.class);

        waitingTime = Optional.ofNullable(configFeature.getString("time.waiting"))
                .map(TimeUtil::parseMillis)
                .orElse(waitingTime);
        inGameTime = Optional.ofNullable(configFeature.getString("time.in-game"))
                .map(TimeUtil::parseMillis)
                .orElse(inGameTime);
        endingTime = Optional.ofNullable(configFeature.getString("time.ending"))
                .map(TimeUtil::parseMillis)
                .orElse(endingTime);
    }

    /**
     * Start the timer of the {@link GameState}
     *
     * @param gameState the {@link GameState}
     */
    public void start(GameState gameState) {
        TimerFeature timerFeature = arena.getFeature(TimerFeature.class);
        if (gameState instanceof WaitingState) {
            timerFeature.setDuration(waitingTime);
        } else if (gameState instanceof InGameState) {
            timerFeature.setDuration(inGameTime);
        } else if (gameState instanceof EndingState) {
            timerFeature.setDuration(endingTime);
        }
    }

    /**
     * Set the value to indicate that the game can start
     *
     * @param canStart the value
     */
    public void setCanStart(boolean canStart) {
        this.canStart = canStart;
    }

    /**
     * Check if the game can start
     *
     * @return true if the game can start
     */
    public boolean canStart() {
        return canStart;
    }

    /**
     * Get the waiting time
     *
     * @return the waiting time
     */
    public long getWaitingTime() {
        return waitingTime;
    }

    /**
     * Get the in-game time
     *
     * @return the in-game time
     */
    public long getInGameTime() {
        return inGameTime;
    }

    /**
     * Get the ending time
     *
     * @return the ending time
     */
    public long getEndingTime() {
        return endingTime;
    }
}
