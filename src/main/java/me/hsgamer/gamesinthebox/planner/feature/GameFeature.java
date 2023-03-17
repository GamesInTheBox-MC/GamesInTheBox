package me.hsgamer.gamesinthebox.planner.feature;

import me.hsgamer.gamesinthebox.game.GameAction;
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

    public void start() {
        if (currentGameArena == null || !isFinished) return;
        currentGameArena.start();
        isFinished = false;
    }

    public void forceEnd() {
        if (currentGameArena == null || isFinished) return;
        currentGameArena.forceEnd();
        isFinished = true;
    }

    public GameAction getGameAction() {
        if (currentGameArena == null || isFinished) return GameAction.EMPTY;
        return currentGameArena.getGameAction();
    }

    public void setFinished() {
        isFinished = true;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
