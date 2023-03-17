package me.hsgamer.gamesinthebox.manager;

import me.hsgamer.gamesinthebox.game.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GameManager {
    private final Map<String, Game> gameMap = new HashMap<>();

    public void addGame(String name, Game game) {
        gameMap.put(name, game);
    }

    public Optional<Game> getGame(String name) {
        return Optional.ofNullable(gameMap.get(name));
    }

    public void removeGame(String name) {
        gameMap.remove(name);
    }
}
