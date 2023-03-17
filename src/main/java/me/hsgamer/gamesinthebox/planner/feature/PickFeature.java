package me.hsgamer.gamesinthebox.planner.feature;

import me.hsgamer.minigamecore.base.Feature;

public class PickFeature implements Feature {
    private final long getNextGameTime = System.currentTimeMillis(); // TODO

    public long getGetNextGameTime() {
        return getNextGameTime;
    }
}
