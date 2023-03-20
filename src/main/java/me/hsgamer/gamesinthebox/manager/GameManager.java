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
