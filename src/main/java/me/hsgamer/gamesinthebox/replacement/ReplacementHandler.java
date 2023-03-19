package me.hsgamer.gamesinthebox.replacement;

import org.bukkit.command.CommandSender;

public interface ReplacementHandler {
    String getReplacement(String name);

    default String getReplacement(CommandSender sender, String name) {
        return getReplacement(name);
    }
}
