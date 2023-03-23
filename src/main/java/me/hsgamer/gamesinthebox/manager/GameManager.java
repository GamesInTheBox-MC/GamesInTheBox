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
package me.hsgamer.gamesinthebox.manager;

import me.hsgamer.gamesinthebox.game.Game;
import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.hscore.builder.Builder;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 * The manager that handles all {@link Game} and {@link GameArena}
 */
public class GameManager extends Builder<GameManager.Input, GameArena> {
    /**
     * Register a game
     *
     * @param game the game
     * @param type the type of the game
     * @return the registered element, used for unregistering
     */
    public FunctionElement<Input, GameArena> register(Game game, String... type) {
        return register(new GameFunction(game), type);
    }

    /**
     * Call the {@link Game#clear()} method for all games
     */
    public void callClear() {
        getRegisteredMap().values().forEach(function -> {
            if (function instanceof GameFunction) {
                ((GameFunction) function).game.clear();
            }
        });
    }

    /**
     * Call the {@link Game#init()} and {@link Game#postInit()} methods for all games
     */
    public void callInit() {
        getRegisteredMap().values().forEach(function -> {
            if (function instanceof GameFunction) {
                Game game = ((GameFunction) function).game;
                game.init();
                game.postInit();
            }
        });
    }

    /**
     * Build the {@link GameArena}
     *
     * @param type    the type of the game
     * @param name    the name of the arena
     * @param planner the planner that the arena belongs to
     * @return the {@link GameArena}
     */
    public Optional<GameArena> build(String type, String name, Planner planner) {
        return build(type, new Input(name, planner));
    }

    /**
     * Get the {@link Game} by its type
     *
     * @param type the type of the game
     * @return the {@link Game}
     */
    public Optional<Game> getGame(String type) {
        return Optional.ofNullable(getRegisteredMap().get(type))
                .filter(GameFunction.class::isInstance)
                .map(GameFunction.class::cast)
                .map(gameFunction -> gameFunction.game);
    }

    private static class GameFunction implements BiFunction<String, Input, GameArena> {
        private final Game game;

        private GameFunction(Game game) {
            this.game = game;
        }

        @Override
        public GameArena apply(String s, Input input) {
            return game.createArena(input.name, input.planner);
        }
    }

    /**
     * The input for building the {@link GameArena}
     */
    public static class Input {
        private final String name;
        private final Planner planner;

        private Input(String name, Planner planner) {
            this.name = name;
            this.planner = planner;
        }
    }
}
