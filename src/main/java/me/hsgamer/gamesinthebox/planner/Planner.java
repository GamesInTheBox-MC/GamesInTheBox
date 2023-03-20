package me.hsgamer.gamesinthebox.planner;

import me.hsgamer.gamesinthebox.game.Game;
import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.manager.PlannerManager;
import me.hsgamer.gamesinthebox.planner.feature.*;
import me.hsgamer.gamesinthebox.planner.state.IdlingState;
import me.hsgamer.gamesinthebox.replacement.ReplacementHandler;
import me.hsgamer.gamesinthebox.util.GameUtil;
import me.hsgamer.hscore.bukkit.utils.ColorUtils;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.minigamecore.bukkit.SimpleBukkitArena;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Planner extends SimpleBukkitArena implements ReplacementHandler {
    public Planner(String name, PlannerManager arenaManager) {
        super(name, arenaManager);
    }

    @Override
    protected List<Feature> loadFeatures() {
        return Arrays.asList(
                new GameFeature(),
                new PickFeature(this),
                new ReplacementFeature(this),
                getFeature(GlobalPlannerConfigFeature.class).createPlannerFeature(this)
        );
    }

    @Override
    protected void postInitArena() {
        setNextState(IdlingState.class);
    }

    @Override
    public String replace(String input) {
        switch (input.toLowerCase()) {
            case "name":
                return getName();
            case "game_name":
                return Optional.ofNullable(getFeature(GameFeature.class))
                        .map(GameFeature::getCurrentGameArena)
                        .map(GameArena::getGame)
                        .map(Game::getDisplayName)
                        .map(ColorUtils::colorize)
                        .orElse("");
            case "game_local_name":
                return Optional.ofNullable(getFeature(GameFeature.class))
                        .map(GameFeature::getCurrentGameArena)
                        .map(GameArena::getLocalName)
                        .orElse("");
            case "state":
                return GameUtil.getState(this);
            case "game_state":
                return Optional.ofNullable(getFeature(GameFeature.class))
                        .map(GameFeature::getCurrentGameArena)
                        .map(GameUtil::getState)
                        .orElse("");
            default:
                return null;
        }
    }

    @Override
    public long getDelay() {
        return getFeature(PluginFeature.class).getPlugin().getMainConfig().getPlannerInterval();
    }

    @Override
    public long getPeriod() {
        return getFeature(PluginFeature.class).getPlugin().getMainConfig().getPlannerInterval();
    }

    @Override
    public boolean isAsync() {
        return getFeature(PluginFeature.class).getPlugin().getMainConfig().isPlannerAsync();
    }
}
