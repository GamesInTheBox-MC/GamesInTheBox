package me.hsgamer.gamesinthebox.game.template.config;

import me.hsgamer.hscore.config.annotation.ConfigPath;

public interface GameConfig {
    @ConfigPath("interval")
    default long getInterval() {
        return 20;
    }

    @ConfigPath("async")
    default boolean isAsync() {
        return true;
    }

    void reloadConfig();
}
