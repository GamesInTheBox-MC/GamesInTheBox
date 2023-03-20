package me.hsgamer.gamesinthebox.planner.state;

import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.planner.feature.GameFeature;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.bukkit.extra.ColoredDisplayName;

public class ListeningState implements GameState, ColoredDisplayName {
    private final GamesInTheBox plugin;

    public ListeningState(GamesInTheBox plugin) {
        this.plugin = plugin;
    }

    @Override
    public void start(Arena arena) {
        arena.getFeature(GameFeature.class).start();
    }

    @Override
    public void update(Arena arena) {
        if (arena.getFeature(GameFeature.class).isFinished()) {
            arena.setNextState(IdlingState.class);
        }
    }

    @Override
    public String getDisplayName() {
        return plugin.getMessageConfig().getPlannerStateListening();
    }
}
