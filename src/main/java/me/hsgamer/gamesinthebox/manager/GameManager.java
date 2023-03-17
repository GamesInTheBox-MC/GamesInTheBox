package me.hsgamer.gamesinthebox.manager;

import me.hsgamer.gamesinthebox.game.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GameManager {
    private final Map<String, Game> gameMap = new HashMap<>();

    public void addGame(String type, Game game) {
        if (gameMap.containsKey(type)) {
            throw new IllegalArgumentException("Game type " + type + " already exists");
        }
        gameMap.put(type, game);
    }

    public Optional<Game> getGame(String type) {
        return Optional.ofNullable(gameMap.get(type));
    }

    public void removeGame(String type) {
        gameMap.remove(type);
    }

    public void callClear() {
        gameMap.values().forEach(Game::clear);
    }

    public void callInit() {
        gameMap.values().forEach(game -> {
            game.init();
            game.postInit();
        });
    }
}
