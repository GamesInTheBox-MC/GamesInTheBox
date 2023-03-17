package me.hsgamer.gamesinthebox.game.feature;

import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.gamesinthebox.planner.feature.GameFeature;
import me.hsgamer.gamesinthebox.planner.state.IdlingState;
import me.hsgamer.minigamecore.base.Feature;

public class PlannerFeature implements Feature {
    private final Planner planner;

    public PlannerFeature(Planner planner) {
        this.planner = planner;
    }

    public void notifyFinished() {
        planner.getFeature(GameFeature.class).setFinished(true);
    }

    public boolean isIdle() {
        return planner.getCurrentState() == IdlingState.class;
    }
}
