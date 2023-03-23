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
package me.hsgamer.gamesinthebox.planner;

import me.hsgamer.gamesinthebox.game.Game;
import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.manager.PlannerManager;
import me.hsgamer.gamesinthebox.planner.feature.*;
import me.hsgamer.gamesinthebox.planner.state.IdlingState;
import me.hsgamer.gamesinthebox.replacement.ReplacementHandler;
import me.hsgamer.gamesinthebox.util.GameUtil;
import me.hsgamer.hscore.bukkit.utils.ColorUtils;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.minigamecore.bukkit.SimpleBukkitArena;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The planner. It will pick a game and run it.
 * Provided replacement queries:
 * <ul>
 *     <li>{@code name}: the name of the planner</li>
 *     <li>{@code state}: the state of the planner</li>
 *     <li>{@code game_name}: the display name of the current game</li>
 *     <li>{@code game_local_name}: the local name of the current game</li>
 *     <li>{@code game_state}: the state of the planner</li>
 * </ul>
 */
public class Planner extends SimpleBukkitArena implements ReplacementHandler {
    public Planner(String name, PlannerManager arenaManager) {
        super(name, arenaManager);
    }

    @Override
    protected List<Feature> loadFeatures() {
        return Arrays.asList(
                new GameRunnerFeature(),
                new GamePickerFeature(this),
                new ReplacementFeature(this),
                new VariableFeature(this),
                new PlannerFeature(this),
                getFeature(GlobalPlannerConfigFeature.class).createPlannerFeature(this)
        );
    }

    @Override
    protected void postInitArena() {
        setNextState(IdlingState.class);
    }

    @Override
    public String replace(String input) {
        switch (input.toLowerCase()) {
            case "name":
                return getName();
            case "game_name":
                return Optional.ofNullable(getFeature(GameRunnerFeature.class))
                        .map(GameRunnerFeature::getCurrentGameArena)
                        .map(GameArena::getGame)
                        .map(Game::getDisplayName)
                        .map(ColorUtils::colorize)
                        .orElse("");
            case "game_local_name":
                return Optional.ofNullable(getFeature(GameRunnerFeature.class))
                        .map(GameRunnerFeature::getCurrentGameArena)
                        .map(GameArena::getLocalName)
                        .orElse("");
            case "state":
                return GameUtil.getState(this);
            case "game_state":
                return Optional.ofNullable(getFeature(GameRunnerFeature.class))
                        .map(GameRunnerFeature::getCurrentGameArena)
                        .map(GameUtil::getState)
                        .orElse("");
            default:
                return null;
        }
    }

    @Override
    public long getDelay() {
        return getFeature(PluginFeature.class).getPlugin().getMainConfig().getPlannerInterval();
    }

    @Override
    public long getPeriod() {
        return getFeature(PluginFeature.class).getPlugin().getMainConfig().getPlannerInterval();
    }

    @Override
    public boolean isAsync() {
        return getFeature(PluginFeature.class).getPlugin().getMainConfig().isPlannerAsync();
    }
}
