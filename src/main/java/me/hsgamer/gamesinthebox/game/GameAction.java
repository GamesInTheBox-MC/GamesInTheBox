package me.hsgamer.gamesinthebox.game;

import java.util.Collections;
import java.util.List;

public interface GameAction {
    GameAction EMPTY = new GameAction() {
        @Override
        public List<String> getActions() {
            return Collections.emptyList();
        }

        @Override
        public List<String> getActionArgs(String action, String... args) {
            return Collections.emptyList();
        }

        @Override
        public void performAction(String action, String... args) {
            // EMPTY
        }
    };

    List<String> getActions();

    List<String> getActionArgs(String action, String... args);

    void performAction(String action, String... args);
}
