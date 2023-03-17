package me.hsgamer.gamesinthebox.planner.feature;

import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.minigamecore.base.Feature;

import java.util.Collections;
import java.util.List;

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

    public List<String> getActions() {
        return currentGameArena == null ? Collections.emptyList() : currentGameArena.getActions();
    }

    public List<String> getActionArgs(String action, String... args) {
        return currentGameArena == null ? Collections.emptyList() : currentGameArena.getActionArgs(action, args);
    }

    public void performAction(String action, String... args) {
        if (currentGameArena == null) return;
        currentGameArena.performAction(action, args);
    }

    public void setFinished() {
        isFinished = true;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
