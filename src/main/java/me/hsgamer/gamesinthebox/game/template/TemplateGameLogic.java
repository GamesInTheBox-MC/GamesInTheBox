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

import me.hsgamer.gamesinthebox.game.simple.SimpleGame;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArenaAction;
import me.hsgamer.gamesinthebox.game.simple.feature.SimplePointFeature;
import me.hsgamer.gamesinthebox.game.template.config.GameConfig;
import me.hsgamer.gamesinthebox.game.template.config.GameMessageConfig;
import me.hsgamer.minigamecore.base.Feature;

import java.util.Collections;
import java.util.List;

/**
 * The logic of the {@link TemplateGame}
 */
public interface TemplateGameLogic {
    /**
     * Create a new arena logic
     *
     * @param arena the arena
     * @return the arena logic
     */
    TemplateGameArenaLogic createArenaLogic(TemplateGameArena arena);

    /**
     * Get the display name of the game
     *
     * @return the display name
     */
    String getDisplayName();

    /**
     * Get the game config
     *
     * @return the game config
     */
    GameConfig getGameConfig();

    /**
     * Get the game message config
     *
     * @return the game message config
     */
    GameMessageConfig getGameMessageConfig();

    /**
     * Get the point values
     *
     * @return the point values
     */
    default List<SimplePointFeature.PointValue> getPointValues() {
        return Collections.emptyList();
    }

    /**
     * Get the default hologram lines by the name
     *
     * @param name the name to get
     * @return the default hologram lines
     */
    default List<String> getDefaultHologramLines(String name) {
        return Collections.emptyList();
    }

    /**
     * Get the editor of the game.
     * Override this method if you want to use your own editor.
     *
     * @param game the game
     * @return the editor
     */
    default TemplateGameEditor getEditor(TemplateGame game) {
        return new TemplateGameEditor(game);
    }

    /**
     * Get the arena action of the game.
     * Override this method if you want to use your own arena action.
     *
     * @param arena the arena
     * @return the arena action
     */
    default SimpleGameArenaAction getArenaAction(TemplateGameArena arena) {
        return new SimpleGameArenaAction(arena);
    }

    /**
     * Get the features of the game
     *
     * @param game the game
     * @return the features
     */
    default List<Feature> getGameFeatures(SimpleGame game) {
        return Collections.emptyList();
    }
}
