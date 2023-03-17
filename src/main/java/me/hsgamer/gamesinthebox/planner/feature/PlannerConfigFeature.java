package me.hsgamer.gamesinthebox.planner.feature;

import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.minigamecore.base.Feature;

import java.util.Map;
import java.util.Objects;

public class PlannerConfigFeature implements Feature {
    private final Planner planner;
    private final GlobalPlannerConfigFeature globalPlannerConfigFeature;

    public PlannerConfigFeature(Planner planner, GlobalPlannerConfigFeature globalPlannerConfigFeature) {
        this.planner = planner;
        this.globalPlannerConfigFeature = globalPlannerConfigFeature;
    }

    public <T> T getInstance(String path, T def, Class<T> clazz) {
        return globalPlannerConfigFeature.getPlannerConfig().getInstance(planner.getName() + "." + path, def, clazz);
    }

    public String getString(String path, String def) {
        return Objects.toString(globalPlannerConfigFeature.getPlannerConfig().getNormalized(planner.getName() + "." + path, def));
    }

    public Object get(String path) {
        return globalPlannerConfigFeature.getPlannerConfig().get(planner.getName() + "." + path);
    }

    public Map<String, Object> getValues(String path, boolean deep) {
        return globalPlannerConfigFeature.getPlannerConfig().getNormalizedValues(planner.getName() + "." + path, deep);
    }

    public boolean contains(String path) {
        return globalPlannerConfigFeature.getPlannerConfig().contains(planner.getName() + "." + path);
    }

    public void set(String path, Object value) {
        globalPlannerConfigFeature.getPlannerConfig().set(planner.getName() + "." + path, value);
        globalPlannerConfigFeature.getPlannerConfig().save();
    }
}
