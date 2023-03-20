package me.hsgamer.gamesinthebox.planner.feature;

import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.manager.GameManager;
import me.hsgamer.gamesinthebox.picker.GamePicker;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.minigamecore.base.Feature;

import java.util.*;
import java.util.logging.Level;

public class PickFeature implements Feature {
    private final Planner planner;
    private final Map<String, GameArena> gameArenaMap = new HashMap<>();
    private GamePicker gamePicker = GamePicker.EMPTY;
    private GameArena forceNextGame;

    public PickFeature(Planner planner) {
        this.planner = planner;
    }

    @Override
    public void postInit() {
        GamesInTheBox plugin = planner.getFeature(PluginFeature.class).getPlugin();

        gamePicker = plugin.getGamePickerManager().build(planner);

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

        gamePicker.setup(Collections.unmodifiableMap(gameArenaMap));
    }

    public boolean canPick() {
        if (forceNextGame != null) {
            return true;
        }
        return gamePicker.canPick();
    }

    public GameArena getNextGame() {
        if (forceNextGame != null) {
            GameArena gameArena = forceNextGame;
            forceNextGame = null;
            return gameArena;
        }
        return gamePicker.pick();
    }

    public boolean setNextGame(String name) {
        if (gameArenaMap.containsKey(name)) {
            forceNextGame = gameArenaMap.get(name);
            return true;
        }
        return false;
    }

    public Collection<String> getGameArenaNames() {
        return Collections.unmodifiableCollection(gameArenaMap.keySet());
    }

    public GamePicker getGamePicker() {
        return gamePicker;
    }
}
