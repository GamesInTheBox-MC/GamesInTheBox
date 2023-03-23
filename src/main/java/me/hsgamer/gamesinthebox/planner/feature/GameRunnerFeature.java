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
package me.hsgamer.gamesinthebox.planner.feature;

import me.hsgamer.gamesinthebox.game.GameAction;
import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.minigamecore.base.Feature;

/**
 * The {@link Feature} that handles the operations of the {@link GameArena}
 */
public class GameRunnerFeature implements Feature {
    private GameArena currentGameArena;
    private boolean isFinished = true;

    /**
     * Get the current {@link GameArena}
     *
     * @return the current {@link GameArena}
     */
    public GameArena getCurrentGameArena() {
        return currentGameArena;
    }

    /**
     * Set the current {@link GameArena}
     *
     * @param currentGameArena the current {@link GameArena}
     */
    public void setCurrentGameArena(GameArena currentGameArena) {
        this.currentGameArena = currentGameArena;
    }

    /**
     * Start the current {@link GameArena}
     */
    public void start() {
        if (currentGameArena == null || !isFinished) return;
        currentGameArena.start();
        isFinished = false;
    }

    /**
     * End the current {@link GameArena}
     */
    public void end() {
        if (currentGameArena == null || isFinished) return;
        currentGameArena.end();
        isFinished = true;
    }

    /**
     * Get the {@link GameAction} of the current {@link GameArena}
     *
     * @return the {@link GameAction}
     */
    public GameAction getGameAction() {
        if (currentGameArena == null || isFinished) return GameAction.EMPTY;
        return currentGameArena.getGameAction();
    }

    /**
     * Mark the current {@link GameArena} as finished
     */
    public void setFinished() {
        isFinished = true;
    }

    /**
     * Check if the current {@link GameArena} is finished
     *
     * @return true if the current {@link GameArena} is finished
     */
    public boolean isFinished() {
        return isFinished;
    }
}
