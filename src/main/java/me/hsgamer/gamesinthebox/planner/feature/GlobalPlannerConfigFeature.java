package me.hsgamer.gamesinthebox.planner.feature;

import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.minigamecore.base.Feature;

public class GlobalPlannerConfigFeature implements Feature {
    private final Config plannerConfig;

    public GlobalPlannerConfigFeature(Config plannerConfig) {
        this.plannerConfig = plannerConfig;
    }

    public Config getPlannerConfig() {
        return plannerConfig;
    }

    public PlannerConfigFeature createPlannerFeature(Planner planner) {
        return new PlannerConfigFeature(planner, this);
    }
}
