/*
   Copyright 2023-2023 Huynh Tien

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package me.hsgamer.gamesinthebox.planner.feature;

import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.manager.GameManager;
import me.hsgamer.gamesinthebox.picker.GamePicker;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.minigamecore.base.Feature;

import java.util.*;
import java.util.logging.Level;

/**
 * The {@link Feature} to initialize the {@link GameArena} and handle the {@link GamePicker}.
 * The {@link GameArena} will be initialized from {@link PlannerConfigFeature} with the format:
 * <pre>
 *     settings:
 *       arena1:
 *         type: gameType
 *         ...
 *       arena2:
 *         type: gameType
 *         ...
 *       ...
 * </pre>
 */
public class GamePickerFeature implements Feature {
    private final Planner planner;
    private final Map<String, GameArena> gameArenaMap = new HashMap<>();
    private GamePicker gamePicker = GamePicker.EMPTY;
    private GameArena forceNextGame;

    /**
     * Create a new {@link GamePickerFeature}
     *
     * @param planner the {@link Planner}
     */
    public GamePickerFeature(Planner planner) {
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
                gameManager.build(type, key, planner).ifPresent(gameArena -> gameArenaMap.put(key, gameArena));
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, e, () -> "Failed to load game " + key + " in arena " + planner.getName());
            }
        });

        gamePicker.setup(Collections.unmodifiableMap(gameArenaMap));
    }

    /**
     * Check if the feature can pick the next game.
     *
     * @return true if the feature can pick the next game
     */
    public boolean canPick() {
        if (forceNextGame != null) {
            return true;
        }
        return gamePicker.canPick();
    }

    /**
     * Get the next game
     *
     * @return the next game
     */
    public GameArena getNextGame() {
        if (forceNextGame != null) {
            GameArena gameArena = forceNextGame;
            forceNextGame = null;
            return gameArena;
        }
        return gamePicker.pick();
    }

    /**
     * Set the next game.
     * It will be used in the next time {@link #getNextGame()} is called.
     *
     * @param name the name of the game
     * @return true if the game is found
     */
    public boolean setNextGame(String name) {
        if (gameArenaMap.containsKey(name)) {
            forceNextGame = gameArenaMap.get(name);
            return true;
        }
        return false;
    }

    /**
     * Get the names of the game arenas
     *
     * @return the names of the game arenas
     */
    public Collection<String> getGameArenaNames() {
        return Collections.unmodifiableCollection(gameArenaMap.keySet());
    }

    /**
     * Get the {@link GameArena} by name
     *
     * @param name the name of the game arena
     * @return the {@link GameArena}
     */
    public Optional<GameArena> getGameArena(String name) {
        return Optional.ofNullable(gameArenaMap.get(name));
    }

    /**
     * Get the {@link GamePicker}
     *
     * @return the {@link GamePicker}
     */
    public GamePicker getGamePicker() {
        return gamePicker;
    }
}
