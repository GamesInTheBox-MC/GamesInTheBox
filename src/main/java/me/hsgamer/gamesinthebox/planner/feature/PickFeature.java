package me.hsgamer.gamesinthebox.planner.feature;

import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.minigamecore.base.Feature;

import java.util.HashMap;
import java.util.Map;

public class PickFeature implements Feature {
    private final Map<String, GameArena> gameArenaMap = new HashMap<>();
    private final long getNextGameTime = System.currentTimeMillis(); // TODO

    public long getGetNextGameTime() {
        return getNextGameTime;
    }

    @Override
    public void postInit() {
        // TODO: load GameArena from config
    }

    public GameArena getNextGame() {
        if (gameArenaMap.isEmpty()) {
            return null;
        }
        return null; // TODO
    }
}
