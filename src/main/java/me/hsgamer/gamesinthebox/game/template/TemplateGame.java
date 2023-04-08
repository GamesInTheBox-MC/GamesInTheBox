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

public class TemplateGame extends SimpleGame {
    private final TemplateGameExpansion expansion;

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
