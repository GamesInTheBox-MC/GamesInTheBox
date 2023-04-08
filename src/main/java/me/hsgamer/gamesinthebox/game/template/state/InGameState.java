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

import me.hsgamer.gamesinthebox.game.template.TemplateGameArenaLogic;
import me.hsgamer.gamesinthebox.game.template.TemplateGameLogic;
import me.hsgamer.gamesinthebox.game.template.feature.ArenaLogicFeature;
import me.hsgamer.gamesinthebox.game.template.feature.CooldownFeature;
import me.hsgamer.gamesinthebox.planner.feature.VariableFeature;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.bukkit.extra.ColoredDisplayName;
import org.bukkit.Bukkit;

/**
 * The in-game state.
 * The arena will be in this state when the game is running.
 */
public class InGameState implements GameState, ColoredDisplayName {
    private final TemplateGameLogic gameLogic;

    /**
     * Create a new state
     *
     * @param gameLogic the game logic
     */
    public InGameState(TemplateGameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    @Override
    public void start(Arena arena) {
        String startMessage = arena.getFeature(VariableFeature.class).replace(gameLogic.getGameMessageConfig().getStartBroadcast());
        Bukkit.getOnlinePlayers().forEach(player -> MessageUtils.sendMessage(player, startMessage));
        arena.getFeature(ArenaLogicFeature.class).getArenaLogic().onInGameStart();
        arena.getFeature(CooldownFeature.class).start(this);
    }

    @Override
    public void update(Arena arena) {
        TemplateGameArenaLogic arenaLogic = arena.getFeature(ArenaLogicFeature.class).getArenaLogic();
        if (arenaLogic.isInGameOver()) {
            arena.setNextState(EndingState.class);
        } else {
            arenaLogic.onInGameUpdate();
        }
    }

    @Override
    public void end(Arena arena) {
        arena.getFeature(ArenaLogicFeature.class).getArenaLogic().onInGameOver();
    }

    @Override
    public String getDisplayName() {
        return gameLogic.getGameMessageConfig().getStateInGame();
    }
}
