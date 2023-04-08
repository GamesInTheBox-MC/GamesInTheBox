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

import me.hsgamer.gamesinthebox.game.simple.feature.SimpleUpdateFeature;
import me.hsgamer.gamesinthebox.game.template.TemplateGameArenaLogic;
import me.hsgamer.gamesinthebox.game.template.TemplateGameExpansion;
import me.hsgamer.gamesinthebox.game.template.feature.ArenaLogicFeature;
import me.hsgamer.gamesinthebox.game.template.feature.CooldownFeature;
import me.hsgamer.gamesinthebox.planner.feature.PlannerFeature;
import me.hsgamer.gamesinthebox.planner.feature.VariableFeature;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.bukkit.extra.ColoredDisplayName;
import org.bukkit.Bukkit;

/**
 * The ending state.
 * The arena will be in this state when the game is over.
 */
public class EndingState implements GameState, ColoredDisplayName {
    private final TemplateGameExpansion expansion;

    /**
     * Create a new ending state
     *
     * @param expansion the expansion
     */
    public EndingState(TemplateGameExpansion expansion) {
        this.expansion = expansion;
    }

    @Override
    public void start(Arena arena) {
        String endMessage = arena.getFeature(VariableFeature.class).replace(expansion.getGameMessageConfig().getEndBroadcast());
        Bukkit.getOnlinePlayers().forEach(player -> MessageUtils.sendMessage(player, endMessage));
        arena.getFeature(ArenaLogicFeature.class).getArenaLogic().onEndingStart();
        arena.getFeature(CooldownFeature.class).start(this);
    }

    @Override
    public void update(Arena arena) {
        TemplateGameArenaLogic arenaLogic = arena.getFeature(ArenaLogicFeature.class).getArenaLogic();
        if (arenaLogic.isEndingOver()) {
            arena.setNextState(IdlingState.class);
        } else {
            arenaLogic.onEndingUpdate();
        }
    }

    @Override
    public void end(Arena arena) {
        arena.getFeature(ArenaLogicFeature.class).getArenaLogic().onEndingOver();
        arena.getFeature(SimpleUpdateFeature.class).clearState();
        arena.getFeature(PlannerFeature.class).notifyFinished();
    }

    @Override
    public String getDisplayName() {
        return expansion.getGameMessageConfig().getStateEnding();
    }
}
