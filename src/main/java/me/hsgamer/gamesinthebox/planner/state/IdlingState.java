package me.hsgamer.gamesinthebox.planner.state;

import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.planner.feature.GameFeature;
import me.hsgamer.gamesinthebox.planner.feature.PickFeature;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.implementation.feature.TimerFeature;

public class IdlingState implements GameState {
    @Override
    public void start(Arena arena) {
        long currentTime = System.currentTimeMillis();
        long nextGameTime = arena.getFeature(PickFeature.class).getNextGameTime();
        arena.getFeature(TimerFeature.class).setDuration(nextGameTime - currentTime);
    }

    @Override
    public void update(Arena arena) {
        if (arena.getFeature(TimerFeature.class).getDuration() <= 0) {
            arena.setNextState(ListeningState.class);
        }
    }

    @Override
    public void end(Arena arena) {
        GameArena gameArena = arena.getFeature(PickFeature.class).getNextGame();
        arena.getFeature(GameFeature.class).setCurrentGameArena(gameArena);
    }
}
