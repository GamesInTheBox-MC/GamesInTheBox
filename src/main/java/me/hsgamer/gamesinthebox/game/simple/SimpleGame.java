package me.hsgamer.gamesinthebox.game.simple;

import me.hsgamer.gamesinthebox.game.Game;
import me.hsgamer.gamesinthebox.game.feature.HologramFeature;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.minigamecore.base.Feature;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleGame extends Game {
    @Override
    protected abstract SimpleGameArena newArena(String name, Planner planner);

    @Override
    protected List<Feature> loadFeatures() {
        List<Feature> features = new ArrayList<>();
        features.add(new HologramFeature(getClass().getSimpleName()));
        return features;
    }
}
