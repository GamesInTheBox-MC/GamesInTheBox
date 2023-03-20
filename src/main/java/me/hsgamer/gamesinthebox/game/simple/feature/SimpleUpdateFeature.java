package me.hsgamer.gamesinthebox.game.simple.feature;

import me.hsgamer.gamesinthebox.game.feature.PointFeature;
import me.hsgamer.gamesinthebox.game.feature.TopFeature;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.minigamecore.base.Feature;

public class SimpleUpdateFeature implements Feature {
    private final SimpleGameArena arena;

    public SimpleUpdateFeature(SimpleGameArena arena) {
        this.arena = arena;
    }

    public void initState() {
        arena.getFeature(DescriptiveHologramFeature.class).initHologram();
    }

    public void updateState() {
        arena.getFeature(PointFeature.class).takeTopSnapshot();
        arena.getFeature(TopFeature.class).setTop(arena.getFeature(PointFeature.class).getTopSnapshotAsStringPair());
        arena.getFeature(DescriptiveHologramFeature.class).updateHologram();
    }

    public void clearState() {
        arena.getFeature(PointFeature.class).clearPoints();
        arena.getFeature(DescriptiveHologramFeature.class).clearHologram();
    }
}
