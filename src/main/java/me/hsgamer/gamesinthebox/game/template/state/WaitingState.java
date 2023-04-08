package me.hsgamer.gamesinthebox.game.template.state;

import me.hsgamer.gamesinthebox.game.simple.feature.SimpleUpdateFeature;
import me.hsgamer.gamesinthebox.game.template.TemplateGameArenaLogic;
import me.hsgamer.gamesinthebox.game.template.TemplateGameExpansion;
import me.hsgamer.gamesinthebox.game.template.feature.ArenaLogicFeature;
import me.hsgamer.gamesinthebox.game.template.feature.CooldownFeature;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.bukkit.extra.ColoredDisplayName;

public class WaitingState implements GameState, ColoredDisplayName {
    private final TemplateGameExpansion expansion;

    public WaitingState(TemplateGameExpansion expansion) {
        this.expansion = expansion;
    }

    @Override
    public void start(Arena arena) {
        arena.getFeature(SimpleUpdateFeature.class).initState();
        arena.getFeature(ArenaLogicFeature.class).getArenaLogic().onWaitingStart();
        arena.getFeature(CooldownFeature.class).start(this);
    }

    @Override
    public void update(Arena arena) {
        TemplateGameArenaLogic arenaLogic = arena.getFeature(ArenaLogicFeature.class).getArenaLogic();
        if (arenaLogic.isWaitingOver()) {
            arena.setNextState(InGameState.class);
        } else {
            arenaLogic.onWaitingUpdate();
        }
    }

    @Override
    public void end(Arena arena) {
        arena.getFeature(ArenaLogicFeature.class).getArenaLogic().onWaitingOver();
    }

    @Override
    public String getDisplayName() {
        return expansion.getGameMessageConfig().getStateWaiting();
    }
}
