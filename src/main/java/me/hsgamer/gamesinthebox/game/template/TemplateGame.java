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

import me.hsgamer.gamesinthebox.game.simple.SimpleGame;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameEditor;
import me.hsgamer.gamesinthebox.game.template.state.EndingState;
import me.hsgamer.gamesinthebox.game.template.state.IdlingState;
import me.hsgamer.gamesinthebox.game.template.state.InGameState;
import me.hsgamer.gamesinthebox.game.template.state.WaitingState;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.minigamecore.base.GameState;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * The {@link SimpleGame} for template
 */
public class TemplateGame extends SimpleGame {
    private final TemplateGameExpansion expansion;

    /**
     * Create a new game
     *
     * @param expansion the expansion
     */
    public TemplateGame(TemplateGameExpansion expansion) {
        this.expansion = expansion;
    }

    @Override
    public @NotNull String getDisplayName() {
        return expansion.getDisplayName();
    }

    @Override
    public @NotNull SimpleGameEditor createEditor() {
        return expansion.getEditor(this);
    }

    @Override
    protected @NotNull SimpleGameArena newArena(@NotNull String name, @NotNull Planner planner) {
        return new TemplateGameArena(expansion, name, this, planner);
    }

    @Override
    protected List<Feature> loadFeatures() {
        List<Feature> features = super.loadFeatures();
        features.addAll(expansion.getGameFeatures(this));
        return features;
    }

    @Override
    protected List<GameState> loadGameStates() {
        return Arrays.asList(
                new IdlingState(expansion),
                new WaitingState(expansion),
                new InGameState(expansion),
                new EndingState(expansion)
        );
    }
}
