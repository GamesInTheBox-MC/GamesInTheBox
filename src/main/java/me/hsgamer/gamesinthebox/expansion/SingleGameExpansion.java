package me.hsgamer.gamesinthebox.expansion;

import me.hsgamer.gamesinthebox.game.Game;
import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.manager.GameManager;
import me.hsgamer.hscore.builder.Builder;

public abstract class SingleGameExpansion implements GameExpansion {
    private Game game;
    private Builder.FunctionElement<GameManager.Input, GameArena> element;

    protected void enable() {
        // EMPTY
    }

    protected void disable() {
        // EMPTY
    }

    protected abstract Game getGame();

    protected abstract String[] getGameType();

    @Override
    public final void onEnable() {
        enable();

        game = getGame();
        game.init();
        game.postInit();

        element = getPlugin().getGameManager().register(game, getGameType());
    }

    @Override
    public final void onDisable() {
        game.clear();

        disable();

        getPlugin().getGameManager().remove(element);
    }
}
