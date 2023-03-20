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

public class GameManager extends Builder<GameManager.Input, GameArena> {
    public FunctionElement<Input, GameArena> register(Game game, String... type) {
        return register(new GameFunction(game), type);
    }

    public void callClear() {
        getRegisteredMap().values().forEach(function -> {
            if (function instanceof GameFunction) {
                ((GameFunction) function).game.clear();
            }
        });
    }

    public void callInit() {
        getRegisteredMap().values().forEach(function -> {
            if (function instanceof GameFunction) {
                Game game = ((GameFunction) function).game;
                game.init();
                game.postInit();
            }
        });
    }

    public Optional<GameArena> build(String type, String name, Planner planner) {
        return build(type, new Input(name, planner));
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

    public static class Input {
        private final String name;
        private final Planner planner;

        private Input(String name, Planner planner) {
            this.name = name;
            this.planner = planner;
        }
    }
}
