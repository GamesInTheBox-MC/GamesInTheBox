package me.hsgamer.gamesinthebox.game.template.state;

import me.hsgamer.gamesinthebox.game.template.TemplateGameExpansion;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.bukkit.extra.ColoredDisplayName;

public class IdlingState implements GameState, ColoredDisplayName {
    private final TemplateGameExpansion expansion;

    public IdlingState(TemplateGameExpansion expansion) {
        this.expansion = expansion;
    }

    @Override
    public String getDisplayName() {
        return expansion.getGameMessageConfig().getStateIdle();
    }
}
