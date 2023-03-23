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

public class GameRunnerFeature implements Feature {
    private GameArena currentGameArena;
    private boolean isFinished = true;

    public GameArena getCurrentGameArena() {
        return currentGameArena;
    }

    public void setCurrentGameArena(GameArena currentGameArena) {
        this.currentGameArena = currentGameArena;
    }

    public void start() {
        if (currentGameArena == null || !isFinished) return;
        currentGameArena.start();
        isFinished = false;
    }

    public void forceEnd() {
        if (currentGameArena == null || isFinished) return;
        currentGameArena.forceEnd();
        isFinished = true;
    }

    public GameAction getGameAction() {
        if (currentGameArena == null || isFinished) return GameAction.EMPTY;
        return currentGameArena.getGameAction();
    }

    public void setFinished() {
        isFinished = true;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
