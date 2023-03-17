package me.hsgamer.gamesinthebox.planner.feature;

import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.minigamecore.base.Feature;

public class PluginFeature implements Feature {
    private final GamesInTheBox plugin;

    public PluginFeature(GamesInTheBox plugin) {
        this.plugin = plugin;
    }

    public GamesInTheBox getPlugin() {
        return plugin;
    }
}
