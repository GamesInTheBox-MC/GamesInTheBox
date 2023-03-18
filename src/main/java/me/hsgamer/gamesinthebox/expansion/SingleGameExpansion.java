package me.hsgamer.gamesinthebox.expansion;

import me.hsgamer.gamesinthebox.game.Game;

public abstract class SingleGameExpansion implements GameExpansion {
    private Game game;

    protected void enable() {
        // EMPTY
    }

    protected void disable() {
        // EMPTY
    }

    protected abstract Game getGame();

    protected abstract String getGameType();

    @Override
    public final void onEnable() {
        enable();

        game = getGame();
        game.init();
        game.postInit();

        getPlugin().getGameManager().addGame(getGameType(), game);
    }

    @Override
    public final void onDisable() {
        game.clear();

        disable();

        getPlugin().getGameManager().removeGame(getGameType());
    }
}
