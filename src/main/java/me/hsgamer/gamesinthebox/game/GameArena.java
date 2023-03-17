package me.hsgamer.gamesinthebox.game;

import me.hsgamer.gamesinthebox.game.feature.PlannerFeature;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.Feature;

import java.util.ArrayList;
import java.util.List;

public abstract class GameArena extends Arena {
    private final Planner planner;

    protected GameArena(String name, Game game, Planner planner) {
        super(name, game);
        this.planner = planner;
    }

    public Planner getPlanner() {
        return planner;
    }

    protected abstract List<Feature> loadExtraFeatures();

    public abstract void start();

    public abstract void forceEnd();

    public GameAction getGameAction() {
        return GameAction.EMPTY;
    }

    @Override
    protected List<Feature> loadFeatures() {
        List<Feature> features = new ArrayList<>();
        features.add(new PlannerFeature(planner));
        features.addAll(loadExtraFeatures());
        return features;
    }
}
