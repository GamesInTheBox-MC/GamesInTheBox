package me.hsgamer.gamesinthebox.planner.feature;

import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.manager.GameManager;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.minigamecore.base.Feature;

import java.util.*;
import java.util.logging.Level;

public class PickFeature implements Feature {
    private final Planner planner;
    private final Map<String, GameArena> gameArenaMap = new HashMap<>();
    private long nextGameTime = System.currentTimeMillis(); // TODO
    private GameArena nextGame;

    public PickFeature(Planner planner) {
        this.planner = planner;
    }

    public long getNextGameTime() {
        return nextGameTime;
    }

    public void completeTime() {
        this.nextGameTime = System.currentTimeMillis();
    }

    @Override
    public void postInit() {
        GamesInTheBox plugin = planner.getFeature(PluginFeature.class).getPlugin();
        GameManager gameManager = plugin.getGameManager();
        planner.getFeature(PlannerConfigFeature.class).getValues("settings", false).forEach((key, value) -> {
            if (!(value instanceof Map)) {
                return;
            }
            //noinspection unchecked
            Map<String, Object> map = (Map<String, Object>) value;
            if (!map.containsKey("type")) {
                return;
            }
            String type = Objects.toString(map.get("type"));
            try {
                gameManager.getGame(type).map(game -> game.createArena(key, planner)).ifPresent(gameArena -> gameArenaMap.put(key, gameArena));
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, e, () -> "Failed to load game " + key + " in arena " + planner.getName());
            }
        });
    }

    public GameArena getNextGame() {
        if (nextGame != null) {
            GameArena gameArena = nextGame;
            nextGame = null;
            return gameArena;
        }
        if (gameArenaMap.isEmpty()) {
            return null;
        }
        return null; // TODO
    }

    public boolean setNextGame(String name) {
        if (gameArenaMap.containsKey(name)) {
            nextGame = gameArenaMap.get(name);
            return true;
        }
        return false;
    }

    public Collection<String> getGameArenaNames() {
        return Collections.unmodifiableCollection(gameArenaMap.keySet());
    }
}
