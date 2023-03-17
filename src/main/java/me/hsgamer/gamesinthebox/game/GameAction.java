package me.hsgamer.gamesinthebox.game;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public interface GameAction {
    GameAction EMPTY = new GameAction() {
        @Override
        public List<String> getActions() {
            return Collections.emptyList();
        }

        @Override
        public List<String> getActionArgs(CommandSender sender, String action, String... args) {
            return Collections.emptyList();
        }

        @Override
        public boolean performAction(CommandSender sender, String action, String... args) {
            return false;
        }
    };

    List<String> getActions();

    List<String> getActionArgs(CommandSender sender, String action, String... args);

    boolean performAction(CommandSender sender, String action, String... args);
}
