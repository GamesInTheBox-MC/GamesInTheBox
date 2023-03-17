package me.hsgamer.gamesinthebox.planner.feature;

import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.manager.GameManager;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.minigamecore.base.Feature;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public class PickFeature implements Feature {
    private final Planner planner;
    private final Map<String, GameArena> gameArenaMap = new HashMap<>();
    private final long getNextGameTime = System.currentTimeMillis(); // TODO

    public PickFeature(Planner planner) {
        this.planner = planner;
    }

    public long getGetNextGameTime() {
        return getNextGameTime;
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
        if (gameArenaMap.isEmpty()) {
            return null;
        }
        return null; // TODO
    }
}
