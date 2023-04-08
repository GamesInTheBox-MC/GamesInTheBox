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
package me.hsgamer.gamesinthebox.game.template.state;

import me.hsgamer.gamesinthebox.game.template.TemplateGameLogic;
import me.hsgamer.gamesinthebox.game.template.feature.CooldownFeature;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.bukkit.extra.ColoredDisplayName;

/**
 * The idling state.
 * The arena will be in this state while waiting for the {@link me.hsgamer.gamesinthebox.planner.Planner} to start the game.
 */
public class IdlingState implements GameState, ColoredDisplayName {
    private final TemplateGameLogic gameLogic;

    /**
     * Create a new state
     *
     * @param gameLogic the game logic
     */
    public IdlingState(TemplateGameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    @Override
    public void start(Arena arena) {
        arena.getFeature(CooldownFeature.class).setCanStart(false);
    }

    @Override
    public void update(Arena arena) {
        if (arena.getFeature(CooldownFeature.class).canStart()) {
            arena.setNextState(WaitingState.class);
        }
    }

    @Override
    public String getDisplayName() {
        return gameLogic.getGameMessageConfig().getStateIdle();
    }
}
