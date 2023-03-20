package me.hsgamer.gamesinthebox.planner.state;

import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.planner.feature.GameFeature;
import me.hsgamer.gamesinthebox.planner.feature.PickFeature;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.bukkit.extra.ColoredDisplayName;

public class IdlingState implements GameState, ColoredDisplayName {
    private final GamesInTheBox plugin;

    public IdlingState(GamesInTheBox plugin) {
        this.plugin = plugin;
    }

    @Override
    public void update(Arena arena) {
        if (arena.getFeature(PickFeature.class).canPick()) {
            arena.setNextState(ListeningState.class);
        }
    }

    @Override
    public void end(Arena arena) {
        GameArena gameArena = arena.getFeature(PickFeature.class).getNextGame();
        arena.getFeature(GameFeature.class).setCurrentGameArena(gameArena);
    }

    @Override
    public String getDisplayName() {
        return plugin.getMessageConfig().getPlannerStateIdling();
    }
}
