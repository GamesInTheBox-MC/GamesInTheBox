package me.hsgamer.gamesinthebox.util;

import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.extra.DisplayName;
import me.hsgamer.minigamecore.bukkit.extra.ColoredDisplayName;

public final class GameUtil {
    private GameUtil() {
        // EMPTY
    }

    public static String getState(Arena arena) {
        return arena.getCurrentStateInstance().map(gameState -> {
            if (gameState instanceof ColoredDisplayName) {
                return ((ColoredDisplayName) gameState).getColoredDisplayName();
            } else if (gameState instanceof DisplayName) {
                return ((DisplayName) gameState).getDisplayName();
            } else {
                return gameState.getClass().getSimpleName();
            }
        }).orElse("");
    }
}
