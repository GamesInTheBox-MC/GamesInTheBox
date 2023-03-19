package me.hsgamer.gamesinthebox.game;

import me.hsgamer.gamesinthebox.game.feature.GameConfigFeature;
import me.hsgamer.gamesinthebox.game.feature.PlannerFeature;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.gamesinthebox.replacement.ReplacementHandler;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.Feature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class GameArena extends Arena implements ReplacementHandler {
    private final String localName;
    private final Planner planner;

    protected GameArena(String name, Game game, Planner planner) {
        super(planner.getName() + "-" + name, game);
        this.localName = name;
        this.planner = planner;
    }

    public Planner getPlanner() {
        return planner;
    }

    protected List<Feature> loadExtraFeatures() {
        return Collections.emptyList();
    }

    public abstract void start();

    public abstract void forceEnd();

    public GameAction getGameAction() {
        return GameAction.EMPTY;
    }

    @Override
    protected final List<Feature> loadFeatures() {
        List<Feature> features = new ArrayList<>();
        features.add(new PlannerFeature(planner));
        features.add(new GameConfigFeature(localName, this));
        features.addAll(loadExtraFeatures());
        return features;
    }
}
