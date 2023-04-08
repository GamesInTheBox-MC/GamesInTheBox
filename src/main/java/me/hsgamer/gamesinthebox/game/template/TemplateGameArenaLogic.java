package me.hsgamer.gamesinthebox.game.template;

import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.minigamecore.base.Initializer;

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

    public abstract void forceEnd();

    public abstract void start();

    public abstract void end();

    public abstract void onWaitingStart();

    public abstract boolean isWaitingOver();

    public abstract void onWaitingOver();

    public abstract void onInGameStart();

    public abstract boolean isInGameOver();

    public abstract void onInGameOver();

    public abstract void onEndingStart();

    public abstract boolean isEndingOver();

    public abstract void onEndingOver();
}
