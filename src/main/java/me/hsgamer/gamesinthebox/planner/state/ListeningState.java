package me.hsgamer.gamesinthebox.planner.state;

import me.hsgamer.gamesinthebox.planner.feature.GameFeature;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.GameState;

public class ListeningState implements GameState {
    @Override
    public void start(Arena arena) {
        arena.getFeature(GameFeature.class).start();
    }

    @Override
    public void update(Arena arena) {
        if (arena.getFeature(GameFeature.class).isFinished()) {
            arena.setNextState(IdlingState.class);
        }
    }
}
