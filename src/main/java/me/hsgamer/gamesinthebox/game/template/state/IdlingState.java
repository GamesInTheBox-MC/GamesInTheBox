package me.hsgamer.gamesinthebox.game.template.state;

import me.hsgamer.gamesinthebox.game.template.TemplateGameExpansion;
import me.hsgamer.gamesinthebox.game.template.feature.CooldownFeature;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.bukkit.extra.ColoredDisplayName;

public class IdlingState implements GameState, ColoredDisplayName {
    private final TemplateGameExpansion expansion;

    public IdlingState(TemplateGameExpansion expansion) {
        this.expansion = expansion;
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
        return expansion.getGameMessageConfig().getStateIdle();
    }
}
