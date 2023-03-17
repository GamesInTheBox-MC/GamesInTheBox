package me.hsgamer.gamesinthebox.planner.feature;

import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.minigamecore.base.Feature;

public class GameFeature implements Feature {
    private GameArena currentGameArena;
    private boolean isFinished = false;

    public GameArena getCurrentGameArena() {
        return currentGameArena;
    }

    public void setCurrentGameArena(GameArena currentGameArena) {
        this.currentGameArena = currentGameArena;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
