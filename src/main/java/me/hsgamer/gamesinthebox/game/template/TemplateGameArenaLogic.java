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
package me.hsgamer.gamesinthebox.game.template;

import me.hsgamer.gamesinthebox.replacement.ReplacementHandler;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.minigamecore.base.Initializer;
import me.hsgamer.minigamecore.implementation.feature.TimerFeature;

import java.util.Collections;
import java.util.List;

/**
 * The game logic
 */
public abstract class TemplateGameArenaLogic implements Initializer, ReplacementHandler {
    /**
     * The arena
     */
    protected final TemplateGameArena arena;

    /**
     * Create a new game logic
     *
     * @param arena the arena
     */
    protected TemplateGameArenaLogic(TemplateGameArena arena) {
        this.arena = arena;
    }

    /**
     * Load the features for the arena
     *
     * @return the list of features
     */
    public List<Feature> loadFeatures() {
        return Collections.emptyList();
    }

    /**
     * Called when the admin forces the game to end
     */
    public void forceEnd() {
        // EMPTY
    }

    /**
     * Called when the game is starting the waiting state
     */
    public void onWaitingStart() {
        // EMPTY
    }

    /**
     * Called when the game is updating the waiting state
     */
    public void onWaitingUpdate() {
        // EMPTY
    }

    /**
     * Check if the waiting state is over
     *
     * @return true if the waiting state is over
     */
    public boolean isWaitingOver() {
        return arena.getFeature(TimerFeature.class).getDuration() <= 0;
    }

    /**
     * Called when the waiting state is over
     */
    public void onWaitingOver() {
        // EMPTY
    }

    /**
     * Called when the game is starting the in-game state
     */
    public void onInGameStart() {
        // EMPTY
    }

    /**
     * Called when the game is updating the in-game state
     */
    public void onInGameUpdate() {
        // EMPTY
    }

    /**
     * Check if the in-game state is over
     *
     * @return true if the in-game state is over
     */
    public boolean isInGameOver() {
        return arena.getFeature(TimerFeature.class).getDuration() <= 0;
    }

    /**
     * Called when the in-game state is over
     */
    public void onInGameOver() {
        // EMPTY
    }

    /**
     * Called when the game is starting the ending state
     */
    public void onEndingStart() {
        // EMPTY
    }

    /**
     * Called when the game is updating the ending state
     */
    public void onEndingUpdate() {
        // EMPTY
    }

    /**
     * Check if the ending state is over
     *
     * @return true if the ending state is over
     */
    public boolean isEndingOver() {
        return arena.getFeature(TimerFeature.class).getDuration() <= 0;
    }

    /**
     * Called when the ending state is over
     */
    public void onEndingOver() {
        // EMPTY
    }
}
