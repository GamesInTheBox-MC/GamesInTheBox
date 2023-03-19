package me.hsgamer.gamesinthebox.planner;

import me.hsgamer.gamesinthebox.manager.PlannerManager;
import me.hsgamer.gamesinthebox.planner.feature.GameFeature;
import me.hsgamer.gamesinthebox.planner.feature.GlobalPlannerConfigFeature;
import me.hsgamer.gamesinthebox.planner.feature.PickFeature;
import me.hsgamer.gamesinthebox.planner.state.IdlingState;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.minigamecore.bukkit.SimpleBukkitArena;

import java.util.Arrays;
import java.util.List;

public class Planner extends SimpleBukkitArena {
    public Planner(String name, PlannerManager arenaManager) {
        super(name, arenaManager);
    }

    @Override
    protected List<Feature> loadFeatures() {
        return Arrays.asList(
                new GameFeature(),
                new PickFeature(this),
                getFeature(GlobalPlannerConfigFeature.class).createPlannerFeature(this)
        );
    }

    @Override
    protected void postInitArena() {
        setNextState(IdlingState.class);
    }
}
