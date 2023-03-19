package me.hsgamer.gamesinthebox.picker;

import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.replacement.ReplacementHandler;

import java.util.Map;

public interface GamePicker extends ReplacementHandler {
    GamePicker EMPTY = new GamePicker() {
        @Override
        public String replace(String input) {
            return null;
        }

        @Override
        public void setup(Map<String, GameArena> arenaMap) {
            // EMPTY
        }

        @Override
        public GameArena pick() {
            return null;
        }

        @Override
        public boolean canPick() {
            return false;
        }
    };

    void setup(Map<String, GameArena> arenaMap);

    GameArena pick();

    boolean canPick();
}
