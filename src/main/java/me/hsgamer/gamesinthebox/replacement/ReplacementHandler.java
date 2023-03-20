package me.hsgamer.gamesinthebox.replacement;

import org.bukkit.OfflinePlayer;

public interface ReplacementHandler {
    ReplacementHandler EMPTY = s -> null;

    String replace(String input);

    default String replace(OfflinePlayer player, String input) {
        return replace(input);
    }
}
