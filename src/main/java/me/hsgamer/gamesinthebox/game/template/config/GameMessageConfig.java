package me.hsgamer.gamesinthebox.game.template.config;

import me.hsgamer.hscore.config.annotation.ConfigPath;

public interface GameMessageConfig {
    @ConfigPath("point.plus")
    default String getPointPlus() {
        return "&a+{point} point(s) &7({total})";
    }

    @ConfigPath("point.minus")
    default String getPointMinus() {
        return "&c-{point} point(s) &7({total})";
    }

    @ConfigPath("state.waiting")
    default String getStateWaiting() {
        return "Waiting";
    }

    @ConfigPath("state.in-game")
    default String getStateInGame() {
        return "In Game";
    }

    @ConfigPath("state.ending")
    default String getStateEnding() {
        return "Ending";
    }

    @ConfigPath("state.idle")
    default String getStateIdle() {
        return "Idle";
    }

    @ConfigPath("start-broadcast")
    default String getStartBroadcast() {
        return "&aThe game has started!";
    }

    @ConfigPath("end-broadcast")
    default String getEndBroadcast() {
        return "&aThe game has ended!";
    }

    void reloadConfig();
}
