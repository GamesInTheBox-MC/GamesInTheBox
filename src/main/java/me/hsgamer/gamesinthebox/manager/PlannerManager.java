package me.hsgamer.gamesinthebox.manager;

import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.gamesinthebox.planner.feature.GlobalPlannerConfigFeature;
import me.hsgamer.gamesinthebox.planner.feature.PluginFeature;
import me.hsgamer.gamesinthebox.planner.state.IdlingState;
import me.hsgamer.gamesinthebox.planner.state.ListeningState;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.implementation.manager.LoadedArenaManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlannerManager extends LoadedArenaManager {
    private final GamesInTheBox plugin;
    private final Config plannerConfig;

    public PlannerManager(GamesInTheBox plugin) {
        this.plugin = plugin;
        this.plannerConfig = new BukkitConfig(plugin, "planner.yml");
    }

    @Override
    public void init() {
        plannerConfig.setup();
        super.init();
    }

    @Override
    protected List<Arena> loadArenas() {
        return plannerConfig.getKeys(false).stream()
                .map(name -> new Planner(name, this))
                .collect(Collectors.toList());
    }

    @Override
    public void reloadArena() {
        plannerConfig.reload();
        super.reloadArena();
    }

    @Override
    protected List<GameState> loadGameStates() {
        return Arrays.asList(
                new IdlingState(),
                new ListeningState()
        );
    }

    @Override
    protected List<Feature> loadFeatures() {
        return Arrays.asList(
                new GlobalPlannerConfigFeature(plannerConfig),
                new PluginFeature(plugin)
        );
    }
}
