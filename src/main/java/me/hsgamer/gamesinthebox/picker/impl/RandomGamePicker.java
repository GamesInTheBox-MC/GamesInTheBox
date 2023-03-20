package me.hsgamer.gamesinthebox.picker.impl;

import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.hscore.common.CollectionUtils;

import java.util.Collections;
import java.util.Map;

public class RandomGamePicker extends DelayedGamePicker {
    private Map<String, GameArena> arenaMap = Collections.emptyMap();

    public RandomGamePicker(Planner planner) {
        super(planner);
    }

    @Override
    public void setup(Map<String, GameArena> arenaMap) {
        this.arenaMap = arenaMap;
    }

    @Override
    protected GameArena pickArena() {
        return CollectionUtils.pickRandom(arenaMap.values());
    }
}
