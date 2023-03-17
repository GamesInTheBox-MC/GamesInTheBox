package me.hsgamer.gamesinthebox.planner.state;

import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.planner.feature.GameFeature;
import me.hsgamer.gamesinthebox.planner.feature.PickFeature;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.GameState;

public class IdlingState implements GameState {
    @Override
    public void update(Arena arena) {
        if (arena.getFeature(PickFeature.class).getGetNextGameTime() <= System.currentTimeMillis()) {
            arena.setNextState(ListeningState.class);
        }
    }

    @Override
    public void end(Arena arena) {
        GameArena gameArena = arena.getFeature(PickFeature.class).getNextGame();
        arena.getFeature(GameFeature.class).setCurrentGameArena(gameArena);
    }
}
