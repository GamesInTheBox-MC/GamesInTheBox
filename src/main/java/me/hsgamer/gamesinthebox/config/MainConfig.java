package me.hsgamer.gamesinthebox.config;

import me.hsgamer.hscore.config.annotation.ConfigPath;

public interface MainConfig {
    @ConfigPath("planner.interval")
    default long getPlannerInterval() {
        return 20L;
    }

    @ConfigPath("planner.async")
    default boolean isPlannerAsync() {
        return true;
    }

    void reloadConfig();
}
