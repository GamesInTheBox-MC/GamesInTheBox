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
package me.hsgamer.gamesinthebox.game.template;

import me.hsgamer.gamesinthebox.expansion.SingleGameExpansion;
import me.hsgamer.gamesinthebox.expansion.extra.Reloadable;
import me.hsgamer.gamesinthebox.game.Game;
import me.hsgamer.gamesinthebox.game.simple.SimpleGame;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArenaAction;
import me.hsgamer.gamesinthebox.game.template.config.GameConfig;
import me.hsgamer.gamesinthebox.game.template.config.GameMessageConfig;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.proxy.ConfigGenerator;
import me.hsgamer.minigamecore.base.Feature;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * The expansion to register the game using {@link TemplateGameArenaLogic}
 */
public abstract class TemplateGameExpansion extends SingleGameExpansion implements Reloadable {
    private final GameConfig gameConfig = ConfigGenerator.newInstance(GameConfig.class, new BukkitConfig(new File(getDataFolder(), "game.yml")));
    private final GameMessageConfig gameMessageConfig = ConfigGenerator.newInstance(GameMessageConfig.class, new BukkitConfig(new File(getDataFolder(), "game-messages.yml")));

    /**
     * Create a new arena logic
     *
     * @param arena the arena
     * @return the arena logic
     */
    public abstract TemplateGameArenaLogic createArenaLogic(TemplateGameArena arena);

    /**
     * Get the display name of the game
     *
     * @return the display name
     */
    public abstract String getDisplayName();

    /**
     * Get the default hologram lines by the name
     *
     * @param name the name to get
     * @return the default hologram lines
     */
    public List<String> getDefaultHologramLines(String name) {
        return Collections.emptyList();
    }

    /**
     * Get the editor of the game.
     * Override this method if you want to use your own editor.
     *
     * @param game the game
     * @return the editor
     */
    public TemplateGameEditor getEditor(TemplateGame game) {
        return new TemplateGameEditor(game);
    }

    /**
     * Get the arena action of the game.
     * Override this method if you want to use your own arena action.
     *
     * @param arena the arena
     * @return the arena action
     */
    public SimpleGameArenaAction getArenaAction(TemplateGameArena arena) {
        return new SimpleGameArenaAction(arena);
    }

    /**
     * Get the features of the game
     *
     * @param game the game
     * @return the features
     */
    public List<Feature> getGameFeatures(SimpleGame game) {
        return Collections.emptyList();
    }

    @Override
    protected @NotNull Game getGame() {
        return new TemplateGame(this);
    }

    @Override
    public void onReload() {
        gameConfig.reloadConfig();
        gameMessageConfig.reloadConfig();
    }

    /**
     * Get the game config
     *
     * @return the game config
     */
    public GameConfig getGameConfig() {
        return gameConfig;
    }

    /**
     * Get the game message config
     *
     * @return the game message config
     */
    public GameMessageConfig getGameMessageConfig() {
        return gameMessageConfig;
    }
}
