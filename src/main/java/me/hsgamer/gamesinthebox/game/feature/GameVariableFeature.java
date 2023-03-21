package me.hsgamer.gamesinthebox.game.feature;

import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.gamesinthebox.planner.feature.ReplacementFeature;
import me.hsgamer.hscore.variable.InstanceVariableManager;
import me.hsgamer.minigamecore.base.Feature;

import java.util.Optional;
import java.util.UUID;

public class GameVariableFeature implements Feature {
    private static final UUID DUMMY_UUID = UUID.randomUUID();
    private final InstanceVariableManager variableManager;

    public GameVariableFeature(Planner planner) {
        this.variableManager = new InstanceVariableManager();
        variableManager.register("", (original, uuid) -> Optional.ofNullable(planner.getFeature(ReplacementFeature.class))
                .map(replacementFeature -> replacementFeature.replace(original))
                .orElse(null));
        variableManager.setReplaceAll(true);
    }

    public String replace(String input, UUID uuid) {
        return variableManager.setVariables(input, uuid);
    }

    public String replace(String input) {
        return replace(input, DUMMY_UUID);
    }
}
