package me.hsgamer.gamesinthebox.manager;

import me.hsgamer.gamesinthebox.planner.state.IdlingState;
import me.hsgamer.gamesinthebox.planner.state.ListeningState;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.implementation.manager.LoadedArenaManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlannerManager extends LoadedArenaManager {
    @Override
    protected List<Arena> loadArenas() {
        return null;
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
        return Collections.emptyList();
    }
}
