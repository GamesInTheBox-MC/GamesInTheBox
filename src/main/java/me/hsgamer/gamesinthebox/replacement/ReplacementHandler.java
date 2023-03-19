package me.hsgamer.gamesinthebox.replacement;

import org.bukkit.command.CommandSender;

public interface ReplacementHandler {
    ReplacementHandler EMPTY = s -> null;

    String replace(String input);

    default String replace(CommandSender sender, String input) {
        return replace(input);
    }
}
