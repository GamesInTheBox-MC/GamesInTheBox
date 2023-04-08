package me.hsgamer.gamesinthebox.game.template;

import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.minigamecore.base.Initializer;
import me.hsgamer.minigamecore.implementation.feature.TimerFeature;

import java.util.Collections;
import java.util.List;

public abstract class TemplateGameArenaLogic implements Initializer {
    protected final SimpleGameArena arena;

    protected TemplateGameArenaLogic(SimpleGameArena arena) {
        this.arena = arena;
    }

    public List<Feature> loadFeatures() {
        return Collections.emptyList();
    }

    public void forceEnd() {
        // EMPTY
    }

    public void onWaitingStart() {
        // EMPTY
    }

    public void onWaitingUpdate() {
        // EMPTY
    }

    public boolean isWaitingOver() {
        return arena.getFeature(TimerFeature.class).getDuration() <= 0;
    }

    public void onWaitingOver() {
        // EMPTY
    }

    public void onInGameStart() {
        // EMPTY
    }

    public void onInGameUpdate() {
        // EMPTY
    }

    public boolean isInGameOver() {
        return arena.getFeature(TimerFeature.class).getDuration() <= 0;
    }

    public void onInGameOver() {
        // EMPTY
    }

    public void onEndingStart() {
        // EMPTY
    }

    public void onEndingUpdate() {
        // EMPTY
    }

    public boolean isEndingOver() {
        return arena.getFeature(TimerFeature.class).getDuration() <= 0;
    }

    public void onEndingOver() {
        // EMPTY
    }
}
