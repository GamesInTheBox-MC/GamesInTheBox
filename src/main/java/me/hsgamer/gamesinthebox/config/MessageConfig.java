package me.hsgamer.gamesinthebox.config;

import me.hsgamer.hscore.config.annotation.ConfigPath;

public interface MessageConfig {
    @ConfigPath("prefix")
    default String getPrefix() {
        return "&6[&eGamesInTheBox&6] &f";
    }

    @ConfigPath("success")
    default String getSuccess() {
        return "&aSuccess";
    }

    @ConfigPath("planner.not-found")
    default String getPlannerNotFound() {
        return "&cPlanner not found";
    }

    @ConfigPath("game.cannot-set")
    default String getGameCannotSet() {
        return "&cCannot set game for planner";
    }

    @ConfigPath("game.cannot-perform-action")
    default String getGameCannotPerformAction() {
        return "&cCannot perform action for planner";
    }

    void reloadConfig();
}
