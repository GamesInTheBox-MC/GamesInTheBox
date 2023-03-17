package me.hsgamer.gamesinthebox.game;

import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.minigamecore.base.ArenaManager;

public abstract class Game extends ArenaManager {
    protected abstract GameArena newArena(String name, Planner planner);

    public abstract String getDisplayName();

    public final GameArena createArena(String name, Planner planner) {
        GameArena arena = newArena(name, planner);
        if (!addArena(arena)) {
            throw new IllegalArgumentException("Arena " + name + " already exists");
        }
        arena.init();
        arena.postInit();
        return arena;
    }
}
