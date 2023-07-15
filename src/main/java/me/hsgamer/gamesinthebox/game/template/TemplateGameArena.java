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

import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArenaAction;
import me.hsgamer.gamesinthebox.game.simple.feature.SimpleUpdateFeature;
import me.hsgamer.gamesinthebox.game.template.feature.ArenaLogicFeature;
import me.hsgamer.gamesinthebox.game.template.feature.CooldownFeature;
import me.hsgamer.gamesinthebox.game.template.state.IdlingState;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.gamesinthebox.planner.feature.VariableFeature;
import me.hsgamer.gamesinthebox.util.ActionBarUtil;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.minigamecore.base.Feature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The {@link SimpleGameArena} for template
 */
public class TemplateGameArena extends SimpleGameArena {
    private final TemplateGameLogic gameLogic;

    /**
     * Create a new arena
     *
     * @param gameLogic the game logic
     * @param name      the name
     * @param game      the game
     * @param planner   the planner
     */
    public TemplateGameArena(TemplateGameLogic gameLogic, @NotNull String name, @NotNull TemplateGame game, @NotNull Planner planner) {
        super(name, game, planner);
        this.gameLogic = gameLogic;
    }

    @Override
    protected void onPointChanged(@NotNull UUID uuid, int point, int totalPoints) {
        if (point == 0) return;

        boolean isNegative = point < 0;
        int absPoint = Math.abs(point);
        boolean isSingular = absPoint == 1;

        String message;
        if (isSingular) {
            message = isNegative ? gameLogic.getGameMessageConfig().getPointMinusSingular() : gameLogic.getGameMessageConfig().getPointPlusSingular();
        } else {
            message = isNegative ? gameLogic.getGameMessageConfig().getPointMinusPlural() : gameLogic.getGameMessageConfig().getPointPlusPlural();
        }

        String finalMessage = message
                .replace("{point}", Integer.toString(absPoint))
                .replace("{total}", Integer.toString(totalPoints));

        ActionBarUtil.sendActionBar(uuid, finalMessage);
    }

    @Override
    protected void onFailedToReward(@NotNull List<UUID> uuids) {
        String notEnoughPlayerMessage = getFeature(VariableFeature.class).replace(gameLogic.getGameMessageConfig().getNotEnoughPlayerToReward());
        for (UUID uuid : uuids) {
            MessageUtils.sendMessage(uuid, notEnoughPlayerMessage);
        }
    }

    @Override
    public @NotNull List<@NotNull String> getDefaultHologramLines(@NotNull String name) {
        return gameLogic.getDefaultHologramLines(name);
    }

    @Override
    protected List<Feature> loadFeatures() {
        List<Feature> features = super.loadFeatures();

        features.add(new CooldownFeature(this));

        TemplateGameArenaLogic templateGameArenaLogic = gameLogic.createArenaLogic(this);
        features.add(new ArenaLogicFeature(templateGameArenaLogic));
        features.addAll(templateGameArenaLogic.loadFeatures());

        return features;
    }

    @Override
    public @Nullable String replace(@NotNull String input) {
        String output = super.replace(input);
        if (output == null) {
            output = Optional.ofNullable(getFeature(ArenaLogicFeature.class))
                    .map(ArenaLogicFeature::getArenaLogic)
                    .map(arenaLogic -> arenaLogic.replace(input))
                    .orElse(null);
        }
        return output;
    }

    @Override
    public @Nullable String replace(@NotNull String input, @NotNull UUID uuid) {
        String output = super.replace(input, uuid);
        if (output == null) {
            output = Optional.ofNullable(getFeature(ArenaLogicFeature.class))
                    .map(ArenaLogicFeature::getArenaLogic)
                    .map(arenaLogic -> arenaLogic.replace(input, uuid))
                    .orElse(null);
        }
        return output;
    }

    @Override
    protected @NotNull SimpleGameArenaAction createAction() {
        return gameLogic.getArenaAction(this);
    }

    @Override
    protected void postInitArena() {
        setNextState(IdlingState.class);
    }

    @Override
    public void start() {
        getFeature(CooldownFeature.class).setCanStart(true);
    }

    @Override
    public void end() {
        setNextState(IdlingState.class);
        getFeature(ArenaLogicFeature.class).getArenaLogic().forceEnd();
        getFeature(SimpleUpdateFeature.class).clearState();
    }

    @Override
    public long getDelay() {
        return gameLogic.getGameConfig().getInterval();
    }

    @Override
    public long getPeriod() {
        return gameLogic.getGameConfig().getInterval();
    }

    @Override
    public boolean isAsync() {
        return gameLogic.getGameConfig().isAsync();
    }
}
